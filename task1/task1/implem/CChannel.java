package task1.implem;
import given.Broker;
import given.Channel;
import given.CircularBuffer;



public class CChannel extends Channel {

	// Input and output buffers
	private CircularBuffer in, out;
	// Disconnection state flag
	private boolean disconnected = false;
	// The Channel you are communicating with ('distant' channel)
	private CChannel linkedChannel;
	// Communication port
	private int port;

	//
	protected CChannel(Broker broker, int port) {
		this.in = new CircularBuffer(256);
		this.out = new CircularBuffer(256);
	}

	//
	protected CChannel(Broker broker, int port, CChannel channel) {
		this.port = port;
		this.linkedChannel = channel;
		channel.setLinkedChannel(this);
		this.in = channel.getOutBuffer();
		this.out = channel.getInBuffer();
	}

	//
	@Override
	public synchronized int read(byte[] bytes, int offset, int length) throws InterruptedException {
	    int readbytes = 0;
	    while (length - readbytes > 0) {
	        while (in.empty()) {
	            // Si buffer vide, on attend qu'il y ait des données
	            wait();  // Attente
	        }
	        // Lire un octet du buffer
	        bytes[offset + readbytes] = in.pull();
	        readbytes++;
	    }
	    notify();
	    return readbytes;  // Retourner le nombre d'octets effectivement lus
	}


	//
	@Override
	public synchronized int write(byte[] bytes, int offset, int length) throws InterruptedException {
	    int writtenbytes = 0;
	    while (length - writtenbytes > 0) {
	        while (out.full()) {
	            // Si buffer plein, on attend qu'il y ait de la place
	            wait();  // Attente
	        }
	        // écrireun octet du buffer
	        out.push(bytes[offset+writtenbytes]);
	        writtenbytes++;
	    }
	    notify();
	    return writtenbytes;  // Retourner le nombre d'octets effectivement écrits
	}

	/*
	 * Set the disconnected flag to true.
	 * In a synchronized block on the in buffer object, notify all waiting threads on the in buffer.
	 * In a synchronized block on the out buffer object, notify all waiting threads on the out buffer.
	 */
	@Override
	public void disconnect() {
		this.disconnected = true;
	}

	//
	@Override
	public synchronized boolean disconnected() {
		return this.disconnected;
	}

	//
	public CircularBuffer getInBuffer() {
		return this.in;
	}

	//	  return the out buffer in field
	public CircularBuffer getOutBuffer() {
		return this.out;
	}

	/*
	 * set the linkedChannel field to this channel
	 */
	public void setLinkedChannel(CChannel channel) {
		this.linkedChannel = channel;
	}

	// return true if the linkedChannel field is not null
	public boolean isLinked() {
		return this.linkedChannel != null;
	}


	// Return the value of the port field
	public int getPort() {
		return port;
	}
	

}
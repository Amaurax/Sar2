package task1.implem;
import given.Broker;
import given.Channel;
import given.CircularBuffer;



public class CChannel extends Channel {

	CircularBuffer in, out;
	CChannel linkedChannel;
	boolean disconnected = false;
	int port;
	boolean dangling;
	String rname;

	//
	protected CChannel(Broker broker, int port) {
		super();
		this.port = port;
		this.in = new CircularBuffer(256);
	}


	
	void connect(CChannel rch, String name) {
		this.linkedChannel = rch;
		rch.linkedChannel = this;
		this.out = rch.in;
		rch.out = this.in;
		rname = name;
	}

	//
	@Override
	public synchronized int read(byte[] bytes, int offset, int length) throws Exception, InterruptedException {
		if (disconnected)
			throw new Exception("disconnected");
	    int readbytes = 0;
	    try {
	    	while (readbytes == 0) {
	    		if (in.empty()) {
	    			synchronized (in) {
	    				while (in.empty()) {
	    					if (disconnected || dangling)
	    						throw new Exception("disconnected");
	    					try {
	    						in.wait();
	    					} catch (InterruptedException ex) {
	    						// nothing to do here 
	    					}
	    				}
	    			}
	    		}
	    		while (readbytes < length && !in.empty()) {
	    			byte val = in.pull();
	    			bytes[offset + readbytes] = val;
	    			readbytes++;
	    		}
	    		if (readbytes != 0) 
	    			synchronized (in) {
	    				in.notify();
	    			}
	    	}
	    }  catch (Exception ex) {
	    		if (!disconnected) {
	    			disconnected = true;
	    			synchronized (out) {
	    				out.notifyAll();
	    			}
	    		}
	    		throw ex;
	    	}
	    	return readbytes;
	    }
	
	    	

	//
	@Override
	public synchronized int write(byte[] bytes, int offset, int length) throws InterruptedException, Exception {
		if (disconnected)
			throw new Exception("disconnected");
		int wbytes = 0;
		while (wbytes == 0) {
			if (out.full()) {
				synchronized (out) {
					while (out.full()) {
						if (disconnected())
							throw new Exception("Disconnected");
						if (dangling)
							return length;
						try {
							out.wait();
						} catch (InterruptedException ex) {
							//nothing to do here
						}
					}
				}
			}
			while (wbytes < length && !out.full()) {
				byte val = bytes[offset + wbytes];
				out.push(val);
				wbytes++;
			}
			if (wbytes !=0)
				synchronized (out) {
					out.notify();
			}
		}
		return wbytes;
	}

	
	@Override
	public void disconnect() {
		synchronized(this) {
			if (this.disconnected()) 
				return;
			disconnected = true;
			linkedChannel.dangling = true;
		}
		synchronized (out) {
			out.notifyAll();
		}
		synchronized (in) {
			in.notifyAll();
		}
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
package event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import given.Channel;
import given.event.MessageQueue;
import given.event.QueueBroker;

public class MMessageQueue extends MessageQueue {


	private static final int HEADER_SIZE = 4;


	private Channel channel;
	private Executor executor;
	private QueueBroker pbroker;
	private Listener listener;
	private Thread reader;
	private Writer writer = new Writer();


	public MMessageQueue(Channel channel, QueueBroker broker, Executor executor) {
		this.channel = channel;
		this.executor = executor;
		this.pbroker = broker;
		writer.start();
	}



	@Override
	public synchronized void setListener(Listener l) {
		this.listener = l;		
		if(reader==null) {
			reader = new Thread() {
				@Override
				public void run() { //
					while(!closed()) {
						synchronized(this){

							byte[] header = new byte[HEADER_SIZE];
							int bytesRead = 0;
							while (bytesRead < HEADER_SIZE) {
								try {
									bytesRead += channel.read(header, bytesRead, HEADER_SIZE - bytesRead);
								} catch (Exception e) {
									close();
									break;								}
							}

							//Retrieve msg's length written in the header
							int length = ((header[0] & 0xFF) << 24) |
									((header[1] & 0xFF) << 16) |
									((header[2] & 0xFF) << 8)  |
									(header[3] & 0xFF);

							byte[] message = new byte[length];


							// receiving the message
							bytesRead = 0;
							while(bytesRead<length) {
								try {
									bytesRead += channel.read(message, bytesRead, length-bytesRead);
								} catch (Exception e) {
									close();
									break;
								}
							}
							if(message.length>0) {
								Runnable r = new Runnable() {
									@Override
									public void run() {
										listener.received(message);
									}
								};
								executor.post(r);
							}
						}
					}
				}
			};
			reader.start();
		}
	}



	@Override
	public synchronized void close() {
		if(!channel.disconnected()) channel.disconnect();
		if(listener!=null) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					listener.closed();
				}
			};
			executor.post(r);
		}
		writer.kill();
	}


	@Override
	public synchronized boolean closed() {
		return channel.disconnected();
	}

	@Override
	public synchronized boolean send(byte[] msg) {
		writer.send(Arrays.copyOf(msg, msg.length)); // ownership
		return true;
	}



	public class Writer extends Thread {
		// FIFO queue of messages to send
		private List<byte[]> queue = new ArrayList<>();
		// the alive flag
		private boolean alive = true;	

		private Object lock_in = new Object(), lock_out = new Object();


		@Override
		public void run() {
			synchronized(lock_in) {

				while(alive) {
					if(queue.size()>0) {
						byte[] msg = queue.get(0);
						byte[] header = new byte[HEADER_SIZE];
						header[0] = (byte) (msg.length >> 24);
						header[1] = (byte) (msg.length >> 16);
						header[2] = (byte) (msg.length >> 8);
						header[3] = (byte) msg.length;

						//send header with message's length
						int bytesSent = 0;
						while(bytesSent < HEADER_SIZE) {
							try {
								bytesSent += channel.write(header, bytesSent, HEADER_SIZE-bytesSent);
							} catch (Exception e) {
								close();
								break;
							}
						}

						// send
						bytesSent = 0;
						while (bytesSent < msg.length) {
							try {
								bytesSent += channel.write(msg, bytesSent, msg.length - bytesSent);
							} catch (Exception e) {
								close();
								break;
							}
						}

						if(bytesSent==msg.length) //err if msg.length
							queue.remove(msg);

					} else {
						synchronized(this) {
							try {
								wait();
							} catch (InterruptedException e) {
								// nothing to do here
							}
						}
					}
				}
			}
		}


		public void kill() {
			alive = false;
			synchronized(this) {
				notify();
			}
		}

		public synchronized void send(byte[] message) {
			queue.add(message);
			notify();
		}
	}



}

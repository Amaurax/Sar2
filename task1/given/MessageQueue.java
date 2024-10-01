package given;

public abstract class MessageQueue {
	
	public void send(byte[] bytes, int offset, int length) throws Exception {
	}
	
	public byte[] receive() throws Exception {
		return null;
	}
	
	public void close() {
	}
	
	public boolean closed() {
		return false;
	}
}
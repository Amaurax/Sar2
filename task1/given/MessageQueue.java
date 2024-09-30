package given;

public abstract class MessageQueue {
	public void send(byte[] bytes, int offset, int length) {
	}
	
	public byte[] receive() {
		return null;
	}
	
	public void close() {
	}
	
	public boolean closed() {
		return false;
	}
}
package given.event;

public abstract class MessageQueue {
	interface Listener {
		void received(byte[] msg);
		void sent(Message msg);
		void closed();
	}
	
	void setListener(Listener l) {
	}
	
	boolean send(Message msg) {
		return false;
	}
	
	void close() {
	}
	
	boolean closed() {
		return false;
	}
}

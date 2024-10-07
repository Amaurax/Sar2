package given.event;

public abstract class QueueBroker {

	public QueueBroker(String name) {
	}

	interface AcceptListener {
		void accepted(MessageQueue queue);
	}

	boolean bind(int port, AcceptListener listener) {
		return false;
	}

	boolean unbind(int port) {
		return false;
	}

	interface ConnectListener {
		void connected(MessageQueue queue);
		void refused();
	}

	boolean connect(String name, int port,
			ConnectListener listener) {
		return false;
	}
}
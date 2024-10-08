package given.event;

import event.Executor;
import given.Broker;

public abstract class QueueBroker {
	
	public Broker broker;
	public Executor pump;

		
	public QueueBroker(Executor pump, Broker broker) {
		this.broker = broker;
		this.pump = pump;
	}

	public QueueBroker(String name) {
	}

	public interface AcceptListener {
		void accepted(MessageQueue queue);
	}

	public boolean bind(int port, AcceptListener listener) {
		return false;
	}

	public boolean unbind(int port) {
		return false;
	}

	public interface ConnectListener {
		void connected(MessageQueue queue);
		void refused();
	}

	public boolean connect(String name, int port,
			ConnectListener listener) {
		return false;
	}
	
	public Executor getPump() {
		return pump;
	}
	
	public Broker getBroker() {
		return broker;
	}
}
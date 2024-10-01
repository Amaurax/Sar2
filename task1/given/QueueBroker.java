package given;

public abstract class QueueBroker {
	
	public Broker broker; // regular channel broker to handle connection

	
	public QueueBroker(Broker cbroker) {
		this.broker = cbroker;
	}


	public MessageQueue accept(int port) {
		return null;
	}
	
	public MessageQueue connect(String name, int port) {
		return null;
	}
	
}
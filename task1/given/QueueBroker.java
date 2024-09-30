package given;

public abstract class QueueBroker {
	
	public String name;
	public Broker cbroker; // regular channel broker to handle connection


	public QueueBroker(Broker broker, String name) {
		this.name = name;
		this.cbroker = broker;
	}
	
	
	public MessageQueue accept(int port) {
		return null;
	}
	
	public MessageQueue connect(String name, int port) {
		return null;
	}
	
}
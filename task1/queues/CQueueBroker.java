package queues;

import given.Channel;
import given.MessageQueue;
import given.QueueBroker;
import task1.implem.CBroker;

public class CQueueBroker extends QueueBroker {
	

	CQueueBroker(CBroker broker, String name) {
		super(broker, name);
		QueueBrokerManager.addBroker(this);
	}
	
	@Override
	public	MessageQueue accept(int port) {
		Channel c = cbroker.accept(port);
		//creer messagequeue par dessus
		return null;//a modifier
	}
	
	@Override
	public MessageQueue connect(String name, int port) {
		Channel c = cbroker.accept(port);
		//
		return null;
	}
	
}

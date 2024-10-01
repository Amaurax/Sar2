package queues;

import given.Broker;
import given.Channel;
import given.MessageQueue;
import given.QueueBroker;
import task1.implem.CBroker;

public class CQueueBroker extends QueueBroker {
	

	public CQueueBroker(Broker broker ) {
		super(broker);
	}
	
	@Override
	public	MessageQueue accept(int port) {
		Channel c = broker.accept(port);
		MessageQueue messagequeue = new CMessageQueue(c, this);
		return messagequeue;	}
	
	@Override
	public MessageQueue connect(String name, int port) {
		Channel c = broker.accept(port);
		MessageQueue messagequeue = new CMessageQueue(c, this);
		return messagequeue;
	}
	
}

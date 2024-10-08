package event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import given.Broker;
import given.Channel;
import given.event.MessageQueue;
import given.event.QueueBroker;

public class MQueueBroker extends QueueBroker {

	public MQueueBroker(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private Map<Integer, Object> binds = new HashMap<>();
	private Map<Integer, Channel> activeChannels = new HashMap<>();

	// list of unbinding ports
	private List<Integer> unbinds = new ArrayList<>();

	public MQueueBroker(Executor executor, Broker broker) {
		super(executor, broker);
	}

	@Override
	public synchronized boolean bind(int port, AcceptListener listener) {
		if (binds.containsKey(port)) return false;
		binds.put(port, new Object());

		Thread worker = new Thread() {            
			@Override
			public void run() {
				while (binds.containsKey(port) || !unbinds.contains(port)) {
					Channel channel = broker.accept(port);

					if (channel != null) {
						// Stocker le canal dans activeChannels
						activeChannels.put(port, channel);
						MMessageQueue messageQueue = new MMessageQueue(channel, self(), pump);

						Runnable r = new Runnable() {
							@Override
							public void run() {
								listener.accepted(messageQueue);                        
							}
						};
						getPump().post(r);
					}
				}
				unbinds.remove(port);
			}
		};
		worker.start();
		return true;
	}




	@Override
	public synchronized boolean unbind(int port) {
		Object lock = binds.get(port);
		if (lock == null) {
			return false;
		}

		synchronized (lock) {
			binds.remove(port);

			// Récupérer et fermer le canal associé
			Channel channel = activeChannels.get(port);
			if (channel != null) {
				channel.disconnect(); 
				activeChannels.remove(port);  
			}
			unbinds.add(port);
		}

		return true;
	}




	@Override
	public synchronized boolean connect(String name, int port, ConnectListener listener) {
		Thread worker = new Thread() {
			@Override
			public void run() {
				Channel channel = getBroker().connect(name, port);
				if(channel==null) {
					Runnable r = new Runnable() {
						@Override
						public void run() {
							listener.refused();
						}
					};
					getPump().post(r);
				}else {
					MMessageQueue messageQueue = new MMessageQueue(channel, self(), getPump());
					Runnable r = new Runnable() {
						@Override
						public void run() {
							listener.connected(messageQueue);
						}
					};
					getPump().post(r);
				}
			}
		};
		worker.start();
		return true;
	}

	private MQueueBroker self() {
		return this;
	}




}

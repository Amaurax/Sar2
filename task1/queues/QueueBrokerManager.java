package queues;

import java.util.HashMap;
import java.util.Map;

public class QueueBrokerManager {

		// map giving the Broker from its name, static ? pas indispensable si un seul brkmngr
		private static Map<String, CQueueBroker> queuebrokers = new HashMap<>();

			
			//  synchronized static
		public synchronized static void addBroker(CQueueBroker broker) throws IllegalArgumentException {
			if (isNameUsed(broker.name))
				throw new IllegalArgumentException("Broker" + broker.name + "already exists");
			queuebrokers.put(broker.name, broker);
		}


		public static boolean isNameUsed(String name) {
			return queuebrokers.containsKey(name);
		}
			
		public static CQueueBroker getBroker(String name) {
			return queuebrokers.get(name);
		}

}

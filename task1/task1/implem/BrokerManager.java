package task1.implem;
import java.util.HashMap;
import java.util.Map;


public class BrokerManager {

		// map giving the Broker from its name, static ? pas indispensable si un seul brkmngr
		private static Map<String, CBroker> brokers = new HashMap<>();

		
		//  synchronized static
		public static void addBroker(CBroker broker) throws IllegalArgumentException {
			if (isNameUsed(broker.name))
				throw new IllegalArgumentException("Broker name already used (" + broker.name + ")");
			brokers.put(broker.name, broker);
		}


		public static boolean isNameUsed(String name) {
			return brokers.containsKey(name);
		}

		
		public static CBroker getBroker(String name) {
			return brokers.get(name);
		}

}
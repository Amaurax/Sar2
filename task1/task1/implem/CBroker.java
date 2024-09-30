package task1.implem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import given.Broker;
import given.Channel;

public class CBroker extends Broker {
	
	//list of waiting rdvs for every port
	private Map<Integer, Rdv> rdvs = new HashMap<>();

	public CBroker(String name) {
		super(name);
		BrokerManager.addBroker(this);
	}


	@Override
	public Channel accept(int port) {
		Rdv rdv = null;
		// retrieve the rendez-vous
		synchronized(rdvs) {
			rdv = rdvs.get(port);
			if(rdv != null)
				throw new IllegalArgumentException("Port" + port +"already accepting");
			rdv = new Rdv();	
			rdvs.put(port,  rdv);
			rdvs.notifyAll();
		}
		Channel ch; 
		ch = rdv.accept(this, port);
		return ch;
	}

	//
	@Override
	public Channel connect(String name, int port) {
		CBroker b = (CBroker) BrokerManager.getBroker(name);
		if (b == null)
			return null;
		return b._connect(this, port);
	}
	
	private Channel _connect(CBroker b, int port) {
		Rdv rdv = null;
		synchronized (rdvs) {
			rdv = rdvs.get(port);
			while (rdv == null) {
				try {
					rdvs.wait();
				} catch (InterruptedException ex) {
					//nothinf to do here
				}
				rdv = rdvs.get(port);
			}
			rdvs.remove(port);
		}
		return rdv.connect(b,  port);
	}


	public String getName() {
		return name;
	}
	


}

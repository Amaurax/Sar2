package task1.implem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import given.Broker;
import given.Channel;

public class CBroker extends Broker {
	
	//list of waiting rdvs for every port
	private Map<Integer, List<Rdv>> rdvs = new HashMap<>();

	public CBroker(String name) {
		super(name);
		BrokerManager.addBroker(this);
	}


	@Override
	public Channel accept(int port) {
		// retrieve the rendez-vous list of the given port
		List<Rdv> rendezVous;
		synchronized(rdvs) {
			if(rdvs.get(port)==null) {
				rdvs.put(port, new ArrayList<>());
			}
			rendezVous = rdvs.get(port);
		}
		// retrieve the rendez-vous
		Rdv rdv;
		synchronized(rendezVous) {
			for(Rdv r : rendezVous)
				if(r.hasAccept())
					throw new IllegalArgumentException(this.toString()+" accept : Invalid port");
			if(rendezVous.isEmpty()) {
				rdv = new Rdv();
				rendezVous.add(rdv);
				rdv.accept(this);
			}else {
				rdv = rendezVous.get(0);
				rdv.accept(this);
				// create the connection and leave the other part in the rendez-vous for the connect()
				CChannel acceptChannel = new CChannel(this, port);
				rdv.setChannel(new CChannel(rdv.getDistantBroker(), port, acceptChannel));
				rendezVous.remove(rdv);
				return acceptChannel;
			}
		}
		return rdv.getChannel();
	}

	//
	@Override
	public Channel connect(String name, int port) {
		// retrieve the 'distant' CBroker
		CBroker broker = BrokerManager.getBroker(name);
		if (broker == null) {
			return null;
		}
		// retrieve the rendez-vous list of the given port
		List<Rdv> rendezVous;
		synchronized(broker.rdvs) {
			if(broker.rdvs.get(port)==null) {
				broker.rdvs.put(port, new ArrayList<>());
			}
			rendezVous = broker.rdvs.get(port);
		}
		// retrieve the rendez-vous
		Rdv rdv = null;
		synchronized(rendezVous) {
			for(Rdv r : rendezVous) {
				if(r.hasAccept()) {
					rdv = r;
					break;
				}
			}
			if(rdv==null) {
				rdv = new Rdv();
				rendezVous.add(rdv);
				rdv.connect(this);
			} else {
				rdv = rendezVous.get(0);
				rdv.connect(this);
				// create the connection and leave the other part in the rendez-vous for the accept()
				CChannel connectChannel = new CChannel(this, port);
				rdv.setChannel(new CChannel(rdv.getDistantBroker(), port, connectChannel));
				rendezVous.remove(rdv);
				return connectChannel;
			}
		}
		return rdv.getChannel();
	}


	public String getName() {
		return name;
	}
	


}

package task1.implem;

public class Rdv {


	private boolean hasConnect=false, hasAccept=false;
	//channel de communication distant
	private CChannel channel;
	//the broker a garder pour 2nde tache
	private CBroker distantBroker;
	
	// broker : accept
	public synchronized void accept(CBroker broker) {
		hasAccept = true;
		distantBroker = broker;
	}
	
	// broker : connect
	public synchronized void connect(CBroker broker) {
		hasConnect = true;
		distantBroker = broker;
	}
	
	//
	public synchronized boolean hasAccept() {
		return hasAccept;
	}
	
	//
	public synchronized boolean hasConnect() {
		return hasConnect;
	}
	
	//
	public CChannel getChannel() {
		return channel;
	}

	//
	public void setChannel(CChannel channel) {
		this.channel = channel;
	}

	//
	public CBroker getDistantBroker() {
		return distantBroker;
	}

	
}

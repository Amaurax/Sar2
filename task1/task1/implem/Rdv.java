package task1.implem;

import given.Broker;
import given.Channel;

public class Rdv {


	CChannel ac, cc;
	Broker ab, cb; 
		
	private void _wait() {
		while (ac == null || cc == null) {
			try {
				wait();
			} catch (InterruptedException ex) {
			//nothing to do here
			}
		}
	}
	
	// broker : accept
	synchronized Channel accept(CBroker ab, int port) {
		this.ab = ab;
		ac = new CChannel(ab, port);
		if (cc != null) {
			cc.connect(ac,  ab.getName());
//			notify();
		} else
			_wait();
		return ac; 		
	}
	
	// broker : connect
	 synchronized Channel connect(CBroker cb, int port) {
		this.cb = cb;
		cc = new CChannel(cb, port);
		if (ac != null) {
			ac.connect(cc, cb.getName());
			notify();
		} else
			_wait();
		return cc;
	}
	

	
}

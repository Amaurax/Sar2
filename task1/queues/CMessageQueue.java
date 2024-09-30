package queues;

import given.MessageQueue;
import task1.implem.CChannel;

public class CMessageQueue extends MessageQueue {

	private CChannel channel;
    private boolean closed = false;
    
    //pas beosin de croiser car déjà fait dans channel

    // Constructeur qui prend un channel comme paramètre
    public CMessageQueue(CChannel channel) {
        this.channel = channel;
    }
    
    @Override
    public void send(byte[] bytes, int offset, int length) {
    	//appel write du channel en boucle
	}
	
    @Override
    public byte[] receive() {
    	//appel read du channel en boucle
		return null;
	}
	
    @Override
    public void close() {
    	// verif message puis appel disconnect
	}
	
    @Override
    public boolean closed() {
    	//appel disconnected
		return false;
	}
    
    
}

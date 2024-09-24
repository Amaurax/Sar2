package given;

public abstract class Broker {
	
	    public String name; //private ou protected avec getter et setter si autorisé

	    public Broker(String name) {
	        this.name = name;
	    }

	    public abstract Channel accept(int port);

	    public abstract Channel connect(String name, int port);
	

}

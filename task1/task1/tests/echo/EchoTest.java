package task1.tests.echo;
import task1.implem.BrokerManager;
import task1.implem.CBroker;

public class EchoTest {
    public static void main(String[] args) {
    	
        System.out.println("Hello, World!");
        CBroker b1 =  new CBroker("Broker1"); //implémentation à définir
        CBroker b2 =  new CBroker("Broker2");

        // Lancer le serveur
        EchoServer server = new EchoServer(b1);
        server.start();
       
        System.out.println("Hello2!");
        
        // Lancer plusieurs clients
        for (int i = 0; i < 5; i++) {
            EchoClient client = new EchoClient(b2);
            client.start();
        }
    }
}

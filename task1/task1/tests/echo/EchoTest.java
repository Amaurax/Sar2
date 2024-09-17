package task1.tests.echo;
s
public class EchoTest {
    public static void main(String[] args) {
        // Créer un Broker partagé
        Broker sharedBroker = new BrokerImpl("SharedBroker"); //implémentation à définir

        // Lancer le serveur
        EchoServer server = new EchoServer(sharedBroker);
        server.start();

        // Lancer plusieurs clients
        for (int i = 0; i < 5; i++) {
            EchoClient client = new EchoClient(sharedBroker);
            client.start();
        }
    }
}

package task1.tests.echo;

import java.util.ArrayList;
import java.util.List;

public class EchoServer extends Task {
    public EchoServer(Broker broker) {
        super(broker, new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Channel channel = broker.accept(3); // Accepter les connexions sur le port 12345
                        byte[] buffer = new byte[256];
                        int bytesRead;
                        while ((bytesRead = channel.read(buffer, 0, buffer.length)) != -1) {
                            channel.write(buffer, 0, bytesRead); // Echo des données
                        }
                        channel.disconnect(); // Déconnexion une fois la communication terminée
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

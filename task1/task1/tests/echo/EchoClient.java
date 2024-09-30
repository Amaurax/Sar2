package task1.tests.echo;
import java.io.IOException;

import task1.implem.CBroker;
import task1.implem.CChannel;

public class EchoClient extends Task {
    public EchoClient(CBroker broker) {
    	//Runnable à définir dans echotest de préférdnce ?
        super(broker, new Runnable() {
            @Override
            public void run() {
                try {
                    CChannel channel = (CChannel) broker.connect("Broker1", 3); // Connexion au serveur
                    byte[] sequence = new byte[255];
                    for (int j = 0; j < 255; j++) {
                        sequence[j] = (byte) (j + 1);
                        System.out.println("Ercriture sequence " + j + " " + sequence[j]);

                    }
                    channel.write(sequence, 0, sequence.length); // Envoyer la séquence de bytes
                    byte[] echo = new byte[255];
                    int bytesRead = channel.read(echo, 0, echo.length); // Lire la réponse
                    for (int j = 0; j < bytesRead; j++) {
                        System.out.println("echo[" + j + "]; sequence["+ j +"] :" + echo[j] + " " + sequence[j]);

                        if (echo[j] != sequence[j]) {
                            System.out.println("Erreur d'echo à l'octet : " + j);
                        }
                    }
                    channel.disconnect(); // Déconnexion une fois la communication terminée
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

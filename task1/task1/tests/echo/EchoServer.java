package task1.tests.echo;

import java.util.ArrayList;
import java.util.List;

import task1.implem.CBroker;
import task1.implem.CChannel;

public class EchoServer extends Task {
	
    public EchoServer(CBroker broker) {
    	
        super(broker, new Runnable() {
        	
            @Override
            public void run() {
                System.out.println("In run!");
                try {
                    while (true) {
                        System.out.println("in while!");
                        CChannel channel = (CChannel) broker.accept(3); // Accepter les connexions sur le port 3
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
        System.out.println("after construct!");
    }
}

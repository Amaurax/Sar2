package queues;

import given.Channel;
import given.MessageQueue;
import given.QueueBroker;
import task1.implem.CChannel;

public class CMessageQueue extends MessageQueue {

	private Channel channel;
	private static final int HEADER_SIZE = 4;

	//pas beosin de croiser car déjà fait dans channel

	// Objects only here for synchronization purpose
	private Object lock_in = new Object(), lock_out = new Object();

	public CMessageQueue(Channel channel, QueueBroker broker) {
		this.channel = channel;
	}


	@Override
	public void send(byte[] bytes, int offset, int length) throws Exception {


		synchronized(lock_in) {

			// Envoi d'une entête avec la longueur du message sur 4 octets
			byte[] header = new byte[HEADER_SIZE];
			header[0] = (byte) (length >> 24);
			header[1] = (byte) (length >> 16);
			header[2] = (byte) (length >> 8);
			header[3] = (byte) length;

			// Envoi de l'entête contenant la taille du message
			int bytesSent = 0;
			while (bytesSent < HEADER_SIZE) {
				try {
					bytesSent += channel.write(header, bytesSent, HEADER_SIZE - bytesSent);
				} catch (Exception e) {
					this.close();
					throw new Exception(this.toString() + " send : Closed");
				}
			}

			// Envoi du message complet
			bytesSent = 0;
			while (bytesSent < length) {
				try {
					bytesSent += channel.write(bytes, offset + bytesSent, length - bytesSent);
				} catch (Exception e) {
					this.close();
					throw new Exception(this.toString() + " send : Closed");
				}
			}
		}
	}

	@Override
	public byte[] receive() throws Exception {
		synchronized(lock_out) {

			// Lecture de l'entête (4 octets qui contiennent la longueur du message)
			byte[] header = new byte[HEADER_SIZE];
			int bytesRead = 0;
			while (bytesRead < HEADER_SIZE) {
				try {
					bytesRead += channel.read(header, bytesRead, HEADER_SIZE - bytesRead);
				} catch (Exception e) {
					this.close();
					throw new Exception(this.toString() + " receive : Closed");
				}
			}

			// Calcul de la taille du message à partir de l'entête
			int length = ((header[0] & 0xFF) << 24) |
					((header[1] & 0xFF) << 16) |
					((header[2] & 0xFF) << 8)  |
					(header[3] & 0xFF);

			byte[] message = new byte[length];

			// Lecture du message complet
			bytesRead = 0;
			while (bytesRead < length) {
				try {
					bytesRead += channel.read(message, bytesRead, length - bytesRead);
				} catch (Exception e) {
					this.close();
					throw new Exception(this.toString() + " receive : Closed");
				}
			}

			return message;
		}
	}

	@Override
	public void close() {
		channel.disconnect();
	}

	@Override
	public boolean closed() {
		return channel.disconnected();
	}


}

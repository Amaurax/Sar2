package given;

public abstract class Task extends Thread {
    protected Broker broker;

    public Task(Broker b, Runnable r) {
	}

    public static Broker getBroker() {
        // Retourne le broker de la tâche actuelle
        return null; // A implémenter selon les besoins
    }
    
    Task(QueueBroker b, Runnable r) {
	}
    
    QueueBroker getQueueBroker() {
		return null;
	}
}

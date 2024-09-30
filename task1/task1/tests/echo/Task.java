package task1.tests.echo;

import task1.implem.CBroker;

public abstract class Task extends Thread {
    protected CBroker broker;
    private Runnable task;

    // Constructeur qui initialise le broker et le Runnable
    public Task(CBroker broker, Runnable task) {
        this.broker = broker;
        this.task = task;  // On sauvegarde le Runnable pour le lancer dans run()
    }

    @Override
    public void run() {
        // Exécute le Runnable associé à cette tâche
        if (task != null) {
           task.run();
        } else {
            System.out.println("No task defined for this thread.");
        }
    }

    // Méthode statique pour retourner le broker (selon les besoins)
    public static CBroker getBroker() {
        // Logique d'accès au broker selon tes besoins
        return null; // À implémenter si nécessaire
    }
}

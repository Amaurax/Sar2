package event;

import java.util.LinkedList;
import java.util.List;

public class Executor extends Thread {
	List<Runnable> queue;

	public Executor(String name) {
		super(name);
		queue = new LinkedList<Runnable>();
	}

	public void run() {
		Runnable r;
		while (true) {
			synchronized (queue) {
				while (queue.size() == 0)
					sleep();
				r = queue.remove(0);
			}
			r.run();
		}
	}

	public void post(Runnable r) {
		synchronized (queue) {
			queue.add(r); // at the end…
			queue.notify();
		}
	}

	private void sleep() {
		try {
			queue.wait();
		} catch (InterruptedException ex) {
			// nothing to do here.
		}
	}
}
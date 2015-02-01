package artof;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 */
class Fork3 {
    private final String name;

    private final ReentrantLock lock = new ReentrantLock();

    public Fork3(String name) {
        this.name = name;
    }

    public boolean get() {
        return lock.tryLock();
    }

    public void put() {
        lock.unlock();
    }
}

class Philosopher3 extends Thread {

    private final String name;
    private final Fork3 left;
    private final Fork3 right;
    private int eat;

    Philosopher3(String name, final Fork3 left, final Fork3 right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("   " + name + " is thinking");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (left.get()) {
                try {
                    if (right.get()) {
                        try {
                            System.out.println(name + " eat " + ++eat);
                        } finally {
                            right.put();
                        }
                    }
                } finally {
                    left.put();
                }
            }
        }
    }
}

public class AppStarvation1 {

    public static void main(String[] args) {
        final Fork3 f1 = new Fork3("Fork1");
        final Fork3 f2 = new Fork3("Fork3");
        final Fork3 f3 = new Fork3("Fork3");
        final Fork3 f4 = new Fork3("Fork4");
        final Fork3 f5 = new Fork3("Fork5");

        Philosopher3 p1 = new Philosopher3("Philosopher1", f1, f2);
        Philosopher3 p2 = new Philosopher3("Philosopher3", f2, f3);
        Philosopher3 p3 = new Philosopher3("Philosopher3", f3, f4);
        Philosopher3 p4 = new Philosopher3("Philosopher4", f4, f5);
        Philosopher3 p5 = new Philosopher3("Philosopher5", f5, f1);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
    }
}

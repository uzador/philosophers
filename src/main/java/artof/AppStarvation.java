package artof;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 */
class Fork2 {
    private final String name;

    public final ReentrantLock lock = new ReentrantLock();

    public Fork2(String name) {
        this.name = name;
    }
}

class Philosopher2 extends Thread {

    private final String name;
    private final Fork2 left;
    private final Fork2 right;
    private int eat;

    Philosopher2(String name, final Fork2 left, final Fork2 right) {
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
            if (left.lock.tryLock()) {
                try {
                    if (right.lock.tryLock()) {
                        try {
                            System.out.println(name + " eat " + ++eat);
                        } finally {
                            right.lock.unlock();
                        }
                    }
                } finally {
                    left.lock.unlock();
                }
            }
        }
    }
}

public class AppStarvation {

    public static void main(String[] args) {
        final Fork2 f1 = new Fork2("Fork1");
        final Fork2 f2 = new Fork2("Fork2");
        final Fork2 f3 = new Fork2("Fork3");
        final Fork2 f4 = new Fork2("Fork4");
        final Fork2 f5 = new Fork2("Fork5");

        Philosopher2 p1 = new Philosopher2("Philosopher1", f1, f2);
        Philosopher2 p2 = new Philosopher2("Philosopher2", f2, f3);
        Philosopher2 p3 = new Philosopher2("Philosopher3", f3, f4);
        Philosopher2 p4 = new Philosopher2("Philosopher4", f4, f5);
        Philosopher2 p5 = new Philosopher2("Philosopher5", f5, f1);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
    }
}

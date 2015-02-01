package artof;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 */
class Fork1 {
    private final String name;

    private final ReentrantLock lock = new ReentrantLock();

    public Fork1(String name) {
        this.name = name;
    }

    public void take() {
        lock.lock();
    }

    public void put() {
        lock.unlock();
    }
}

class Philosopher1 extends Thread {

    private final String name;
    private final Fork1 left;
    private final Fork1 right;
    private int eat;

    Philosopher1(String name, final Fork1 left, final Fork1 right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("   " + name + " is thinking");
            left.take();
            right.take();
            System.out.println(name + " eat " + ++eat);
            left.put();
            right.put();
        }
    }
}

public class AppLockDeadLock {

    public static void main(String[] args) {
        final Fork1 f1 = new Fork1("Fork1");
        final Fork1 f2 = new Fork1("Fork2");
        final Fork1 f3 = new Fork1("Fork3");
        final Fork1 f4 = new Fork1("Fork4");
        final Fork1 f5 = new Fork1("Fork5");

        Philosopher1 p1 = new Philosopher1("Philosopher1", f1, f2);
        Philosopher1 p2 = new Philosopher1("Philosopher2", f2, f3);
        Philosopher1 p3 = new Philosopher1("Philosopher3", f3, f4);
        Philosopher1 p4 = new Philosopher1("Philosopher4", f4, f5);
        Philosopher1 p5 = new Philosopher1("Philosopher5", f5, f1);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
    }
}

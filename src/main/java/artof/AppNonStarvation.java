package artof;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 */
class Fork4 {
    private final String name;

    private final ReentrantLock lock = new ReentrantLock();

    public Fork4(String name) {
        this.name = name;
    }

    public boolean get() {
        return lock.tryLock();
    }

    public void put() {
        lock.unlock();
    }
}

class Philosopher4 extends Thread {

    private final String name;
    private final Fork4 left;
    private final Fork4 right;
    private int eat;
    private CyclicBarrier wait;

    Philosopher4(CyclicBarrier barrier, String name, final Fork4 left, final Fork4 right) {
        this.name = name;
        this.left = left;
        this.right = right;
        this.wait = barrier;
    }

    @Override
    public void run() {
        boolean eaten;
        while (true) {
            eaten = false;
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
                            eaten = true;
                            System.out.println(name + " eat " + ++eat);
                        } finally {
                            right.put();
                        }
                    }
                } finally {
                    left.put();
                }
            }

            if (eaten) {
                try {
                    wait.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class AppNonStarvation {

    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(5, () -> System.out.println("Philisophers've eaten"));

        final Fork4 f1 = new Fork4("Fork1");
        final Fork4 f2 = new Fork4("Fork3");
        final Fork4 f3 = new Fork4("Fork3");
        final Fork4 f4 = new Fork4("Fork4");
        final Fork4 f5 = new Fork4("Fork5");

        Philosopher4 p1 = new Philosopher4(barrier, "Philosopher1", f1, f2);
        Philosopher4 p2 = new Philosopher4(barrier, "Philosopher3", f2, f3);
        Philosopher4 p3 = new Philosopher4(barrier, "Philosopher3", f3, f4);
        Philosopher4 p4 = new Philosopher4(barrier, "Philosopher4", f4, f5);
        Philosopher4 p5 = new Philosopher4(barrier, "Philosopher5", f5, f1);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
    }
}

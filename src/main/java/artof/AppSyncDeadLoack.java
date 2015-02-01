package artof;

/**
 * Hello world!
 */
class Fork {
    private final String name;

    public Fork(String name) {
        this.name = name;
    }
}

class Philosopher extends Thread {

    private final String name;
    private final Fork left;
    private final Fork right;
    private int eat;

    Philosopher(String name, final Fork left, final Fork right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("   " + name + " is thinking");
            synchronized (left) {
                synchronized (right) {
                    System.out.println(name + " eat " + ++eat);
                }
            }
        }
    }
}

public class AppSyncDeadLoack {

    public static void main(String[] args) {
        final Fork f1 = new Fork("Fork1");
        final Fork f2 = new Fork("Fork2");
        final Fork f3 = new Fork("Fork3");
        final Fork f4 = new Fork("Fork4");
        final Fork f5 = new Fork("Fork5");

        Philosopher p1 = new Philosopher("Philosopher1", f1, f2);
        Philosopher p2 = new Philosopher("Philosopher2", f2, f3);
        Philosopher p3 = new Philosopher("Philosopher3", f3, f4);
        Philosopher p4 = new Philosopher("Philosopher4", f4, f5);
        Philosopher p5 = new Philosopher("Philosopher5", f5, f1);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
    }
}

public class MainDeadlock {
    private int balance = 0;
    private final Object mainDeadlock0 = new Object();
    private final Object mainDeadlock1 = new Object();

    public static void main(String[] args) throws InterruptedException {
        MainDeadlock mainDeadlock = new MainDeadlock();

        Thread thread0 = new Thread(mainDeadlock::deposit);
        Thread thread1 = new Thread(mainDeadlock::withdraw);

        System.out.println(thread0.getName() + " " + thread0.getState());
        System.out.println(thread1.getName() + " " + thread1.getState());
        thread0.start();
        thread1.start();
        Thread.sleep(3000);
        System.out.println(thread0.getName() + " " + thread0.getState());
        System.out.println(thread1.getName() + " " + thread1.getState());
        thread0.join();
        thread1.join();
    }

    public void deposit() {
        synchronized (mainDeadlock0) {
            System.out.println("some code");
            synchronized (mainDeadlock1) {
                System.out.println("some code");
            }
        }
    }

    public void withdraw() {
        synchronized (mainDeadlock1) {
            System.out.println("some code");
            synchronized (mainDeadlock0) {
                System.out.println("some code");
            }
        }
    }
}
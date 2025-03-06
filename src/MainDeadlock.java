public class MainDeadlock {
    private int balance = 0;
    private final Object mainDeadlock0 = new Object();

    public static void main(String[] args) throws InterruptedException {
        MainDeadlock mainDeadlock = new MainDeadlock();

        Thread thread0 = new Thread(mainDeadlock::deposit);
        Thread thread1 = new Thread(mainDeadlock::withdraw);

        System.out.println(thread0.getName() + " " + thread0.getState());
        System.out.println(thread1.getName() + " " + thread1.getState());
        thread0.start();
        thread1.start();
        Thread.sleep(1000);
        System.out.println(thread0.getName() + " " + thread0.getState());
        System.out.println(thread1.getName() + " " + thread1.getState());
        thread0.join();
        thread1.join();
        System.out.println(thread0.getName() + " " + thread0.getState());
        System.out.println(thread1.getName() + " " + thread1.getState());

        System.out.println("BALANCE: " + mainDeadlock.balance);
    }

    public void deposit() {
        synchronized (mainDeadlock0) {
            for (int i = 0; i < 40; i++) {
                balance = balance + 500;
                System.out.println("+500");

                if (balance >= 5000) {
                    mainDeadlock0.notify();
                }

                try {
//                    while (balance >= 5000) {
                        mainDeadlock0.wait();
//                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void withdraw() {
        synchronized (mainDeadlock0) {
            for (int i = 0; i < 4; i++) {
                while (balance < 5000) {
                    try {
                        System.out.println("sleep");
                        mainDeadlock0.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(balance + "-5000");
                balance -= 5000;
                mainDeadlock0.notify();
            }
        }
    }
}
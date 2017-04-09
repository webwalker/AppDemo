/**
 * Created by xujian on 2017/3/14.
 */

public class ThreadTest {
    /**
     * @param args
     */
    public Object lock = new Object();


    public static void main(String[] args) {
        new ThreadTest().go();
    }

    private volatile boolean thread1Finish = false;
    private volatile boolean thread2Finish = false;


    public void go() {
        Thread1 t1 = new Thread1();
        Thread2 t2 = new Thread2();
        t1.start();
        t2.start();
    }


    public class Thread1 extends Thread {
        public void run() {
            while (true) {
                synchronized (lock) {
                    try {
                        System.out.println("Thread1正在运行");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public class Thread2 extends Thread {
        public void run() {
            while (true) {
                synchronized (lock) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread2在运行");
                    try {
                        lock.wait();
                        lock.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}

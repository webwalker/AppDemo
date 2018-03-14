import com.webwalker.java.threads.CallableFuture;
import com.webwalker.java.threads.ITester;
import com.webwalker.java.threads.ThreadJoin;

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
        ITester tester = new ThreadJoin();
        tester.Test();
        /*
        Thread1 t1 = new Thread1();
        Thread2 t2 = new Thread2();
        t1.start();
        t2.start();*/
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
                        System.out.println("Thread1正在wait");
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
                        System.out.println("Thread2正在wait");
                        lock.notify();
                        System.out.println("Thread2正在notify");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}

/**
 * 
 */
package com.webwalker.java.threads;

/**
 * @author Administrator
 * 
 */
public class SleepLock implements ITester {

	/**
	 * t1��t2������������̣߳����Ե��߳�t1ͨ��sleep()����ͣ��ʱ�� �ų�������̳߳��е��������Ŀ�ִ���̣߳� �Ӷ�t2�̱߳�����
	 */
	@Override
	public void Test() {
//		TestThreadMethod1 t1 = new TestThreadMethod1("t1");
//		TestThreadMethod1 t2 = new TestThreadMethod1("t2");
		
		TestThreadMethod2 t1 = new TestThreadMethod2("t1");
		TestThreadMethod2 t2 = new TestThreadMethod2("t2");
		
		t1.start();// ��1��
		// t1.start();//��2��
		t2.start();// ��3��
	}

	class TestThreadMethod1 extends Thread {
		public int shareVar = 0;

		public TestThreadMethod1(String name) {
			super(name);
		}

		/**
		 * ��Ȼ��run()��ִ����sleep()�������������ͷŶ����"����־"��
		 * ���Գ��Ǵ���(1)���߳�ִ����run()�������ͷŶ����"����־" �� ������루2�����߳���Զ����ִ��
		 */
		public synchronized void run() {
			for (int i = 0; i < 3; i++) {
				System.out.print(Thread.currentThread().getName());
				System.out.println(" extends " + i);
				try {
					Thread.sleep(100);// ��4��
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
			}
		}
	}

	class TestThreadMethod2 extends Thread {
		public int shareVar = 0;

		public TestThreadMethod2(String name) {
			super(name);
		}

		public synchronized void run() {
			for (int i = 0; i < 5; i++) {
				System.out.print(Thread.currentThread().getName());
				System.out.println(" extends " + i);
				try {
					if (Thread.currentThread().getName().equals("t1"))
						Thread.sleep(200);
					else
						Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
			}
		}
	}
}

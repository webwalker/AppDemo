/**
 * 
 */
package com.webwalker.java.threads;

/**
 * @author Administrator �̰߳�ȫ �������
 */
public class ThreadShareVarliable implements ITester {

	@Override
	public void Test() {
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);

		// t1.start();
		// t2.start();
	}

	Runnable r = new Runnable() {

		// i������ʹ��
		int i = 100;

		@Override
		public void run() {
			while (true) {
				i--;
				System.out.println(i);

				try {
					Thread.sleep(1000);
				} catch (Exception e) {

				}
			}
		}
	};
}

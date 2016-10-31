/**
 * 
 */
package com.webwalker.java.threads;

/**
 * @author Administrator �߳�2�ȴ��߳�1�������ӡ���,һ�������ϣ�������ʵ��ͬ���Ĺ���
 */
public class ThreadJoin implements ITester {

	int sum = 0;

	@Override
	public void Test() {
		Thread t = new Thread(r);
		t.start();

		System.out.println("��������");
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sum);
	}

	Runnable r = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				sum += i;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
}

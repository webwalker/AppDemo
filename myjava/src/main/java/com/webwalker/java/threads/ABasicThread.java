package com.webwalker.java.threads;

/**
 * һ����ͳ���߳���
 */
public class ABasicThread implements ITester {
	@Override
	public void Test() {
		/** ��һ��ʵ�ַ�ʽ, ��д run ���� */
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1 " + Thread.currentThread().getName());
				}
			}
		};
		thread.start();

		/** �ڶ��ַ�ʽ,ʵ��Runnable �ӿ� */
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("2 " + Thread.currentThread().getName());
				}
			}
		});
		thread2.start();

		/**
		 * �����ַ�ʽ,(){}����д����ʾ������һ������,��ʱ()�����ǿյ�,����11�� Thread(new Runnable())
		 * ���ʾ�ڹ��췽���ж�����һ���� ��˴���ִ��˳�����ߴ�ӡ thread���У��ٵ����췽�������� ��ӡrunnable
		 * ����Runnable �Ǹ��ӿڣ���������run����������ֻ�������࣬��Զ������ ��ӡrunnable
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("runnable "
							+ Thread.currentThread().getName());
				}
			}
		}) {
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread "
							+ Thread.currentThread().getName());
				}
			}
		}.start();
	}
}
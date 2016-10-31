/**
 * 
 */
package com.webwalker.java.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * 
 */
public class ThreadPoolExecutors implements ITester {
	class MyThread extends Thread {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + "����ִ�С�����");
		}
	}

	/**
	 * ExecutorService�Ĺ���û�������е���ô�ã��������ֻ���ṩһ���̵߳��������ѣ� ��ʹ��ThreadGroup���
	 */
	@Override
	public void Test() {
		 SingleThreadPool();
		// FixedThreadPool();
		// CachedThreadPool();
//		ScheduedThreadPool();
//		ScheduedFixedRateThreadPool();
	}

	private void SingleThreadPool() {
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		threadPool.execute(new MyThread());
	}

	private void FixedThreadPool() {
		// ����һ�������ù̶���С���̳߳�
		ExecutorService pool = Executors.newFixedThreadPool(2);
		// ����ʵ����Runnable�ӿڶ���Thread����ȻҲʵ����Runnable�ӿ�
		Thread t1 = new MyThread();
		Thread t2 = new MyThread();
		Thread t3 = new MyThread();
		Thread t4 = new MyThread();
		Thread t5 = new MyThread();
		// ���̷߳�����н���ִ��
		pool.execute(t1);
		pool.execute(t2);
		pool.execute(t3);
		pool.execute(t4);
		pool.execute(t5);
		// �ر��̳߳�
		pool.shutdown();
	}

	private void CachedThreadPool() {
		// ���������̳߳�, ������̳߳ؾ���˵���������ˣ��������в�������,
		// ���Զ������µ��߳� ������̸߳����ǲ�����
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			// ���������10������
			final int task = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 1; j <= 10; j++) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.print("�߳�: "
								+ Thread.currentThread().getName() + "  ��Ե� "
								+ task + " ������" + " ��   " + j + " ��ѭ��\n");
					}
				}
			});
		}
		System.out.println("all of 10 tasks  �Ѿ����!");
		threadPool.shutdown();// �����������ˣ����̳߳ظɵ�
	}

	private void ScheduedThreadPool() {
		Executors.newScheduledThreadPool(3).schedule(new Runnable() {
			@Override
			public void run() {
				System.out.println("Schedued bombing...");
			}
		}, 5, TimeUnit.SECONDS);

		// ExecutorService servcie =
		// ScheduledExecutorService servcie =
		// Executors.newScheduledThreadPool(3);
	}

	private void ScheduedFixedRateThreadPool() {
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println("FixedRate bombing...");
			}
		}, 5, 2, TimeUnit.SECONDS);

	}
}

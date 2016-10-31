package com.webwalker.java.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * �̷߳�Χ�ڹ������ ʾ������
 * 
 */
public class ThreadShareData implements ITester {

	private static int data = 0;
	private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();

	@Override
	public void Test() {
		// ѭ��2�Σ���ʾ���� 2 ���߳�
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName()
							+ " has put data :" + data);
					threadData.put(Thread.currentThread(), data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}

	static class A {
		public void get() {
			// ��Map��ȡ����
			int data = threadData.get(Thread.currentThread());
			System.out.println("A from " + Thread.currentThread().getName()
					+ " get data :" + data);
		}
	}

	static class B {
		public void get() {
			int data = threadData.get(Thread.currentThread());
			System.out.println("B from " + Thread.currentThread().getName()
					+ " get data :" + data);
		}
	}
}

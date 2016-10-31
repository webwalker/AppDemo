/**
 * 
 */
package com.webwalker.java.threads;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Administrator ��������һ���̣߳�����֮���һ�����ؽ��
 * 
 */
public class CallableFuture implements ITester {
	@Override
	public void Test() {
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		// Future ��ʾ�����
		Future<String> future = threadPool.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Thread.sleep(2000);
				return "hello";
			}
		});
		System.out.println("future isDone:" + future.isDone());
		System.out.println("�ȴ����");
		try {
			System.out.println("�õ������" + future.get());
			System.out.println("future isDone:" + future.isDone());
			// future.cancel(false);
			// System.out.println("future isCancelled��" + future.isCancelled());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// �ύһ������
		ExecutorService threadPool2 = Executors.newFixedThreadPool(10);
		CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(
				threadPool2);
		for (int i = 1; i <= 10; i++) {
			final int seq = i;
			completionService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					// ������5s, ���5s
					Thread.sleep(new Random().nextInt(5000));
					return seq;
				}
			});
		}
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println("completionService get "
						+ completionService.take().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

	}
}

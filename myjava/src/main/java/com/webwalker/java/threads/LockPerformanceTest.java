/**
 * 
 */
package com.webwalker.java.threads;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 * 
 */
public class LockPerformanceTest implements ITester {

	@Override
	public void Test() {
		for (int i = 0; i < 5; i++) {
			int round = 100000 * (i + 1);
			int threadNum = 5 * (i + 1);
			
			CyclicBarrier cb = new CyclicBarrier(threadNum * 2 + 1);
			System.out.println("==========================");
			System.out.println("round:" + round + " thread:" + threadNum);
			test(round, threadNum, cb);
		}
	}

	public void test(int round, int threadNum, CyclicBarrier cyclicBarrier) {
		new SyncTest("Sync", round, threadNum, cyclicBarrier).testTime();
		new LockTest("Lock", round, threadNum, cyclicBarrier).testTime();
		new AtomicTest("Atom", round, threadNum, cyclicBarrier).testTime();
	}

	class SyncTest extends TestTemplate {
		public SyncTest(String _id, int _round, int _threadNum,
				CyclicBarrier _cb) {
			super(_id, _round, _threadNum, _cb);
		}

		@Override
		/**    
		 * synchronized�ؼ��ֲ��ڷ���ǩ�����棬���Բ��漰��������    
		 */
		synchronized long getValue() {
			return super.countValue;
		}

		@Override
		synchronized void sumValue() {
			super.countValue += preInit[index++ % round];
		}
	}

	class LockTest extends TestTemplate {
		ReentrantLock lock = new ReentrantLock();

		public LockTest(String _id, int _round, int _threadNum,
				CyclicBarrier _cb) {
			super(_id, _round, _threadNum, _cb);
		}

		/**
		 * synchronized�ؼ��ֲ��ڷ���ǩ�����棬���Բ��漰��������
		 */
		@Override
		long getValue() {
			try {
				lock.lock();
				return super.countValue;
			} finally {
				lock.unlock();
			}
		}

		@Override
		void sumValue() {
			try {
				lock.lock();
				super.countValue += preInit[index++ % round];
			} finally {
				lock.unlock();
			}
		}
	}

	class AtomicTest extends TestTemplate {
		public AtomicTest(String _id, int _round, int _threadNum,
				CyclicBarrier _cb) {
			super(_id, _round, _threadNum, _cb);
		}

		@Override
		/**    
		 * synchronized�ؼ��ֲ��ڷ���ǩ�����棬���Բ��漰��������    
		 */
		long getValue() {
			return super.countValueAtmoic.get();
		}

		@Override
		void sumValue() {
			super.countValueAtmoic.addAndGet(super.preInit[indexAtomic.get()
					% round]);
		}
	}

	abstract class TestTemplate {
		private String id;
		protected int round;
		private int threadNum;
		protected long countValue;
		protected int[] preInit;
		protected int index;
		
		protected AtomicInteger indexAtomic = new AtomicInteger(0);
		protected AtomicLong countValueAtmoic = new AtomicLong(0);
		Random r = new Random(47);
		
		// ����դ����ͬ�������ȵ���wait���������һֱ�ȵ�ȫ�����񵽴��ƶ���wait�ص�󣬲���ȫ�����ѣ�����ִ��
		private CyclicBarrier cb;

		public TestTemplate(String _id, int _round, int _threadNum,
				CyclicBarrier _cb) {
			this.id = _id;
			this.round = _round;
			this.threadNum = _threadNum;
			cb = _cb;
			preInit = new int[round];
			for (int i = 0; i < preInit.length; i++) {
				preInit[i] = r.nextInt(100);
			}
		}

		abstract void sumValue();

		/*
		 * ��long�Ĳ����Ƿ�ԭ�ӵģ�ԭ�Ӳ���ֻ���32λ long��64λ���ײ������ʱ���2��32λ��д����˲����̰߳�ȫ
		 */
		abstract long getValue();

		public void testTime() {
			ExecutorService se = Executors.newCachedThreadPool();
			long start = System.nanoTime();
			// ͬʱ����2*ThreadNum�����Ķ�д�߳�
			for (int i = 0; i < threadNum; i++) {
				se.execute(new Runnable() {
					public void run() {
						for (int i = 0; i < round; i++) {
							sumValue();
						}

						// ÿ���߳�ִ����ͬ��������͵ȴ�
						try {
							cb.await();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				se.execute(new Runnable() {
					public void run() {

						getValue();
						try {
							// ÿ���߳�ִ����ͬ��������͵ȴ�
							cb.await();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}

			try {
				// ��ǰͳ���߳�Ҳwait,����CyclicBarrier�ĳ�ʼֵ��threadNum*2+1
				cb.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// �����߳�ִ�����֮�󣬲Ż��ܵ���һ��
			long duration = System.nanoTime() - start;
			System.out.println(id + " = " + duration);
		}
	}

}

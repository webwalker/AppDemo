/**
 * 
 */
package com.webwalker.java.threads;

/**
 * @author Administrator ������������ ��ͬ��ǰ�� �������ѷǳ��ң� ���Խ��� ����������, �߳�ͬ��Ҳ��������⣺
 *         1�������߶����������� 2�������߶�ȡ������
 */
public class ProductorConsumer implements ITester {

	/**
	 * ������-�����ߣ�producer-consumer�����⣬Ҳ�����н绺������bounded-buffer�����⣬
	 * �������̹���һ�������Ĺ̶���С�Ļ�����
	 * ������һ���������ߣ����ڽ���Ϣ���뻺����������һ���������ߣ����ڴӻ�������ȡ����Ϣ����������ڵ��������Ѿ�����
	 * ������ʱ�����߻��������з���һ���µ������������
	 * �������������������ߴ�ʱ�������ߣ��ȴ������ߴӻ�������ȡ����һ�����߶�����ݺ���ȥ��������ͬ���أ�
	 * ���������Ѿ����ˣ��������߻���ȥȡ��Ϣ����ʱҲ�����������߽������ߣ��ȴ������߷���һ�����߶������ʱ�ٻ�������
	 * һ�����ȶ��幫����Դ�࣬���еı���number�Ǳ���Ĺ������ݡ����Ҷ�����������������number��ֵ�ͼ���number��ֵ
	 */
	@Override
	public void Test() {
		PublicResource resource = new PublicResource();

		for (int i = 0; i < 3; i++) {
			new Thread(new ProducerThread(resource)).start();
			new Thread(new ConsumerThread(resource)).start();
		}
	}

	/**
	 * �������̣߳���������������Դ
	 */
	public class ProducerThread implements Runnable {
		private PublicResource resource;

		public ProducerThread(PublicResource resource) {
			this.resource = resource;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep((long) (Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				resource.increace();
			}
		}
	}

	/**
	 * �������̣߳��������ѹ�����Դ
	 */
	public class ConsumerThread implements Runnable {
		private PublicResource resource;

		public ConsumerThread(PublicResource resource) {
			this.resource = resource;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep((long) (Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				resource.decreace();
			}
		}
	}

	/**
	 * ������Դ�� ��non-synchronized������non-synchronized��block�н��е��ã�
	 * ��Ȼ�ܱ���ͨ������������ʱ�ᷢ��IllegalMonitorStateException���쳣
	 */
	public class PublicResource {
		private int number = 0;

		/**
		 * ���ӹ�����Դ
		 */
		public synchronized void increace() {
			while (number != 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			number++;
			System.out.println(number);
			notify();
		}

		/**
		 * ���ٹ�����Դ
		 */
		public synchronized void decreace() {
			while (number == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			number--;
			System.out.println(number);
			notify();
		}
	}
}

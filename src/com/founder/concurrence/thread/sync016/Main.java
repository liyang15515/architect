package com.founder.concurrence.thread.sync016;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
/**@Author yanglee
* @Description: TODO <p>多线程设计模式之生产者-消费者:<b>
	生产者-消费者也是一个非常经典的多线程模式，我们在实际开发中应用非常广泛的的思想理念。
	在生产者-消费者模式中：通常有两类线程，即若干个生产者的线程和若干个消费者的线程。
	生产者线程负责提交用户请求，
	消费者线程负责具体处理生产者提交的任务，
	在生产者和消费者之间通过共享内存缓存区进行通信。
</b>
</p>
* @Param
* @Return
* @Date 2019-04-22 10:58
*/
public class Main {

	public static void main(String[] args) throws Exception {
		//内存缓冲区
		BlockingQueue<Data> queue = new LinkedBlockingQueue<Data>(10);
		//生产者
		Provider p1 = new Provider(queue);
		
		Provider p2 = new Provider(queue);
		Provider p3 = new Provider(queue);
		//消费者
		Consumer c1 = new Consumer(queue);
		Consumer c2 = new Consumer(queue);
		Consumer c3 = new Consumer(queue);
		//创建线程池运行,这是一个缓存的线程池，可以创建无穷大的线程，没有任务的时候不创建线程。空闲线程存活时间为60s（默认值）

		ExecutorService cachePool = Executors.newCachedThreadPool();
		cachePool.execute(p1);
		cachePool.execute(p2);
		cachePool.execute(p3);
		cachePool.execute(c1);
		cachePool.execute(c2);
		cachePool.execute(c3);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		p1.stop();
		p2.stop();
		p3.stop();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
//		cachePool.shutdown();
//		cachePool.shutdownNow();
		

	}
	
}

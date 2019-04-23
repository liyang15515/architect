package com.founder.concurrence.thread.threadpool.sync017;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UseExecutors {

	public static void main(String[] args) {
		/**@Author yanglee
		* @Description: TODO 4个线程池底层核心，都是通过 new ThreadPoolExecutor 来实例化线程池。
		* @Param [args]
		* @Return void
		* @Date 2019-04-22 22:39
		*/
		ExecutorService pool = Executors.newSingleThreadExecutor();
		ExecutorService pool2 = Executors.newFixedThreadPool(10);

		ExecutorService pool3 = Executors.newCachedThreadPool();
		ExecutorService pool4 = Executors.newScheduledThreadPool(10);
	}
}

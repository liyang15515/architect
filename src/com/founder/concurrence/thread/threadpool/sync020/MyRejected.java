package com.founder.concurrence.thread.threadpool.sync020;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejected implements RejectedExecutionHandler{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		System.out.println("自定义处理");
		
		System.out.println("自定义处理"+r.toString());
		System.out.println("自定义处理"+executor.getQueue().size());
		
	}

}

package com.founder.concurrence.thread.sync006;

/**
 * 使用synchronized代码块加锁,比较灵活
 * @author 6002
 *
 */
public class ObjectLock {

	public void method1(){
		synchronized (this) {	//对象锁
			try {
				Thread.sleep(2000);
				System.out.println(Thread.currentThread().getName()+":do method1.."+System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void method2(){		//类锁
		synchronized (ObjectLock.class) {
			try {
				Thread.sleep(2000);
				System.out.println(Thread.currentThread().getName()+":do method2.."+System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Object lock = new Object();
	public void method3(){		//任何对象锁
		synchronized (lock) {
			try {
				Thread.sleep(2000);
				System.out.println(Thread.currentThread().getName()+":do method3.."+System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {

		final ObjectLock objLock = new ObjectLock();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				objLock.method1();
			}
		},"t1");
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				objLock.method2();
			}
		},"t2");
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				objLock.method3();
			}
		},"t3");
		System.out.println("startTime:"+System.currentTimeMillis());
		t1.start();
		t2.start();
		t3.start();
		
		
	}
	
}

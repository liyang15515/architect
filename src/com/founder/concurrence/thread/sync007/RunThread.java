package com.founder.concurrence.thread.sync007;
/**@Author 7-001
* @Description: TODO volatile相当于一个轻量级的synchronized锁，但无法代替synchronized；
 * 				TODO volatile不用把对数据往自己的线程空间（栈桢）copy，节约了复制时间，效率一级棒
* @Param
* @Return
* @Date 2019-04-19 16:45
*/
public class RunThread extends Thread{

	private volatile boolean isRunning = true;
	private void setRunning(boolean isRunning){
		this.isRunning = isRunning;
	}
	
	public void run(){
		System.out.println("进入run方法..");
		int i = 0;
		while(isRunning == true){
			//..
		}
		System.out.println("线程停止");
	}
	
	public static void main(String[] args) throws InterruptedException {
		RunThread rt = new RunThread();
		rt.start();
		Thread.sleep(1000);
		rt.setRunning(false);
		System.out.println("isRunning的值已经被设置了false");
	}
	
	
}

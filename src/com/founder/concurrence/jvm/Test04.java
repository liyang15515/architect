package com.founder.concurrence.jvm;

public class Test04 {

	//-Xss1m  	1M的情况下大概1万2千次
	//-Xss5m
	
	//栈调用深度
	private static int count;
	
	public static void recursion(){
		count++;
		recursion();
	}
	public static void main(String[] args){
		try {
			recursion();
		} catch (Throwable t) {
			System.out.println("调用最大深入：" + count);
			t.printStackTrace();
		}
	}
}

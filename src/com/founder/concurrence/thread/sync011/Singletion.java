package com.founder.concurrence.thread.sync011;
/**@Author yanglee
* @Description: TODO 单例模式
* @Param
* @Return
* @Date 2019-04-19 21:31
*/
public class Singletion {
	
	private static class InnerSingletion {
		private static Singletion single = new Singletion();
	}
	
	public static Singletion getInstance(){
		return InnerSingletion.single;
	}
	
}

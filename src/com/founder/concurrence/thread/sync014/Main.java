package com.founder.concurrence.thread.sync014;
/**@Author yanglee
* @Description: TODO <p>多线程设计模式之Future模式：
<b>
Future模式有点类似于商品订单。比如在网购时，当看重某一件商品时，就可以提交订单，当订单处理完成后，在家里等待商品送货上门即可。
或者说更形象的我们发送ajax请求的时候，页面是异步的进行后台处理，用户无须一直等待请求的结果，可以继续浏览或操作其他内容。
</b>
<b>
Future模式会异步创建一个子线程，去完成相关请求任务，然后将处理结果返回给主线程main。
在子线程请求并处理数据的过程中，主线程可以继续做别的事情，即异步加载数据。
</b>
<b>
Future模式非常适合在处理耗时很长的业务逻辑时进行使用，可以有效减少系统的响应时间，提高系统的吞吐量。
</b>
</p>
* @Param
* @Return
* @Date 2019-04-22 10:46
*/
public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		FutureClient fc = new FutureClient();
		Data data = fc.request("请求参数");
		System.out.println("请求发送成功!");
		System.out.println("做其他的事情...");
		
		String result = data.getRequest();
		System.out.println(result);
		
	}
}

package com.founder.concurrence.socketIO.IO01.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	final static int PROT = 8765;
	
	public static void main(String[] args) {
		
		ServerSocket server = null;
		try {
			server = new ServerSocket(PROT);
			System.out.println("阻塞前， server start .. ");
			//进行阻塞
			Socket socket = server.accept();
			//新建一个线程执行客户端的任务
			System.out.println("阻塞通了后，纳尼，从监听端口的请求队列取出socket链接");
			new Thread(new ServerHandler(socket)).start();
			//传统的TCP点对点直连接的方式，每一个连接在server端都要创建一个线程；windows最大支持1000个线程，unix最大支持2000个线程；
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(server != null){
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			server = null;
		}
		
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

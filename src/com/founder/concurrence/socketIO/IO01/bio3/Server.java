 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.founder.concurrence.socketIO.IO01.bio3;

 import java.io.IOException;
 import java.net.ServerSocket;
 import java.net.Socket;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/11/27 9:38 下午
  * @Version
  **/
 public class Server {
  private int port = 8000;
  private ServerSocket serverSocket;

  public Server() throws IOException {
   serverSocket = new ServerSocket(port);
   System.out.println("server 启动了");
  }

  public static void main(String[] args)throws Exception {
   Server server = new Server();
   Thread.sleep(600000);
//   server.hanlder();
  }
  public void hanlder(){
   int i = 1;
   while(true){
    Socket socket = null;
    try {
     socket = serverSocket.accept();
     System.out.println("新连接创建"+(i++)+",地址是"+socket.getInetAddress()+",端口是："+socket.getPort());
    } catch (IOException e) {
     e.printStackTrace();
    }finally {
     if(socket != null){
      try {
       socket.close();
      } catch (IOException e) {
       e.printStackTrace();
      }
     }
    }
   }
  }
 }

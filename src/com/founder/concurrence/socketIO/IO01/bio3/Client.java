 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.founder.concurrence.socketIO.IO01.bio3;

 import java.net.Socket;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/11/27 9:32 下午
  * @Version
  **/
 public class Client {

     public static void main(String args[]) throws Exception {
         final int length = 100;
         String host = "localhost";
         int port = 8000;

         Socket[] sockets = new Socket[length];
         System.out.println("试图建立100次连接");
         for (int i = 0; i < length; i++) {
          sockets[i] = new Socket(host, port);
             System.out.println("第" + (i + 1) + "次连接成功");
         }
         Thread.sleep(3000);
         for (int i = 0; i < length; i++) {
             sockets[i].close();
         }
     }
 }

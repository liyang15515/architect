 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.bio.bioserver2;

 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/12/8 11:47 上午
  * @Version
  **/
 public class ServerThreadpool {
     public static void main(String[] args) {
         ExecutorService executor = Executors.newFixedThreadPool(3);
         RequestHandler requestHandler = new RequestHandler();
         try (ServerSocket serverSocket = new ServerSocket(8080)) {
             System.out.println("BIOServer has started , listening on port:" + serverSocket.getLocalSocketAddress());
             while (true) {
                 Socket clientSocket = serverSocket.accept();
                 System.out.println("connection from " + clientSocket.getRemoteSocketAddress());
                 executor.submit(new ClientHandler(clientSocket, requestHandler));
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }

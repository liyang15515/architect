 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.bio;

 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.Scanner;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/12/8 11:10 上午
  * @Version
  **/
 public class BIOServer {
     public static void main(String[] args) {
         try (ServerSocket serverSocket = new ServerSocket(8080)) {
             System.out.println("BIOServer has started , listening on port:" + serverSocket.getLocalSocketAddress());
             while (true) {
                 Socket clientSocket = serverSocket.accept();
                 System.out.println("connection from " + clientSocket.getRemoteSocketAddress());
                 try (Scanner  input = new Scanner(clientSocket.getInputStream())) {
                  while(true){
                   String request = input.nextLine();
                   if("quit".equals(request)){
                    break;
                   }
                   System.out.println(String.format("from %s : %s",clientSocket.getRemoteSocketAddress(),request));
                   String response = "from BIOServer Hello "+request+".\n";
                   clientSocket.getOutputStream().write(response.getBytes());
                  }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }

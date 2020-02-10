 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.bio.bioserver2;



 import java.net.Socket;
 import java.util.Scanner;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/12/8 11:56 上午
  * @Version
  **/
 public class ClientHandler implements Runnable{
     private final Socket clientSocket;
     private final RequestHandler requestHandler;

     public ClientHandler(Socket clientSocket, RequestHandler requestHandler) {
         this.clientSocket = clientSocket;
         this.requestHandler = requestHandler;
     }

     @Override
     public void run() {
         try (Scanner input = new Scanner(clientSocket.getInputStream())) {
             while(true){
                 String request = input.nextLine();//阻塞
                 //think:阻塞合理吗？如果没有客户端数据进来，该阻塞吗？
                 //假如说线程不是一直等待在浏览器的输入，而是有输入再进行用线程处理
                 if("quit".equals(request)){
                     break;
                 }
                 System.out.println(String.format("from %s : %s",clientSocket.getRemoteSocketAddress(),request));
                 String response = "from BIOServer Hello "+request+".\n";
                 clientSocket.getOutputStream().write(response.getBytes());
             }
         }catch(Exception e){
             e.printStackTrace();
         }
     }
 }

 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.founder.concurrence.socketIO.IO01.nio2;

 import java.io.IOException;
 import java.nio.ByteBuffer;
 import java.nio.channels.Pipe;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/12/5 4:21 下午
  * @Version
  **/
 public class Server {
     public static void main(String[] args) {
         try {
             Pipe pipe = Pipe.open();
             Pipe.SinkChannel sinkChannel = pipe.sink();
             String newDate = "New String to write to file..." + System.currentTimeMillis();
          ByteBuffer byteBuffer = ByteBuffer.allocate(48);
          byteBuffer.clear();
          byteBuffer.put(newDate.getBytes());
          byteBuffer.flip();
          while (byteBuffer.hasRemaining()){
           int i = sinkChannel.write(byteBuffer);
           System.out.println("写入："+i+"数据");
          }
          Pipe.SourceChannel sourceChannel = pipe.source();
          ByteBuffer buf = ByteBuffer.allocate(48);
          int bytesRead = sourceChannel.read(buf);
          System.out.println(bytesRead);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }

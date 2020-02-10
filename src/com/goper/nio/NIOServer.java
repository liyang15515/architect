 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.nio;

 import com.goper.bio.bioserver2.RequestHandler;

 import java.io.IOException;
 import java.net.InetSocketAddress;
 import java.nio.ByteBuffer;
 import java.nio.channels.SelectionKey;
 import java.nio.channels.Selector;
 import java.nio.channels.ServerSocketChannel;
 import java.nio.channels.SocketChannel;
 import java.util.Iterator;
 import java.util.Set;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/12/8 5:06 下午
  * @Version
  **/
 public class NIOServer {
     public static void main(String[] args) throws IOException {
         ServerSocketChannel serverChannel = ServerSocketChannel.open();
         serverChannel.configureBlocking(false);//为了兼容bio的方式
         serverChannel.bind(new InetSocketAddress(8080));
         System.out.println("NIOServer has started,listening on port :" + serverChannel.getLocalAddress());
         Selector selector = Selector.open();
         serverChannel.register(selector, SelectionKey.OP_ACCEPT);
         ByteBuffer buffer = ByteBuffer.allocate(1024);
         RequestHandler requestHandler = new RequestHandler();
         while (true) {
             //唯一会阻塞的地方，所有的等待都会汇集在这里
             int select = selector.select();
             if (select == 0) {
                 continue;
             }
             //如果selector中有channel的话，就继续往下走
             Set<SelectionKey> selectionKeys = selector.selectedKeys();
             Iterator<SelectionKey> iterator = selectionKeys.iterator();
             while (iterator.hasNext()) {
                 SelectionKey key = iterator.next();
                 if (key.isAcceptable()) {
                     ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                     SocketChannel cilentChannel = channel.accept();
                     System.out.println("connection from " + cilentChannel.getRemoteAddress());
                     cilentChannel.configureBlocking(false);
                     cilentChannel.register(selector, SelectionKey.OP_READ);
                 }
                 if (key.isReadable()) {
                     SocketChannel channel = (SocketChannel) key.channel();
                     channel.read(buffer);
                     String request = new String(buffer.array()).trim();
                     buffer.clear();
                     System.out.println(String.format("From %s : %s", channel.getRemoteAddress(), request));
                     String response = requestHandler.handle(request);
                     channel.write(ByteBuffer.wrap(response.getBytes()));
                 }
                 if (key.isWritable()) {
                     //几乎用不到，除非客户端
                 }
                 iterator.remove();
             }
         }
     }
 }

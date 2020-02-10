 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.consumer.proxy;

 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.ChannelInboundHandlerAdapter;
 import io.netty.channel.SimpleChannelInboundHandler;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/9 3:00 上午
  * @Version
  **/
 public class RpcProxyHandler extends ChannelInboundHandlerAdapter {
     private Object response;

     @Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
         response = msg;
     }

     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         cause.printStackTrace();
      System.out.println("client is exception");
     }
     public Object getResponse(){
      return response;
     }
 }

 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.client.handler;

 import com.goper.chat.protocol.IMMessage;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.SimpleChannelInboundHandler;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/11 12:26 上午
  * @Version
  **/
 public class ChatClientHandler extends SimpleChannelInboundHandler<IMMessage> {
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
   super.exceptionCaught(ctx, cause);
  }

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, IMMessage msg) throws Exception {

  }
 }

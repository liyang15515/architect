 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.server.handler;

 import com.goper.chat.processor.MsgProsesser;
 import com.goper.chat.protocol.IMMessage;
 import io.netty.channel.Channel;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.SimpleChannelInboundHandler;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/10 11:00 下午
  * @Version
  **/
 public class TerminalServerHandler extends SimpleChannelInboundHandler<IMMessage> {
  private MsgProsesser prosesser = new MsgProsesser();

  @Override
     protected void messageReceived(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
//      prosesser.sendMsg(ctx.channel(), msg);
      prosesser.process(ctx.channel(), msg);
  }

     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         Channel client = ctx.channel();
         System.out.println("Client:"+client.remoteAddress()+"异常");
         // 当出现异常就关闭连接
         cause.printStackTrace();
         ctx.close();
     }
 }

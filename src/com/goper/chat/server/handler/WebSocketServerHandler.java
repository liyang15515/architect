 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.server.handler;

 import com.goper.chat.processor.MsgProsesser;
 import io.netty.channel.Channel;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.SimpleChannelInboundHandler;
 import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/10 11:00 下午
  * @Version
  **/
 public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
     //  @Override
//  public void channelRead(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
//   super.channelRead(ctx, msg);
//  }
     private MsgProsesser prosesser = new MsgProsesser();

     @Override
     protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
         //处理业务逻辑
//      prosesser.sendMsg(ctx.channel(),msg.text());
      prosesser.process(ctx.channel(),msg.text());
     }

     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         Channel client = ctx.channel();
         String addr = prosesser.getAddress(client);
         System.out.println("WebSocket Client:" + addr + "异常");
         // 当出现异常就关闭连接
         cause.printStackTrace();
         ctx.close();
     }
 }

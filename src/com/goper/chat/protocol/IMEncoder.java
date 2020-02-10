 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.protocol;

 import io.netty.buffer.ByteBuf;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.handler.codec.MessageToByteEncoder;
 import org.msgpack.MessagePack;

 /**
  * @Description 自定义IM协议的编码器
  * @Author YangLee
  * @Date2020/2/10 9:32 下午
  * @Version
  **/
 public class IMEncoder extends MessageToByteEncoder<IMMessage> {
     @Override
     protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out) throws Exception {
         out.writeBytes(new MessagePack().write(msg));
     }

     public String encode(IMMessage msg) {
         if (null == msg) {
             return "";
         }
         String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
         if (IMP.LOGIN.getName().equals(msg.getCmd()) ||
                 IMP.FLOWER.getName().equals(msg.getCmd())) {
             prex += ("[" + msg.getSender() + "][" + msg.getTerminal() + "]");
         } else if (IMP.CHAT.getName().equals(msg.getCmd())) {
             prex += ("[" + msg.getSender() + "]");
         } else if (IMP.SYSTEM.getName().equals(msg.getCmd())) {
             prex += ("[" + msg.getOnline() + "]");
         }
         if (!(null == msg.getContent() || "".equals(msg.getContent()))) {
             prex += (" - " + msg.getContent());
         }
         return prex;
     }
 }

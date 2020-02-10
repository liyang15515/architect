 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.protocol;

 import io.netty.buffer.ByteBuf;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.handler.codec.ByteToMessageDecoder;
 import org.msgpack.MessagePack;
 import org.msgpack.MessageTypeException;

 import java.util.List;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;

 /**
  * @Description 自定义IM协议的解码器
  * @Author YangLee
  * @Date2020/2/10 9:31 下午
  * @Version
  **/
 public class IMDecoder extends ByteToMessageDecoder {
     //解析IM写一下请求内容的正则
     private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");

     @Override
     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
         try {
             //先获取可读字节数
             final int length = in.readableBytes();
             final byte[] arrayInfo = new byte[length];
             String content = new String(arrayInfo, in.readerIndex(), length);
             //空消息不解析
             if (!(null == content || "".equals(content.trim()))) {
                 if (!IMP.isIMP(content)) {
                     ctx.channel().pipeline().remove(this);
                     return;
                 }
             }
             //把字符串变成一个我们能够识别的IMMessage对象
             in.getBytes(in.readerIndex(), arrayInfo, 0, length);
             //利用序列化框架，将网络流信息直接转化成IMMessge对象
             out.add(new MessagePack().read(arrayInfo, IMMessage.class));
             in.clear();
         } catch (MessageTypeException e) {
             ctx.channel().pipeline().remove(this);
         }
     }

     public IMMessage decode(String msg) {
         if (null == msg || "".equals(msg.trim())) {
             return null;
         }
         //解析字符串最好的方式就是正则
         Matcher m = pattern.matcher(msg);
         String header = "";
         String content = "";
         if (m.matches()) {
             header = m.group(1);
             content = m.group(3);
         }
         String[] heards = header.split("\\]\\[");
         long time = 0;
         try {
             time = Long.parseLong(heards[1]);
         } catch (Exception e) {
         }
         String nickName = heards[2];
         //昵称最多十个字
         nickName = nickName.length() < 10 ? nickName : nickName.substring(0, 9);

         if (msg.startsWith("[" + IMP.LOGIN.getName() + "]")) {
             return new IMMessage(heards[0], heards[3], time, nickName);
         } else if (msg.startsWith("[" + IMP.CHAT.getName() + "]")) {
             return new IMMessage(heards[0], time, nickName, content);
         } else if (msg.startsWith("[" + IMP.FLOWER.getName() + "]")) {
             return new IMMessage(heards[0], heards[3], time, nickName);
         } else {
             return null;
         }
     }
 }

 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.processor;

 import com.alibaba.fastjson.JSONObject;
 import com.goper.chat.protocol.IMDecoder;
 import com.goper.chat.protocol.IMEncoder;
 import com.goper.chat.protocol.IMMessage;
 import com.goper.chat.protocol.IMP;
 import io.netty.channel.Channel;
 import io.netty.channel.group.ChannelGroup;
 import io.netty.channel.group.DefaultChannelGroup;
 import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
 import io.netty.util.AttributeKey;
 import io.netty.util.concurrent.GlobalEventExecutor;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/10 11:07 下午
  * @Version
  **/
 public class MsgProsesser {

     //自定义编码器
     private IMEncoder encoder = new IMEncoder();
     private IMDecoder decoder = new IMDecoder();
     //1、记录在线用户，ChannelGroup是netty的一个容器，当然也可以用Map去保存在线用户
     //GlobalEventExecutor 全局的事件执行器
     private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

     /**
      * 获取用户远程IP地址
      *
      * @param client
      * @return
      */
     public String getAddress(Channel client) {
         return client.remoteAddress().toString().replaceFirst("/", "");
     }

     //     public void sendMsg(Channel client, IMMessage msg) {//IMMessage表示控制台拿到的
//         sendMsg(client, encoder.encode(msg));
//     }
//
//     public void sendMsg(Channel client, String msg) {//String msg 表示websocket拿到的
//
//     }
     /**
      * 获取扩展属性
      * @param client
      * @return
      */
     private void setAttrs(Channel client,String key,Object value){
         try{
             JSONObject json = client.attr(ATTRS).get();
             json.put(key, value);
             client.attr(ATTRS).set(json);
         }catch(Exception e){
             JSONObject json = new JSONObject();
             json.put(key, value);
             client.attr(ATTRS).set(json);
         }
     }
     /**
      * 获取扩展属性
      * @param client
      * @return
      */
     public JSONObject getAttrs(Channel client){
         try{
             return client.attr(ATTRS).get();
         }catch(Exception e){
             return null;
         }
     }
     public void process(Channel client, IMMessage msg) {//IMMessage表示控制台拿到的
         process(client, encoder.encode(msg));
     }

     //定义一些扩展属性
     public static final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
     public static final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
     public static final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");
     public static final AttributeKey<String> FROM = AttributeKey.valueOf("from");

     public void process(Channel client, String msg) {//String msg 表示websocket拿到的
         //统一在这里处理逻辑
         IMMessage request = decoder.decode(msg);
         if (null == request) {
             return;
         }
         String addr = getAddress(client);
         if (request.getCmd().equals(IMP.LOGIN.getName())) {
             //保存这个用户的IP地址
             // 端口
             // 昵称
             //终端类型
             client.attr(NICK_NAME).getAndSet(request.getSender());
             client.attr(IP_ADDR).getAndSet(addr);
             client.attr(FROM).getAndSet(request.getTerminal());
//			System.out.println(client.attr(FROM).get());
             //把这个用户保存到一个统一的容器中，要给所有已经在线的用户推送消息
             onlineUsers.add(client);

             for (Channel channel : onlineUsers) {
                 boolean isself = (channel == client);
                 if(!isself){
                     //不是自己，就告诉对方谁谁上线了
                     request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickName(client) + "加入");
                 }else{
                     //是自己，就告诉自己已经与服务器建立连接
                     request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "已与服务器建立连接！");
                 }

                 //消息已经准备好了，要开始推送消息了
                 //如果终端是控制台，就推IMMessage
                 if("Console".equals(channel.attr(FROM).get())){
                     channel.writeAndFlush(request);
                     continue;
                 }
                 //如果终端是websocket，就推String字符串
                 String content = encoder.encode(request);
                 channel.writeAndFlush(new TextWebSocketFrame(content));
             }
         } else if (request.getCmd().equals(IMP.CHAT.getName())) {
             for (Channel channel : onlineUsers) {
                 boolean isself = (channel == client);
                 if (isself) {
                     request.setSender("you");
                 }else{
                     request.setSender(getNickName(client));
                 }
                 request.setTime(sysTime());

                 if("Console".equals(channel.attr(FROM).get()) & !isself){
                     channel.writeAndFlush(request);
                     continue;
                 }
                 String content = encoder.encode(request);
                 channel.writeAndFlush(new TextWebSocketFrame(content));
             }
         } else if (request.getCmd().equals(IMP.FLOWER.getName())) {
             {
                 JSONObject attrs = getAttrs(client);
                 long currTime = sysTime();
                 //属于权限
                 //加一个专门处理权限的Handler
                 if(null != attrs){
                     long lastTime = attrs.getLongValue("lastFlowerTime");
                     //60秒之内不允许重复刷鲜花
                     int secends = 10;
                     long sub = currTime - lastTime;
                     if(sub < 1000 * secends){
                         request.setSender("you");
                         request.setCmd(IMP.SYSTEM.getName());
                         request.setContent("您送鲜花太频繁," + (secends - Math.round(sub / 1000)) + "秒后再试");

                         String content = encoder.encode(request);
                         client.writeAndFlush(new TextWebSocketFrame(content));
                         return;
                     }
                 }

                 //正常送花
                 for (Channel channel : onlineUsers) {
                     if (channel == client) {
                         request.setSender("you");
                         request.setContent("你给大家送了一波鲜花雨");
                         setAttrs(client, "lastFlowerTime", currTime);
                     }else{
                         request.setSender(getNickName(client));
                         request.setContent(getNickName(client) + "送来一波鲜花雨");
                     }
                     request.setTime(sysTime());

                     //只有web页面才支持刷花
                     String content = encoder.encode(request);
                     channel.writeAndFlush(new TextWebSocketFrame(content));
                 }
             }
         }
     }
     /**
      * 获取系统时间
      * @return
      */
     private Long sysTime(){
         return System.currentTimeMillis();
     }
     /**
      * 获取用户昵称
      * @param client
      * @return
      */
     public String getNickName(Channel client){
         return client.attr(NICK_NAME).get();
     }

 }

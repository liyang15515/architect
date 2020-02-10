 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.server;

 import com.goper.chat.protocol.IMDecoder;
 import com.goper.chat.protocol.IMEncoder;
 import com.goper.chat.server.handler.HttpServerHandler;
 import com.goper.chat.server.handler.TerminalServerHandler;
 import com.goper.chat.server.handler.WebSocketServerHandler;
 import io.netty.bootstrap.ServerBootstrap;
 import io.netty.channel.*;
 import io.netty.channel.nio.NioEventLoopGroup;
 import io.netty.channel.socket.SocketChannel;
 import io.netty.channel.socket.nio.NioServerSocketChannel;
 import io.netty.handler.codec.http.HttpObjectAggregator;
 import io.netty.handler.codec.http.HttpServerCodec;
 import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
 import io.netty.handler.stream.ChunkedWriteHandler;

 import java.io.IOException;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/10 10:11 下午
  * @Version
  **/
 public class ChatServer {

     private int port = 10080;

     public void start(int port) {
      //主从线程模型的线程池
         EventLoopGroup bossGroup = new NioEventLoopGroup();
         EventLoopGroup workerGroup = new NioEventLoopGroup();
         try {
             ServerBootstrap b = new ServerBootstrap();
             b.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .option(ChannelOption.SO_BACKLOG, 1024)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         public void initChannel(SocketChannel ch) throws Exception {

                             ChannelPipeline pipeline = ch.pipeline();

                             /** 解析自定义协议 */
                             pipeline.addLast(new IMDecoder());  //Inbound
                             pipeline.addLast(new IMEncoder());  //Outbound
                          //专门用来处理直接发送IMMessage对象的IDE控制台
                             pipeline.addLast(new TerminalServerHandler());  //Inbound





                             /** 解析Http请求 */
                             pipeline.addLast(new HttpServerCodec());  //Outbound
                             //主要是将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest完整的消息对象
                             pipeline.addLast(new HttpObjectAggregator(64 * 1024));//Inbound
                             //主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的 ,加上这个handler我们就不用考虑这个问题了
                             pipeline.addLast(new ChunkedWriteHandler());//Inbound、Outbound
                             //处理web页面的，解析http协议的
                          pipeline.addLast(new HttpServerHandler());//Inbound



                             /** 解析WebSocket请求 */
                             pipeline.addLast(new WebSocketServerProtocolHandler("/im"));    //Inbound
                            //处理websocket通信协议（主要用来处理IM聊天信息的）
                          pipeline.addLast(new WebSocketServerHandler()); //Inbound

                         }
                     });
             ChannelFuture f = b.bind(this.port).sync();
             System.out.println("服务已启动,监听端口" + this.port);
             f.channel().closeFuture().sync();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } finally {
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }

     public void start() {
         start(this.port);
     }


     public static void main(String[] args) throws IOException {
         if (args.length > 0) {
             new ChatServer().start(Integer.valueOf(args[0]));
         } else {
             new ChatServer().start();
         }
     }

 }

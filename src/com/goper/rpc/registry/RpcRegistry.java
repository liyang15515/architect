 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.registry;

 import io.netty.bootstrap.ServerBootstrap;
 import io.netty.channel.*;
 import io.netty.channel.nio.NioEventLoopGroup;
 import io.netty.channel.socket.SocketChannel;
 import io.netty.channel.socket.nio.NioServerSocketChannel;
 import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
 import io.netty.handler.codec.LengthFieldPrepender;
 import io.netty.handler.codec.serialization.ClassResolvers;
 import io.netty.handler.codec.serialization.ObjectDecoder;
 import io.netty.handler.codec.serialization.ObjectEncoder;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/8 10:32 下午
  * @Version
  **/
 public class RpcRegistry {

     private int port;

     public RpcRegistry(int port) {
         this.port = port;
     }

     public void start() {

         //初始化主线程池，Selector
         EventLoopGroup bossGroup = new NioEventLoopGroup();
         //子线程池初始化，具体对应客户端的处理逻辑
         EventLoopGroup workerGroup = new NioEventLoopGroup();

         ServerBootstrap server = new ServerBootstrap();
         server.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         //在netty中，把所有的业务逻辑处理全部归总到了一个队列中，
                         //这个队列中包含了各种各样的处理逻辑，对这些处理逻辑在netty中有一个封装
                         //封装成一个对象===》无锁化串行队列pipeline中。
                         ChannelPipeline pipeline = ch.pipeline();
                         //下面对要处理的业务逻辑进行封装
                         //对自定义协议的内容进行编码
                         pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                 //自定义解码器
                                 .addLast(new LengthFieldPrepender(4))
                                 //实参的 处理，编码
                                 .addLast("encoder", new ObjectEncoder())
                                 //实参的解码
                                 .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                 //前面的编解码是对数据的解析
                                 //最后一步，执行自己的业务逻辑
                                 //1.注册，给每一个对象起一个名字，对外提供服务的名字
                                 //2.服务位置要做一个标记
                                 .addLast(new RegistryHandler());
                     }
                 })
                 .option(ChannelOption.SO_BACKLOG, 128)
                 //保证子线程是长连接，每个链接都是可回收利用的
                 .option(ChannelOption.SO_KEEPALIVE, true);
         try {
             //正式启动服务，相当于一个死循环开始轮询
             ChannelFuture future = server.bind(this.port).sync();
             System.out.println("GP RPC Registry start listen at " + port);
             future.channel().closeFuture().sync();
         } catch (InterruptedException e) {
          bossGroup.shutdownGracefully();
          workerGroup.shutdownGracefully();
         }
     }

     public static void main(String[] args) {
         new RpcRegistry(8080).start();
     }
 }

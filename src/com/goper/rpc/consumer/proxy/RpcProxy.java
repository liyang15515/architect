 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.consumer.proxy;

 import com.goper.rpc.protocol.InvokerProtocol;
 import io.netty.bootstrap.Bootstrap;
 import io.netty.channel.*;
 import io.netty.channel.nio.NioEventLoopGroup;
 import io.netty.channel.socket.SocketChannel;
 import io.netty.channel.socket.nio.NioServerSocketChannel;
 import io.netty.channel.socket.nio.NioSocketChannel;
 import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
 import io.netty.handler.codec.LengthFieldPrepender;
 import io.netty.handler.codec.serialization.ClassResolvers;
 import io.netty.handler.codec.serialization.ObjectDecoder;
 import io.netty.handler.codec.serialization.ObjectEncoder;

 import java.lang.reflect.InvocationHandler;
 import java.lang.reflect.Method;
 import java.lang.reflect.Proxy;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/9 3:00 上午
  * @Version
  **/
 public class RpcProxy {

     public static <T> T create(Class<?> clazz) {
         MethodProxy proxy = new MethodProxy(clazz);
         T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
         return result;
     }

     //将本地调用通过代理的形式变成网络调用
     private static class MethodProxy implements InvocationHandler {
         private Class<?> clazz;

         public MethodProxy(Class<?> clazz) {
             this.clazz = clazz;
         }

         @Override
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
             //如果是class实体类，就直接调用
             if (Object.class.equals(method.getDeclaringClass())) {
                 return method.invoke(this, args);
             } else {//如果是interface就发送网络请求
                 return rpcInvoker(proxy, method, args);
             }
         }

         private Object rpcInvoker(Object proxy, Method method, Object[] args) {
             //先构造协议的内容，也就是消息
             InvokerProtocol msg = new InvokerProtocol();
             msg.setClassName(this.clazz.getName());
             msg.setMethodName(method.getName());
             msg.setValues(args);
             msg.setParams(method.getParameterTypes());

             final RpcProxyHandler consumerHandler = new RpcProxyHandler();

             //发送网络请求
             //Reactor 多线程模型。如果new NioEventLoopGroup(1)，参数为1代表Reactor单线程模型，selector就是单线程模型
             EventLoopGroup workGroup = new NioEventLoopGroup();
             try {
                 Bootstrap client = new Bootstrap();
                 client.group(workGroup)
                         .channel(NioSocketChannel.class)
                         .option(ChannelOption.TCP_NODELAY,true)
                         .handler(new ChannelInitializer<SocketChannel>() {
                             @Override
                             protected void initChannel(SocketChannel ch) throws Exception {
                                 ChannelPipeline pipeline = ch.pipeline();
                                 pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                 //自定义协议编码器
                                 pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                 //对象参数类型编码器
                                 pipeline.addLast("encoder", new ObjectEncoder());
                                 //对象参数类型解码器
                                 pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                 pipeline.addLast(consumerHandler);
                             }
                         });
                 ChannelFuture future = client.connect("localhost", 8080).sync();
                 future.channel().writeAndFlush(msg).sync();
                 future.channel().closeFuture().sync();
             } catch (Exception e) {
                 e.printStackTrace();
             } finally {
                 workGroup.shutdownGracefully();
             }
             return consumerHandler.getResponse();
         }
     }

 }

 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.registry;

 import com.goper.rpc.protocol.InvokerProtocol;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.ChannelInboundHandlerAdapter;

 import java.io.File;
 import java.lang.reflect.Method;
 import java.net.URL;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/8 10:33 下午
  * @Version
  **/
 public class RegistryHandler extends ChannelInboundHandlerAdapter {
     private List<String> classNames = new ArrayList<>();
     private Map<String,Object> registryMap = new ConcurrentHashMap<>();


     public RegistryHandler() {
         //1、根据一个包名将所有符合条件的class全部扫描出来，放到一个容器中
         scannerClass("com.goper.rpc.provider");
         //2、给每一个对应的class起一个唯一的名字，作为服务名称，保存到容器中
         doRegistry();
     }

     private void doRegistry() {
         if (classNames.isEmpty()) {
             return;
         }
         for (String className : classNames) {
          try {
           Class<?> clazz = Class.forName(className);
           Class<?> i = clazz.getInterfaces()[0];
           String serviceName = i.getName();
           //本来这里存的是网络路径，从配置文件中读取，
           //在调用的时候再去解析，这里直接用反射去调用
           registryMap.put(serviceName,clazz.newInstance());

          } catch (ClassNotFoundException e) {
           e.printStackTrace();
          } catch (IllegalAccessException e) {
           e.printStackTrace();
          } catch (InstantiationException e) {
           e.printStackTrace();
          }
         }
     }

     //正常情况下是读取配置文件，咱们现在简单粗暴，直接扫描本地class
     private void scannerClass(String packageName) {
         URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
         File classPath = new File(url.getFile());
         for (File file : classPath.listFiles()) {
             if (file.isDirectory()) {
                 scannerClass(packageName + "." + file.getName());
             } else {
                 classNames.add(packageName + "." + file.getName().replace(".class", ""));
             }
         }
     }

     //有客户端连接上的时候，会回调
     @Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      //3、当有客户端连接过来后，netty的编解码器就会把它解成一个InvokerProtocol对象，从而获取协议内容
      InvokerProtocol request = (InvokerProtocol)msg;
      Object result = new Object();
      //4.从注册好的容器中找到符合的服务
      if(registryMap.containsKey(request.getClassName())){
       Object service = registryMap.get(request.getClassName());
       Method method = service.getClass().getMethod(request.getMethodName(),request.getParams());
       result = method.invoke(service,request.getValues());
      }
      //5、通过远程调用provider得到返回的结果，并回复给客户端
      ctx.write(result);
      ctx.flush();
      ctx.close();
     }

     //链接发生异常的时候，会发生回调
     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         super.exceptionCaught(ctx, cause);
     }
 }

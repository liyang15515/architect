 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.tomcat;

 import com.goper.tomcat.http.GPRequest;
 import com.goper.tomcat.http.GPResponse;
 import com.goper.tomcat.http.GPServlet;
 import io.netty.bootstrap.ServerBootstrap;
 import io.netty.channel.*;
 import io.netty.channel.nio.NioEventLoopGroup;
 import io.netty.channel.socket.SocketChannel;
 import io.netty.channel.socket.nio.NioServerSocketChannel;
 import io.netty.handler.codec.http.HttpRequest;
 import io.netty.handler.codec.http.HttpRequestDecoder;
 import io.netty.handler.codec.http.HttpResponseEncoder;

 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Properties;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/8 4:11 下午
  * @Version
  **/
 //Netty就是一个同时支持多协议的网络通信框架
 public class NettyTomcat {
     private int port = 8080;

     private Map<String, GPServlet> servletMapping = new HashMap<String, GPServlet>();

     private Properties webxml = new Properties();

     private ServerSocket server = null;

     private void init() {

         //加载web.xml文件,同时初始化 ServletMapping对象
         try {
             String WEB_INF = this.getClass().getResource("/").getPath();
             System.out.println("WEB_INF is :" + WEB_INF);
             FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");
             webxml.load(fis);

             for (Object k : webxml.keySet()) {

                 String key = k.toString();
                 if (key.endsWith(".url")) {
                     String servletName = key.replaceAll("\\.url$", "");
                     String url = webxml.getProperty(key);
                     String className = webxml.getProperty(servletName + ".className");
                     GPServlet obj = (GPServlet) Class.forName(className).newInstance();
                     servletMapping.put(url, obj);
                 }
             }

         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     public void start() {
         init();
         //netty封装了nio、Reactor模型，出现了boos、work两个主从多线程模型
         //boss线程
         EventLoopGroup bossGroup = new NioEventLoopGroup();
         //work线程
         EventLoopGroup workGroup = new NioEventLoopGroup();
         //ServerBootstrap 是对ServerSocketChannel的封装
         ServerBootstrap server = new ServerBootstrap();
         server.group(bossGroup, workGroup)
                 //主线程处理类，看到这样的写法，底层运用的是反射
                 .channel(NioServerSocketChannel.class)
                 //子线程处理类，Handler
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     //客户端初始化处理
                     @Override
                     protected void initChannel(SocketChannel client) throws Exception {
                         //pipeline 无锁串行化编程
                         //HttpResponseEncoder和HttpRequestDecoder 是Netty对Http协议的封装
                         //HttpResponseEncoder编码器
                         client.pipeline().addLast(new HttpResponseEncoder());
                         //HttpRequestDecoder 解码器
                         client.pipeline().addLast(new HttpRequestDecoder());
                         //具体的业务处理
                         client.pipeline().addLast(new GPTomcatHandler());
                     }
                 })
                 //针对主线程的配置  分配线程最大数量 128
                 .option(ChannelOption.SO_BACKLOG, 128)
                 //针对子线程的配置  保持长连接
                 .option(ChannelOption.SO_KEEPALIVE, true);
         try {
             //启动服务
             ChannelFuture f = server.bind(port).sync();
             System.out.println("netty-tomcat 已经启动，监听端口是：" + this.port);
             f.channel().closeFuture().sync();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }

     public class GPTomcatHandler extends ChannelInboundHandlerAdapter {
         @Override
         public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
             if (msg instanceof HttpRequest) {
                 HttpRequest req = (HttpRequest) msg;
                 //转交给我们自己的request实现
                 GPRequest request = new GPRequest(ctx, req);
                 //转交给我们自己的response实现
                 GPResponse response = new GPResponse(ctx, req);
                 //实际业务处理
                 String url = request.getUrl2();
                 if (servletMapping.containsKey(url)) {
                     servletMapping.get(url).service(request, response);
                 } else {
                     response.write2("404 - Not Found");
                 }
             }
         }
     }

     private void process(Socket client) throws Exception {
         InputStream is = client.getInputStream();
         OutputStream os = client.getOutputStream();
         //7、Request(InputStream)/Response(OutputStream)流来包装对象
         GPRequest request = new GPRequest(is);
         GPResponse response = new GPResponse(os);
         //5、从http协议中拿到url，把相应的servlet用发射进行实例化，去匹配
         String url = request.getUrl();
         if (servletMapping.containsKey(url)) {
             //6、调用实例化对象的service()方法，执行具体的doGet/doPost方法
             servletMapping.get(url).service(request, response);
         } else {
             response.write("404 - not found");
         }
         os.flush();
         os.close();
         is.close();
         client.close();
     }

     public static void main(String[] args) {
         new NettyTomcat().start();
     }

 }

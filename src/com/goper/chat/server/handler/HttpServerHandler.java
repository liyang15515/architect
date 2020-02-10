 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.chat.server.handler;

 import io.netty.channel.*;
 import io.netty.handler.codec.http.*;

 import java.io.File;
 import java.io.RandomAccessFile;
 import java.net.URL;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/10 10:59 下午
  * @Version
  **/
 public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
     //获取class路径
     private URL baseURL = HttpServerHandler.class.getResource("");
     private final String webroot = "webroot";

     private File getResource(String fileName) throws Exception {
         String basePath = baseURL.toURI().toString();
         int start = basePath.indexOf("classes/");
         basePath = (basePath.substring(0, start) + "/" + "classes/").replaceAll("/+", "/");

         //找到webroot目录
         String path = basePath + webroot + "/" + fileName;
//        log.info("BaseURL:" + basePath);
         path = !path.contains("file:") ? path : path.substring(5);
         path = path.replaceAll("//", "/");
         return new File(path);
     }

     @Override
     protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
         FullHttpRequest request = msg;
         //请求一个URL  就相当于读取一个静态资源文件
         String uri = request.uri();
         //读取文件内容
         RandomAccessFile file = null;
         try {
             String page = uri.equals("/") ? "chat.html" : uri;
             file = new RandomAccessFile(getResource(page), "r");
         } catch (Exception e) {
             ctx.fireChannelRead(request.retain());
             return;
         }
         //构建一个HttpResponse对象，准备往外写文件内容
         HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
         String contextType = "text/html;";
         if (uri.endsWith(".css")) {
             contextType = "text/css;";
         } else if (uri.endsWith(".js")) {
             contextType = "text/javascript;";
         } else if (uri.toLowerCase().matches(".*\\.(jpg|png|gif)$")) {
             String ext = uri.substring(uri.lastIndexOf("."));
             contextType = "image/" + ext;
         }
         response.headers().set(HttpHeaderNames.CONTENT_TYPE, contextType + "charset=utf-8;");

         boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
         //在页面上支持websocket，设置长连接
         if (keepAlive) {
             response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length() + "");
             response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
         }
         ctx.write(response);

         ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));

         ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
         if (!keepAlive) {
             //通过异步的结果future去监听
             future.addListener(ChannelFutureListener.CLOSE);
         }

         file.close();
     }

     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         Channel client = ctx.channel();
         System.out.println("Client:" + client.remoteAddress() + "异常");
         // 当出现异常就关闭连接
         cause.printStackTrace();
         ctx.close();
     }
 }

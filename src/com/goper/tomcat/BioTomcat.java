 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.tomcat;

 import com.goper.tomcat.http.GPRequest;
 import com.goper.tomcat.http.GPResponse;
 import com.goper.tomcat.http.GPServlet;

 import java.io.FileInputStream;
 import java.io.IOException;
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
  * @Date2020/2/8 4:10 下午
  * @Version
  **/
 public class BioTomcat {

     private int port = 8080;

     private Map<String, GPServlet> servletMapping = new HashMap<String, GPServlet>();

     private Properties webxml = new Properties();

     private ServerSocket server = null;

     private void init() {

         //加载web.xml文件,同时初始化 ServletMapping对象
         try {
             String WEB_INF = this.getClass().getResource("/").getPath();
             System.out.println("WEB_INF is :"+WEB_INF);
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
         try {
             server = new ServerSocket(this.port);
             System.out.println("tomcat 已经启动，监听端口是：" + this.port);
             //用一个死循环来等待用户的请求
             while (true) {
                 Socket client = server.accept();
                 //4、http请求发送的数据就是字符串，有规律的字符串（HTTP协议）
                 process(client);
             }
         } catch (Exception e) {
             e.printStackTrace();
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
         new BioTomcat().start();
     }

 }

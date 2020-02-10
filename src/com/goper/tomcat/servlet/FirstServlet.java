 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.tomcat.servlet;

 import com.goper.tomcat.http.GPRequest;
 import com.goper.tomcat.http.GPResponse;
 import com.goper.tomcat.http.GPServlet;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/8 4:34 下午
  * @Version
  **/
 public class FirstServlet extends GPServlet {
  @Override
  public void service(GPRequest request, GPResponse response) throws Exception {
   super.service(request, response);
  }

  @Override
  public void doGet(GPRequest request, GPResponse response) throws Exception {
     this.doPost(request,response);
  }

  @Override
  public void doPost(GPRequest request, GPResponse response) throws Exception {
//     response.write("This is first servlet");
   response.write2("This is first servlet");

  }
 }

 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.bio.bioserver2;

 /**
  * @Description
  * @Author YangLee
  * @Date2019/12/8 12:04 下午
  * @Version
  **/
 public class RequestHandler {

     public static String handle(String request){
         String response = "from BIOServer Hello "+request+".\n";
         return response;
     }
 }

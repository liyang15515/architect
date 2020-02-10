 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.founder.concurrence.socketIO.IO01.bio2.runtime;

 /**
  * @Description 返回cpu的线程数，如 1-4-2 （1个cpu、每个cpu4核、每核2个超线程）则线程数是8
  * @Author YangLee
  * @Date2019/11/28 1:45 上午
  * @Version
  **/
 public class RuntimeTest {
     public static void main(String[] args) {
         System.out.println(Runtime.getRuntime().availableProcessors());
     }
 }

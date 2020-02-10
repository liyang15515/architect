 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.api;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/8 10:23 下午
  * @Version
  **/
 public interface IRpcService {
  /** 加 */
   int add(int a,int b);

  /** 减 */
   int sub(int a,int b);

  /** 乘 */
   int mult(int a,int b);

  /** 除 */
   int div(int a,int b);
 }

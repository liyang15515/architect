 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.consumer;

 import com.goper.rpc.api.IRpcHelloService;
 import com.goper.rpc.api.IRpcService;
 import com.goper.rpc.consumer.proxy.RpcProxy;
 import com.goper.rpc.provider.RpcHelloServiceImpl;
 import com.goper.rpc.provider.RpcServiceImpl;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/9 2:54 上午
  * @Version
  **/
 public class RpcConsumer {
     public static void main(String[] args) {
      //本地调用
//      IRpcHelloService helloService = new RpcHelloServiceImpl();
      IRpcHelloService helloService = RpcProxy.create(IRpcHelloService.class);
      System.out.println(helloService.hello("Tom"));;

//      IRpcService service = new RpcServiceImpl();
      IRpcService service = RpcProxy.create(IRpcService.class);
              System.out.println("8 + 2 = "+service.add(8,2));
      System.out.println("8 - 2 = " + service.sub(8, 2));
      System.out.println("8 * 2 = " + service.mult(8, 2));
      System.out.println("8 / 2 = " + service.div(8, 2));

     }
 }

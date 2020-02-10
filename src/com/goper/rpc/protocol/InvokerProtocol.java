 /*
  * Copyright (c) 2013, 2019, G42&Totok and/or   its affiliates. All rights reserved.
  * TOTOK PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */
 package com.goper.rpc.protocol;

 import java.io.Serializable;

 /**
  * @Description
  * @Author YangLee
  * @Date2020/2/8 10:26 下午
  * @Version
  **/
 public class InvokerProtocol implements Serializable {

   private String className;//类名
   private String methodName;//函数名称
   private Class<?>[] params;//形参列表
   private Object[] values;//实参列表

  public String getClassName() {
   return className;
  }

  public void setClassName(String className) {
   this.className = className;
  }

  public String getMethodName() {
   return methodName;
  }

  public void setMethodName(String methodName) {
   this.methodName = methodName;
  }

  public Class<?>[] getParams() {
   return params;
  }

  public void setParams(Class<?>[] params) {
   this.params = params;
  }

  public Object[] getValues() {
   return values;
  }

  public void setValues(Object[] values) {
   this.values = values;
  }
 }

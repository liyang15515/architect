package com.goper.tomcat.http;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;


public class GPRequest {
	private String method;
	private String url;
	private ChannelHandlerContext ctx;
	private HttpRequest req;

	public GPRequest(InputStream inputStream){
		String content = "";
		byte[] buff = new byte[1024];
		int len = 0;
		try {
			if((len = inputStream.read(buff)) > 0 ){
				content = new String(buff,0,len);
				String line = content.split("\\n")[0];
				String[] arr = line.split("\\s");
				this.method = arr[0];
				this.url = arr[1].split("\\?")[0];
			}
//			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public GPRequest(ChannelHandlerContext ctx, HttpRequest req){
		this.ctx = ctx;
		this.req = 	req;

	}


	public String getUrl() {
		return url;
	}
	public String getUrl2(){
		return req.uri();
	}

	public String getMethod() {

		return method;
	}
	public String getMethod2() {
		return req.method().name().toString();
	}
}

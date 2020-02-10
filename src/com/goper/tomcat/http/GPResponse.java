package com.goper.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.io.OutputStream;


public class GPResponse {

    private OutputStream out;

    private ChannelHandlerContext ctx;//SocketChannel的封装
    private HttpRequest req;
    public GPResponse(OutputStream os) {
        this.out = os;
    }
    public GPResponse(ChannelHandlerContext ctx,HttpRequest req){
        this.ctx = ctx;
        this.req = req;
    }

    public void write(String s) {
        //用的是http协议，输出也要是http协议
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
				.append("\r\n")
                .append(s);
        try {
            out.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write2(String out)throws Exception{
        if (out == null || out.length() == 0) {
            return;
        }
        // 设置 http协议及响应头信息
        FullHttpResponse response = new DefaultFullHttpResponse(
                // 设置http版本为1.1
                HttpVersion.HTTP_1_1,
                // 设置响应状态码
                HttpResponseStatus.OK,
                // 将输出值写出 编码为UTF-8
                Unpooled.wrappedBuffer(out.getBytes("UTF-8"))
        );
        response.headers().set("Content-Type","text/html;");
        try {
            ctx.write(response);
        }  finally {
            ctx.flush();
            ctx.close();
        }

    }
}

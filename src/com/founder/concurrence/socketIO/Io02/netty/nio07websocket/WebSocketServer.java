package com.founder.concurrence.socketIO.Io02.netty.nio07websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
/**@Author yanglee
* @Description: TODO  netty 通过浏览器 基于websocket协议的网络通信 （Tomcat8的原型）
* @Param
* @Return
* @Date 2019-05-22 16:33
   websocket 可以实现在线的网络聊天室
*/
public class WebSocketServer {
    public void run(int port) throws Exception {
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch)
				throws Exception {
			    ChannelPipeline pipeline = ch.pipeline();
			    pipeline.addLast("netty03http-codec",
				    new HttpServerCodec());
			    pipeline.addLast("aggregator",
				    new HttpObjectAggregator(65536));
			    ch.pipeline().addLast("netty03http-chunked",
				    new ChunkedWriteHandler());
			    pipeline.addLast("handler",
				    new WebSocketServerHandler());
			}
		    });

	    Channel ch = b.bind(port).sync().channel();
	    System.out.println("Web socket server started at port " + port + '.');
	    System.out.println("Open your browser and navigate to netty03http://localhost:" + port + '/');

	    ch.closeFuture().sync();
	} finally {
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

    public static void main(String[] args) throws Exception {

    	new WebSocketServer().run(8765);
    }
}

package com.founder.concurrence.socketIO.Io02.netty.nio01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
/**@Author yanglee
* @Description: TODO netty如何实现通信  客户端
* @Param
* @Return
* @Date 2019-04-28 21:42
*/
public class Client {

	public static void main(String[] args) throws Exception {
		
		EventLoopGroup workgroup = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(workgroup)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		
		ChannelFuture cf1 = b.connect("127.0.0.1", 8765).sync();
		
		//buf
		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
		
		cf1.channel().closeFuture().sync();
		workgroup.shutdownGracefully();
		
	}
}

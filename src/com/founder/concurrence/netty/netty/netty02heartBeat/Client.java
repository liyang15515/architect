package com.founder.concurrence.netty.netty.netty02heartBeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
/**@Author yanglee
* @Description: TODO netty 心跳检测
* @Param
* @Return
* @Date 2019-05-23 15:13
*/
public class Client {

	
	public static void main(String[] args) throws Exception{
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
				sc.pipeline().addLast(new ClienHeartBeattHandler());
			}
		});
		
		ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();

		cf.channel().closeFuture().sync();
		group.shutdownGracefully();
	}
}

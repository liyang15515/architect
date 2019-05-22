package com.founder.concurrence.socketIO.Io02.netty.nio04;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author yanglee
 * @Description: TODO netty解决tcp的粘包和拆包client端（FixedLengthFrameDecoder定长解码器方式）
 * @Param
 * @Return
 * @Date 2019-04-28 22:36
 */
public class Client {

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new FixedLengthFrameDecoder(5));
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf = b.connect("127.0.0.1", 8761).sync();

        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("aaaaabbbbb".getBytes()));
        cf.channel().writeAndFlush(Unpooled.copiedBuffer("ccccccc".getBytes()));

        //等待客户端端口关闭
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();

    }
}

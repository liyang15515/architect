
  package com.founder.concurrence.netty.netty.netty03http;

  import io.netty.channel.ChannelInitializer;
  import io.netty.channel.ChannelPipeline;
  import io.netty.channel.socket.SocketChannel;
  import io.netty.handler.codec.http.HttpServerCodec;
  import io.netty.handler.ssl.SslContext;
/**@Author yanglee
* @Description: TODO netty是基于tcp传输层协议的，要想用应用层http协议，需要初始化 信道channel
* @Param
* @Return
* @Date 2019-05-23 17:24
*/
  public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
  
      private final SslContext sslCtx;
  
      public HttpHelloWorldServerInitializer(SslContext sslCtx) {
         this.sslCtx = sslCtx;
      }
  
      @Override
      public void initChannel(SocketChannel ch) {
          ChannelPipeline p = ch.pipeline();
          if (sslCtx != null) {
              p.addLast(sslCtx.newHandler(ch.alloc()));
          }
          p.addLast(new HttpServerCodec());//用于处理http请求
          p.addLast(new HttpHelloWorldServerHandler());
      }
  }
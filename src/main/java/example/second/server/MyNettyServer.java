package example.second.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MyNettyServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGRoup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGRoup, workGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MyNettyServerInitiator());

            ChannelFuture channelFuture = bootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGRoup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

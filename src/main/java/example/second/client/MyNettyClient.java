package example.second.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * 启动多个客户端实例
 */
public class MyNettyClient {
    public static void main(String[] args) throws Exception {

        EventLoopGroup wokerGroup = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(wokerGroup).channel(NioSocketChannel.class).handler(new MyNettyClientInititor());
            Channel channel = bootstrap.connect("127.0.0.1", 8899).sync().channel();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg);
            }

//            future.channel().closeFuture().sync();

        } finally {
            wokerGroup.shutdownGracefully();
        }

    }
}

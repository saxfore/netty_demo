package example.second.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyNettyServerInitiator extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("MyNettyServerInitiator initChannel = [" + this.hashCode() + "]");
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("ideSateHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast("stringDecoder", new StringDecoder());
        pipeline.addLast("stringEncoder", new StringEncoder());
        pipeline.addLast("heartBeatHandler", new HeartBeatHandler());
        pipeline.addLast("myServerHandler", new MyServerHandler());
    }
}

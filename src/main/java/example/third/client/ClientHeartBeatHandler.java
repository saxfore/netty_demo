package example.third.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 每新连接一个客户端就会创建一次该handler实例
 *
 */
public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter {
    private int count = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        System.out.println("ClientHeartBeatHandler userEventTriggered = " + this.hashCode());

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case ALL_IDLE:
                    System.out.println("HeartBeatHandler all_idle ...");
                    ctx.channel().writeAndFlush("Hi...");
                    break;
                case READER_IDLE:
                    count++;
                    System.out.println("HeartBeatHandler reader_idle ..., count = " + count);
                    break;
                case WRITER_IDLE:
                    System.out.println("HeartBeatHandler write_idle ...");
                    break;
                default:
                    break;
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }
}

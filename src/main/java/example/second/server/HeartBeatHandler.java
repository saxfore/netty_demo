package example.second.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private int count = 0;

    /**
     * Calls {@link ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("HeartBeatHandler userEventTriggered = " + this.hashCode());

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case ALL_IDLE:
                    System.out.println("HeartBeatHandler all_idle ...");
                    break;
                case READER_IDLE:
                    count++;
                    System.out.println("HeartBeatHandler reader_idle ..., count = " + count);

                    // 读空闲重试次数超过三次的认定已经掉线
                    if (count > 3) {
                        ctx.writeAndFlush("服务器消息：您挂机时长达20秒之久，系统将对您进行强制下线！");
                        ctx.channel().close();
                    }

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

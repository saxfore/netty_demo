package example.second.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 多人通讯的功能
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println("MyServerHandler channelRead0 ... ");
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush(channel.remoteAddress() + "：" + msg + "\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyServerHandler handlerAdded ..., === " + this.hashCode());
        channelGroup.forEach(channel -> {
            channel.writeAndFlush("客户端：" + ctx.channel().remoteAddress() + "上线通知");
        });
        channelGroup.add(ctx.channel());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyServerHandler channelRegistered ...");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyServerHandler channelActive ...");
        ctx.channel().writeAndFlush("感谢您访问 Netty_demo, 更多详情请访问 https://github.com/saxfore/netty_demo ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyServerHandler channelInactive ...");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyServerHandler channelUnregistered ...");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyServerHandler handlerRemoved ...");
        channelGroup.writeAndFlush("客户端：" + ctx.channel().remoteAddress() + "下线通知");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("MyServerHandler exceptionCaught ...");
        super.exceptionCaught(ctx, cause);
    }
}

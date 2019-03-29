package example.third.server;

import example.third.GlobalContext;
import example.third.dataBean.ChatUser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 每新连接一个客户端就会创建一次该handler实例
 */
public class WebSocketHeartBeatHandler extends ChannelInboundHandlerAdapter {
    private int count = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        System.out.println("HeartBeatHandler userEventTriggered = " + this.hashCode());

        Channel currentChannel = ctx.channel();
        ChatUser chatUser = GlobalContext.getUser(currentChannel.id().asLongText());

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case ALL_IDLE:
//                    System.out.println("HeartBeatHandler all_idle ...");
                    break;
                case READER_IDLE:
                    count++;
//                    System.out.println("HeartBeatHandler reader_idle ..., count = " + count);

                    // 读空闲重试次数超过2次的发出警告
                    if (count == 5) {
                        String responseText = "服务器消息：@" + chatUser.getNickName() + "请勿长时间挂机！";
                        TextWebSocketFrame response = new TextWebSocketFrame(responseText);
                        GlobalContext.getChannelGroup().writeAndFlush(response);
                    }

                    // 读空闲重试次数超过10次的认定已经掉线
                    if (count == 10) {
                        // ctx.channel().close();
                        String responseText = "服务器消息：管理员将" + chatUser.getNickName() + "踢出群聊！";
                        TextWebSocketFrame response = new TextWebSocketFrame(responseText);
                        GlobalContext.getChannelGroup().writeAndFlush(response);
                    }

                    break;
                case WRITER_IDLE:
//                    System.out.println("HeartBeatHandler write_idle ...");
                    break;
                default:
                    break;
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }
}

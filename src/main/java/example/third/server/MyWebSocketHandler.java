package example.third.server;

import example.third.GlobalContext;
import example.third.dataBean.ChatUser;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 每新连接一个客户端就会创建一次该handler实例
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("MyWebSocketHandler channelRead0 ...");

        Channel currentChannel = ctx.channel();
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            String content = textWebSocketFrame.text();
            System.out.println("------------" + content + "----------");

            if (content.startsWith("/login.do")) {
                String[] sp = content.split("/");
                if (sp != null && sp.length > 0) {
                    String loginInfo = sp[sp.length - 1];
                    if (!GlobalContext.isLogin(loginInfo)) {

                        String responseText = "error";
                        TextWebSocketFrame response = new TextWebSocketFrame(responseText);
                        currentChannel.writeAndFlush(response).addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (future.isDone()) {
                                    System.out.println("future = [" + future.isDone() + "]");
                                    currentChannel.close();
                                }
                            }
                        });

                        return;
                    } else {

                        GlobalContext.updateUserInfo(loginInfo, ctx.channel());
                        ChatUser chatUser = GlobalContext.getUser(currentChannel.id().asLongText());
                        GlobalContext.getChannelGroup().forEach(channel -> {
                            if (channel != currentChannel) {
                                String responseText = "服务器消息：" + chatUser.getNickName() + "加入群聊！";
                                TextWebSocketFrame response = new TextWebSocketFrame(responseText);
                                channel.writeAndFlush(response);
                            }
                        });
                    }
                }
            }

            ChatUser chatUser = GlobalContext.getUser(currentChannel.id().asLongText());
            String responseText = chatUser.getNickName() + ": " + content;
            TextWebSocketFrame response = new TextWebSocketFrame(responseText);
            GlobalContext.getChannelGroup().writeAndFlush(response);

        } else if (msg instanceof BinaryWebSocketFrame) {
            System.out.println("收到二进制消息：" + ((BinaryWebSocketFrame) msg).content().readableBytes());
            BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(Unpooled.buffer().writeBytes("xxx".getBytes()));
            GlobalContext.getChannelGroup().writeAndFlush(binaryWebSocketFrame);

        } else {
            // ...
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel currentChannel = ctx.channel();
        GlobalContext.getChannelGroup().add(currentChannel);

        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String responseText = "感谢您访问 Netty_demo, 更多详情请访问 https://github.com/saxfore/netty_demo ";
        TextWebSocketFrame response = new TextWebSocketFrame(responseText);
        ctx.channel().writeAndFlush(response);
        super.channelActive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel currentChannel = ctx.channel();
        ChatUser chatUser = GlobalContext.getUser(currentChannel.id().asLongText());

        GlobalContext.getChannelGroup().forEach(channel -> {
            String responseText = "服务器消息：" + chatUser.getNickName() + "退出群聊！";
            TextWebSocketFrame response = new TextWebSocketFrame(responseText);
            channel.writeAndFlush(response);
        });

        // 连接断开会自动移除channelGroup，无需手动移除
        // channelGroup.remove(currentChannel);
//        GlobalContext.removeUser(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

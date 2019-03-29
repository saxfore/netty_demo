package example.third;

import example.third.dataBean.ChatUser;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

public class GlobalContext {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<String, ChatUser> chatUserCache = new HashMap<>();

    public static void init() {
        ChatUser user1 = new ChatUser();
        user1.setUserName("guojing");
        user1.setPassword("123456");
        user1.setNickName("郭靖");

        String key1 = user1.getUserName() + "&" + user1.getPassword();
        chatUserCache.put(key1, user1);

        ChatUser user2 = new ChatUser();
        user2.setUserName("huangrong");
        user2.setPassword("123456");
        user2.setNickName("黄蓉");

        String key2 = user2.getUserName() + "&" + user2.getPassword();
        chatUserCache.put(key2, user2);


        ChatUser user3 = new ChatUser();
        user3.setUserName("zhangwuji");
        user3.setPassword("123456");
        user3.setNickName("张无忌");

        String key3 = user3.getUserName() + "&" + user3.getPassword();
        chatUserCache.put(key3, user3);
    }

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public static Map<String, ChatUser> getChatUserCache() {
        return chatUserCache;
    }

    public static ChatUser getUser(String channelId) {
        for (Map.Entry<String, ChatUser> entry : chatUserCache.entrySet()) {
            if (channelId.equals(entry.getValue().getUserID())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static void updateUserInfo(Channel channel) {
        String channelID = channel.id().asLongText();
        String remoteAddr = channel.remoteAddress().toString();
        int lastSuffix = remoteAddr.lastIndexOf(":");

        ChatUser user = new ChatUser();
        user.setUserID(channelID);
        user.setRemoteIp(remoteAddr.substring(1, lastSuffix));
        user.setRemotPort(remoteAddr.substring(lastSuffix + 1));
        user.setChatTime(System.currentTimeMillis());

        if (!isNullOrEmpty(user.getUserName()) && !isNullOrEmpty(user.getPassword())) {
            String key = user.getUserName() + "&" + user.getPassword();
            chatUserCache.put(key, user);
        }
    }

    public static void updateUserInfo(String loginInfo, Channel channel) {
        String channelID = channel.id().asLongText();
        String remoteAddr = channel.remoteAddress().toString();
        int lastSuffix = remoteAddr.lastIndexOf(":");

        ChatUser user = new ChatUser();
        user.setUserID(channelID);
        user.setRemoteIp(remoteAddr.substring(1, lastSuffix));
        user.setRemotPort(remoteAddr.substring(lastSuffix + 1));
        user.setChatTime(System.currentTimeMillis());

        if (!isNullOrEmpty(loginInfo)) {
            String key = user.getUserName() + "&" + user.getPassword();
            chatUserCache.put(key, user);
        }
    }

    public static void removeUser(Channel channel) {
        chatUserCache.remove(chatUserCache.get(channel.id().asLongText()));
    }

    public static boolean isLogin(String loginInfo) {
        return chatUserCache.containsKey(loginInfo);
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value) || "null".equals(value);
    }
}

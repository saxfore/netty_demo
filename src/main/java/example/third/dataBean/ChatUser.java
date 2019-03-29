package example.third.dataBean;

import java.io.Serializable;

public class ChatUser implements Serializable {
    private String userID;
    private String userName;
    private String password;
    private String nickName;
    private long chatTime;
    private String remoteIp;
    private String remotPort;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getChatTime() {
        return chatTime;
    }

    public void setChatTime(long chatTime) {
        this.chatTime = chatTime;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getRemotPort() {
        return remotPort;
    }

    public void setRemotPort(String remotPort) {
        this.remotPort = remotPort;
    }
}

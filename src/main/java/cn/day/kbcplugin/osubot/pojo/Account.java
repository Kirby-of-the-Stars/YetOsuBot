package cn.day.kbcplugin.osubot.pojo;

import cn.day.kbcplugin.osubot.annotion.TableId;
import cn.day.kbcplugin.osubot.annotion.TableName;
import cn.day.kbcplugin.osubot.enums.ServerEnum;

@TableName("accounts")
public class Account {

    @TableId("osu_id")
    private int osuId;
    private String kookId;
    private String userName;
    private int serverId;

    public int getOsuId() {
        return osuId;
    }

    public void setOsuId(int osuId) {
        this.osuId = osuId;
    }

    public String getKookId() {
        return kookId;
    }

    public void setKookId(String kookId) {
        this.kookId = kookId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServerName(){
        return ServerEnum.parseInt(this.serverId).name();
    }

    public Account(int osuId, String kookId, int serverId) {
        this.osuId = osuId;
        this.kookId = kookId;
        this.serverId = serverId;
    }

    public Account() {

    }
}

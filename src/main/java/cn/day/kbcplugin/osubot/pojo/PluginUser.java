package cn.day.kbcplugin.osubot.pojo;

import cn.day.kbcplugin.osubot.annotion.TableId;
import cn.day.kbcplugin.osubot.annotion.TableName;
import cn.day.kbcplugin.osubot.enums.ServerEnum;

@TableName("user")
public class PluginUser {
    @TableId("kook_id")
    private String kookId;

    private int osuId;

    private int mode;

    private Integer serverId;

    public PluginUser(String kookId, int osuId, int mode, Integer serverId) {
        this.kookId = kookId;
        this.osuId = osuId;
        this.mode = mode;
        this.serverId = serverId;
    }
    public PluginUser(String kookId, int osuId, int mode) {
        this.kookId = kookId;
        this.osuId = osuId;
        this.mode = mode;
    }

    public String getKookId() {
        return kookId;
    }

    public void setKookId(String kookId) {
        this.kookId = kookId;
    }

    public int getOsuId() {
        return osuId;
    }

    public void setOsuId(int osuId) {
        this.osuId = osuId;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public ServerEnum getCurrentServer(){
        return ServerEnum.parseInt(this.serverId);
    }
}

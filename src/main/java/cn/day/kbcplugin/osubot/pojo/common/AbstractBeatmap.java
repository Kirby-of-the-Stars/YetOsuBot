package cn.day.kbcplugin.osubot.pojo.common;

/**
 * 为了解决多个api返回的map info不一致问题
 */
public interface AbstractBeatmap {
    Integer getSid();
    Integer getBid();
    String getArtist();
    String getTitle();
    Integer getMaxCombo();
    String getVersion();
    String getCreator();
    String getBgName();
}

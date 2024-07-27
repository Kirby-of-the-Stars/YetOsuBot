package cn.day.kbcplugin.osubot.model.api.base;

/**
 * 为了解决多个api返回的map info不一致问题
 */
public interface IBeatmap {
    Long getSid();
    Long getBid();
    String getArtist();
    String getTitle();
    Integer getMaxCombo();
    String getVersion();
    String getCreator();
    String getBgName();
}

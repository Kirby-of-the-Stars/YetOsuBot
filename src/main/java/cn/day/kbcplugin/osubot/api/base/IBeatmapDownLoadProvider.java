package cn.day.kbcplugin.osubot.api.base;

import java.io.File;

public interface IBeatmapDownLoadProvider {
    /**
     * 下载地图
     * @return 下载的文件
     */
    boolean downloadMap(String beatmapId, File target);

    /**
     * 获取API名字
     */
    String getName();
}

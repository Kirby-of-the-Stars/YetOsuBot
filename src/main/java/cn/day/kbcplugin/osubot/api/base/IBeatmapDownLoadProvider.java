package cn.day.kbcplugin.osubot.api.base;

import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;

import java.io.File;

public interface IBeatmapDownLoadProvider {
    /**
     * 下载地图
     *
     * @return 下载的文件
     */
    boolean downloadMap(IBeatmap beatmap, File target);

    /**
     * 获取API名字
     */
    String getName();
}

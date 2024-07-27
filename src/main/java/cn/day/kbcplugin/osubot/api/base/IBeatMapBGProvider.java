package cn.day.kbcplugin.osubot.api.base;

import java.io.File;

public interface IBeatMapBGProvider {
    /**
     * 下载铺面的背景图片
     * @return 是否下载成功
     */
    File downloadBG(String beatmapId, File target);

    /**
     * 获取API名字
     */
    String getName();
}

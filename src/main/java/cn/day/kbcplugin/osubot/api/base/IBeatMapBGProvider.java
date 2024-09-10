package cn.day.kbcplugin.osubot.api.base;

import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;

import java.io.File;

public interface IBeatMapBGProvider {
    /**
     * 下载铺面的背景图片
     *
     * @return 是否下载成功
     */
    File downloadBG(IBeatmap beatmap, File target);

    /**
     * 获取API名字
     */
    String getName();
}

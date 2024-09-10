package cn.day.kbcplugin.osubot.api.base;

import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;

import java.io.File;

public interface IBeatMapBGProvider {
    /**
     * 下载铺面的背景图片
     * @param beatmap 铺面信息
     * @param name 背景图名字
     * @param target 目标路径
     * @return 是否下载成功
     */
    File downloadBG(IBeatmap beatmap, File target,String name);

    /**
     * 获取API名字
     */
    String getName();
}

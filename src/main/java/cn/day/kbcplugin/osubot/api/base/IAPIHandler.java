package cn.day.kbcplugin.osubot.api.base;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public interface IAPIHandler {
    /**
     * 获取用户信息
     */
    @Nullable
    IUserInfo getUserInfo(String osuId, OsuModeEnum mode);

    /**
     * 获取铺面信息
     */
    @Nullable
    IBeatmap getBeatmap(String bid, String sid);

    /**
     * 获取最近的成绩
     */
    @Nullable
    IScore getRecentScore(String osuId, OsuModeEnum mode);

    /**
     * 获取前N个成绩
     */
    @Nullable
    List<? extends IScore> getTopNScores(String osuId, OsuModeEnum mode, int count);

    /**
     * 获取用户头像
     *
     * @param osuId osu id
     * @return base64
     */
    @Nullable
    String getUserAvatar(String osuId);

    /**
     * 返回API名字
     */
    String getName();
}

package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IAPIHandler;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import org.dromara.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

class SBApiTest {

    private final IAPIHandler sbApi = new SBApi();

    @Test
    void getUserInfo() {
        final String uid = "4660";
        IUserInfo info = sbApi.getUserInfo(uid, OsuModeEnum.STANDER_RX);
        Assert.notNull(info);
    }

    @Test
    void getBeatmap() {
        final Long bid = 696225L;
        IBeatmap map = sbApi.getBeatmap(String.valueOf(bid),null);
        Assert.notNull(map);
    }

    @Test
    void getRecentScore() {
        final String uid = "4660";
        IScore score = sbApi.getRecentScore(uid, OsuModeEnum.STANDER_RX);
        Assert.notNull(score);
    }

    @Test
    void getTopNScores() {
        final String uid = "4660";
        final int size = 10;
    }

}
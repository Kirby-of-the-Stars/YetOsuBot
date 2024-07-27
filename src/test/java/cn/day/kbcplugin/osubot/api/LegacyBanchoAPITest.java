package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.APIKey;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.bancho.legacy.LegacyBanchoBeatmap;
import cn.day.kbcplugin.osubot.model.api.bancho.legacy.LegacyBanchoScore;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import org.dromara.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

class LegacyBanchoAPITest {

    private LegacyBanchoAPI legacyBanchoAPI = new LegacyBanchoAPI(APIKey.KEY);

    @Test
    void getUserInfo() {
        final String uid = "10408827";
        final OsuModeEnum mode = OsuModeEnum.STANDER;
        IUserInfo userInfo = legacyBanchoAPI.getUserInfo(uid, mode);
        Assert.notNull(userInfo);
    }

    @Test
    void getBeatmap() {
        final String bid = "696225";
        LegacyBanchoBeatmap beatmap = legacyBanchoAPI.getBeatmap(bid, null);
        Assert.notNull(beatmap);
    }

    @Test
    void getRecentScore() {
        final String uid = "7562902";
        final OsuModeEnum mode = OsuModeEnum.STANDER;
        LegacyBanchoScore recentScore = legacyBanchoAPI.getRecentScore(uid, mode);
        Assert.notNull(recentScore);
    }

    @Test
    void getTopNScores() {
        final String uid = "10408827";
        final OsuModeEnum mode = OsuModeEnum.STANDER;
        final int size = 10;
        List<LegacyBanchoScore> topNScores = legacyBanchoAPI.getTopNScores(uid, mode, size);
        Assert.notEmpty(topNScores);
    }
}
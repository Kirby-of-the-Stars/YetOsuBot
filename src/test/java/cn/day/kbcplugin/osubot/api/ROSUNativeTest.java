package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.model.api.OsuMap;
import cn.day.kbcplugin.osubot.model.api.base.Mods;
import cn.day.kbcplugin.osubot.model.api.PPResult;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

class ROSUNativeTest {

    @Test
    void test() {
        //TODO waiting for testing case
        File osuFile = new File("D:\\2019-2-24osu!backup\\Songs\\896080 Wakeshima Kanon - Tsukinami\\Wakeshima Kanon - Tsukinami (Reform) [AR9.7].osu");
        OsuMap map = new OsuMap(
                osuFile.getAbsolutePath(),
                Mods.None().intValue(),
                97.76f,
                1,
                1519,
                1689
        );
        PPResult ppResult = RustOsuPPCalculator.INSTANCE.cal_pp(map);
        Assert.notNull(ppResult);
        System.out.println(JSONUtil.toJsonStr(ppResult));
    }
}

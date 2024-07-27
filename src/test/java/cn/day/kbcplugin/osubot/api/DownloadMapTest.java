package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.utils.MapHelper;
import org.dromara.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;

class DownloadMapTest {

    @Test
    void test() {
        Main.BeatMapsPath = new File("D:\\test");
        final String bid = "696225";
        final String sid = "306591";
        File osuFile = MapHelper.getOsuFile(bid, sid);
        Assert.notNull(osuFile);
    }
}

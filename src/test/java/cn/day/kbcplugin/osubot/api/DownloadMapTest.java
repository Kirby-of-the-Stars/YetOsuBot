package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.APIKey;
import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.model.api.bancho.legacy.LegacyBanchoBeatmap;
import cn.day.kbcplugin.osubot.utils.MapHelper;
import org.dromara.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;

class DownloadMapTest {

    static {
        Main.BeatMapsPath = new File("D:\\test");
        APIHandler.INSTANCE.init(new ChimuAPI(),new LegacyBanchoAPI(APIKey.KEY),new SBApi());
    }

    @Test
    void test() {
        final String bid = "696225";
        final String sid = "306591";
        LegacyBanchoBeatmap banchoBeatmap = new LegacyBanchoBeatmap();
        banchoBeatmap.setBeatmapId(Long.parseLong(bid));
        banchoBeatmap.setBeatmapsetId(Long.parseLong(sid));
        File osuFile = MapHelper.getOsuFile(banchoBeatmap);
        Assert.notNull(osuFile);
    }

    @Test
    void sayo(){
        final String bid = "696225";
        final String sid = "306591";
        LegacyBanchoBeatmap banchoBeatmap = new LegacyBanchoBeatmap();
        banchoBeatmap.setBeatmapId(Long.parseLong(bid));
        banchoBeatmap.setBeatmapsetId(Long.parseLong(sid));
        banchoBeatmap.setVersion("Special");
        File osuFile = MapHelper.getOsuFile(banchoBeatmap);
        Assert.notNull(osuFile);
        File mapFile = MapHelper.getBgFile(banchoBeatmap);
        Assert.notNull(mapFile);
    }
}

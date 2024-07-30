package cn.day.kbcplugin.osubot.card;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.LegacyBanchoAPI;
import cn.day.kbcplugin.osubot.model.api.chimu.ChimuBeatmap;
import cn.day.kbcplugin.osubot.model.api.chimu.ChimuBeatmapInfo;
import org.dromara.hutool.core.text.StrUtil;
import snw.jkook.HttpAPI;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.module.DividerModule;
import snw.jkook.message.component.card.module.HeaderModule;
import snw.jkook.message.component.card.module.SectionModule;

import java.util.List;

public class SearchMapCard {

    public static MultipleCardComponent build(List<ChimuBeatmap> maps) {
        CardBuilder builder = new CardBuilder();
        builder.setSize(Size.LG);
        builder.setTheme(Theme.SUCCESS);
        if (maps.isEmpty()) {
            builder.addModule(new HeaderModule("没有任何结果呢~"));
            return builder.build();
        }
        builder.addModule(new HeaderModule("搜索结果:"));
        int index = 0;
        HttpAPI httpAPI = Main.instance.getCore().getHttpAPI();
        for (ChimuBeatmap map : maps) {
            String directLink = StrUtil.format("{}{}", LegacyBanchoAPI.BANCHO_MAP_URL, map.getSid());
            String icoUrl = map.getCovers().getListAt2x();
            String iconKookUrl = httpAPI.uploadFile("pic", icoUrl);
            ImageElement icon = new ImageElement(iconKookUrl, "", Size.SM, false);
            ChimuBeatmapInfo lastVersion = map.getBeatmaps().getLast();
            String text = StrUtil.format("[{}-{}]({}) (by:{}) **bpm**:{} **star({})**:{} :star:",
                    map.getArtist(), map.getTitle(), directLink, map.getCreator(), map.getBpm(),
                    lastVersion.getVersion(), lastVersion.getDifficultyRating());
            builder.addModule(new SectionModule(new MarkdownElement(text), icon, Accessory.Mode.LEFT));
            if (index != maps.size() - 1) {
                builder.addModule(DividerModule.INSTANCE);
            }
            index++;
        }
        return builder.build();
    }
}

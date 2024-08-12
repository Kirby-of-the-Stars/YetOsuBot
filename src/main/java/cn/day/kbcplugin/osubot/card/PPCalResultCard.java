package cn.day.kbcplugin.osubot.card;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.model.api.PPResult;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.utils.MapHelper;
import org.dromara.hutool.core.text.StrUtil;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.BaseElement;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.module.ContainerModule;
import snw.jkook.message.component.card.module.DividerModule;
import snw.jkook.message.component.card.module.SectionModule;
import snw.jkook.message.component.card.structure.Paragraph;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PPCalResultCard {

    public static MultipleCardComponent build(List<PPResult> results, IBeatmap beatmap) {
        CardBuilder buildr = new CardBuilder();
        buildr.setTheme(Theme.INFO);
        buildr.setSize(Size.LG);
        byte[] data = APIHandler.INSTANCE.getLegacyBanchoAPI().getMapCover(String.valueOf(beatmap.getSid()));
        if (data != null) {
            String url = Main.instance.getCore().getHttpAPI().uploadFile("1", data);
            buildr.addModule(new ContainerModule(List.of(new ImageElement(url, "1", false))));
        }
        List<BaseElement> fields = new ArrayList<>(3);
        fields.add(new MarkdownElement(StrUtil.format("**{}**\n[{}]", beatmap.getTitle(), beatmap.getVersion())));
        fields.add(new MarkdownElement(StrUtil.format("**MAX PP**\n[{}]", BigDecimal.valueOf(results.getFirst().getMax_pp()).floatValue())));
        fields.add(new MarkdownElement(StrUtil.format("**难度**\n[{}]", beatmap.getTitle(), BigDecimal.valueOf(results.getFirst().getMap_star()).floatValue())));
        buildr.addModule(new SectionModule(new Paragraph(3, fields)));
        fields.clear();//for reuse
        if (results.size() > 1) {
            for (int i = 0; i < results.size(); i++) {
                PPResult r = results.get(i);
                fields.add(new MarkdownElement(StrUtil.format("**{}Acc**\nAimPP:{}", r.getRawMap().acc, r.pp_aim)));
                fields.add(new MarkdownElement(StrUtil.format("\nSpdPP{}", r.pp_speed)));
                fields.add(new MarkdownElement(StrUtil.format("\nAccPP{}", r.pp_acc)));
                buildr.addModule(new SectionModule(new Paragraph(3, fields)));
                if (i != results.size() - 1) {
                    buildr.addModule(DividerModule.INSTANCE);
                }
                fields.clear();
            }
        } else {
            PPResult first = results.getFirst();
            fields.add(new MarkdownElement(StrUtil.format("**{}Acc**\nAimPP:{}", first.getRawMap().acc, first.pp_aim)));
            fields.add(new MarkdownElement(StrUtil.format("**if fc:{}**\nSpdPP{}", first.pp_fc, first.pp_speed)));
            fields.add(new MarkdownElement(StrUtil.format("\nAccPP{}", first.pp_acc)));
        }
        return buildr.build();
    }
}

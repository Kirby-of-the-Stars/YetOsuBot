package cn.day.kbcplugin.osubot.card;

import cn.day.kbcplugin.osubot.api.APIHandler;
import org.dromara.hutool.core.text.StrUtil;
import snw.jkook.message.component.MarkdownComponent;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.BaseElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.DividerModule;
import snw.jkook.message.component.card.module.HeaderModule;
import snw.jkook.message.component.card.module.SectionModule;
import snw.jkook.message.component.card.structure.Paragraph;

import java.util.ArrayList;
import java.util.List;

public class APIInfoCard {

    public static MultipleCardComponent build(){
        CardBuilder buildr = new CardBuilder();
        buildr.setTheme(Theme.INFO);
        buildr.setSize(Size.LG);
        buildr.addModule(new HeaderModule("当前API信息如下:"));
        List<BaseElement> fields = new ArrayList<>(3);
        fields.add(new MarkdownElement(StrUtil.format("**铺面数据API**\n{}", APIHandler.getMapInfoProvider().getName())));
        fields.add(new MarkdownElement(StrUtil.format("**铺面下载API**\n{}", APIHandler.getBeatmapDownLoadProvider().getName())));
        fields.add(new MarkdownElement(StrUtil.format("**铺面BG下载API**\n{}", APIHandler.getBeatMapBGProvider().getName())));
        buildr.addModule(new SectionModule(new Paragraph(3,fields)));
        buildr.addModule(DividerModule.INSTANCE);
        buildr.addModule(new HeaderModule("当前可用的API如下"));
        buildr.addModule(new SectionModule(new MarkdownElement("bancho(v1) ppysb chimu sayo(WIP)")));
        buildr.addModule(DividerModule.INSTANCE);
        buildr.addModule(new SectionModule(new MarkdownElement("使用setapi命令可以设置api源")));
        return buildr.build();
    }
}

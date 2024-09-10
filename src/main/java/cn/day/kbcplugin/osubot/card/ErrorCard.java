package cn.day.kbcplugin.osubot.card;

import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.module.SectionModule;

public class ErrorCard {

    public static MultipleCardComponent build(MarkdownElement ele) {
        CardBuilder builder = new CardBuilder();
        builder.setTheme(Theme.DANGER)
                .setSize(Size.LG);
        builder.addModule(new SectionModule(ele));
        return builder.build();
    }
}

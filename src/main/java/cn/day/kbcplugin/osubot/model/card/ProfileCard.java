package cn.day.kbcplugin.osubot.model.card;

import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import org.dromara.hutool.core.text.StrUtil;
import snw.jkook.message.component.MarkdownComponent;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.BaseElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.module.DividerModule;
import snw.jkook.message.component.card.module.HeaderModule;
import snw.jkook.message.component.card.module.SectionModule;
import snw.jkook.message.component.card.structure.Paragraph;

import java.util.ArrayList;
import java.util.List;

public class ProfileCard {

    public static MultipleCardComponent build(List<UserInfo> userInfos, String name) {
        int i = 0;
        CardBuilder builder = new CardBuilder();
        builder.setTheme(Theme.PRIMARY);
        builder.setSize(Size.LG);
        builder.addModule(new HeaderModule(name + "的账号如下:"));//title
        for (UserInfo userInfo : userInfos) {
            //TODO getImage;
            List<BaseElement> fields = new ArrayList<>();
            //name
            fields.add(new MarkdownElement(StrUtil.format("**用户名**\\n{}", userInfo.getUserName())));
            //server
            fields.add(new MarkdownElement(StrUtil.format("**服务器**\\n{}", userInfo.getServer().getName())));
            //mode
            fields.add(new MarkdownElement(StrUtil.format("**模式**\\n{}", userInfo.getMode().getName())));
            Paragraph paragraph = new Paragraph(3, fields);
            builder.addModule(new SectionModule(paragraph));
            if (i + 1 != userInfos.size()) {
                builder.addModule(DividerModule.INSTANCE);//add diver
            }
        }
        return builder.build();
    }
}

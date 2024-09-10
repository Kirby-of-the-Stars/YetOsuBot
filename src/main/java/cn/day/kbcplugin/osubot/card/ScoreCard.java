package cn.day.kbcplugin.osubot.card;

import cn.day.kbcplugin.osubot.Main;
import com.mybatisflex.core.keygen.impl.FlexIDKeyGenerator;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.module.ContainerModule;

import java.util.Base64;

public class ScoreCard {

    private static final FlexIDKeyGenerator generator = new FlexIDKeyGenerator();

    public static MultipleCardComponent build(String base64) {
        CardBuilder builder = new CardBuilder();
        builder.setTheme(Theme.SUCCESS).setSize(Size.LG);
        byte[] base64File = Base64.getDecoder().decode(base64);
        String fileName = generator.generate(null, null) + "-" + System.currentTimeMillis() + ".png";
        String url = Main.instance.getCore().getHttpAPI().uploadFile(fileName, base64File);
        builder.addModule(new ContainerModule.Builder()
                .add(new ImageElement(url, null, Size.LG, true)).build());
        return builder.build();
    }
}

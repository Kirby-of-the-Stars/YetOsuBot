package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.api.ChimuAPI;
import cn.day.kbcplugin.osubot.card.SearchMapCard;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.chimu.ChimuBeatmap;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Command(name = "search")
@Prefix("/")
@Description("搜图,用法:/search mode 关键字")
public class SearchMap {

    private static final Log logger = LogFactory.getLog("[Search Command]");
    private static final int Limit = 10;

    @Execute(name = "osu")
    public void searchOsu(
            @Context Message message,
            @Arg("keywords") String[] keywords
    ) {
        search(OsuModeEnum.STANDER, message, keywords);
    }

    @Execute(name = "taiko")
    public void searchTaiko(
            @Context Message message,
            @Arg String[] keywords
    ) {
        search(OsuModeEnum.TAIKO, message, keywords);
    }

    @Execute(name = "catch")
    public void searchCatch(
            @Context Message message,
            @Arg String[] keywords
    ) {
        search(OsuModeEnum.CATCH, message, keywords);
    }

    @Execute(name = "mania")
    public void searchMania(
            @Context Message message,
            @Arg String[] keywords
    ) {
        search(OsuModeEnum.MANIA, message, keywords);
    }

    private void search(OsuModeEnum mode, Message message, String[] keywords) {
        try {
            List<String> words = new ArrayList<>(Arrays.asList(keywords));
            StringBuilder keyword = new StringBuilder();
            for (String s : words) {
                keyword.append(s);
                keyword.append(" ");
            }
            keyword.deleteCharAt(keyword.length() - 1);//除去最后一个空格
            message.sendReaction(Main.instance.getCore().getUnsafe().getEmoji("✅"));
            ChimuAPI chimuAPI = APIHandler.INSTANCE.getChimuAPI();
            List<ChimuBeatmap> result = chimuAPI.searchBeatmap(keyword.toString(), Limit, mode);
            MultipleCardComponent card = SearchMapCard.build(result);
            message.reply(card);
        } catch (Exception e) {
            message.reply("指令执行失败");
            logger.error("意外异常:{}", e.getLocalizedMessage(), e);
        }
    }
}

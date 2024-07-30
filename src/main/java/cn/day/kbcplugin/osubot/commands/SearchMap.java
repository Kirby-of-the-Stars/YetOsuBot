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
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Command(name = "search")
@Prefix("/")
@Description("搜图,用法:/search 关键字,如果加上-mode则取最后一个单词作为mode")
public class SearchMap {

    private static final Log logger = LogFactory.getLog("[Search Command]");
    private static final int Limit = 10;

    @Execute
    public void search(
            @Context CommandSender commandSender,
            @Context Message message,
            @Flag("-mode") boolean useMode,
            @Arg String[] keywords
    ) {
        if (commandSender instanceof User sender) {
            try {
                List<String> words = new ArrayList<>(Arrays.asList(keywords));
                useMode = words.contains("-mode");
                String modeStr = null;
                if (useMode) {
                    words.remove("-mode");
                    modeStr = words.getLast();
                    words.remove(modeStr);
                }
                OsuModeEnum mode = null;
                if (modeStr != null) {
                    mode = OsuModeEnum.fromName(modeStr);
                    if (mode == null || mode.index > 3) {
                        message.reply("无效的mode,可用 osu taiko catch mania");
                        return;
                    }
                }
                StringBuilder keyword = new StringBuilder();
                for(String s:words){
                    keyword.append(s);
                    keyword.append(" ");
                }
                keyword.deleteCharAt(keyword.length() - 1);//除去最后一个空格
                message.sendReaction(Main.instance.getCore().getUnsafe().getEmoji("✅"));
                ChimuAPI chimuAPI = APIHandler.getChimuAPI();
                List<ChimuBeatmap> result = chimuAPI.searchBeatmap(keyword.toString(), Limit, mode);
                MultipleCardComponent card = SearchMapCard.build(result);
                message.reply(card);
            } catch (Exception e) {
                message.reply("指令执行失败");
                logger.error("意外异常:{}", e.getLocalizedMessage(), e);
            }
        }
    }
}

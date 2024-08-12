package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.card.PPCalResultCard;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.PPResult;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.Mods;
import cn.day.kbcplugin.osubot.utils.ScoreUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import java.math.BigDecimal;
import java.util.List;

@Command(name = "pp")
@Prefix("/")
@Description("计算某个图的pp,用法:/pp mode 难度id [mods] [acc] [miss] 例如/pp osu 11451 RXHDDT 99.33 1")
public class PPCal {

    private static final Log logger = LogFactory.getLog("[PP Command]");


    @Execute(name = "osu")
    public void CalOsu(
            @Context Message message,
            @Arg String bid,
            @OptionalArg String mods,
            @OptionalArg String acc,
            @OptionalArg String miss
    ) {
        this.CalPP(OsuModeEnum.STANDER, message, bid, mods, acc, miss);
    }

    @Execute(name = "taiko")
    public void CalTK(
            @Context Message message,
            @Arg String bid,
            @OptionalArg String mods,
            @OptionalArg String acc,
            @OptionalArg String miss
    ) {
        this.CalPP(OsuModeEnum.TAIKO, message, bid, mods, acc, miss);
    }

    @Execute(name = "catch")
    public void CalCTB(
            @Context Message message,
            @Arg String bid,
            @OptionalArg String mods,
            @OptionalArg String acc,
            @OptionalArg String miss
    ) {
        this.CalPP(OsuModeEnum.CATCH, message, bid, mods, acc, miss);
    }

    @Execute(name = "mania")
    public void CalMania(
            @Context Message message,
            @Arg String bid,
            @OptionalArg String mods,
            @OptionalArg String acc,
            @OptionalArg String miss
    ) {
        this.CalPP(OsuModeEnum.MANIA, message, bid, mods, acc, miss);
    }

    private void CalPP(OsuModeEnum mode, Message message, String bid, String modsStr, String accStr, String missStr) {
        if (!mode.equals(OsuModeEnum.STANDER)) {
            message.reply("其它模式还在研究中~");
            return;
        }
        Integer mods = modsStr == null ? 0 : Mods.modeInt(modsStr);
        BigDecimal acc = BigDecimal.valueOf(100);
        Integer miss = null;
        if (accStr != null) {
            try {
                acc = new BigDecimal(accStr);
                miss = Integer.parseInt(missStr);
            } catch (NumberFormatException e) {
                message.reply("请输入正确的数字");
                return;
            }
        }
        IBeatmap beatmap = null;
        try {
            beatmap = APIHandler.INSTANCE.getMapInfoProvider().getBeatmap(bid, null);
            if (beatmap == null) {
                message.reply("无法查询铺面数据,请确认铺面id是否正确");
                return;
            }
        } catch (Exception e) {
            message.reply("获取铺面数据失败");
            logger.error("无法获取铺面数据:{}", e.getLocalizedMessage(), e);
            return;
        }
        try {
            List<PPResult> results = ScoreUtil.calPPFromMap(beatmap, accStr == null ? null : acc.floatValue(), mods, miss);
            if (results == null) {
                message.reply("计算程序出错,请联系管理员");
                return;
            }
            MultipleCardComponent card = PPCalResultCard.build(results, beatmap);
            message.reply(card);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            message.reply("发送失败");
        }
    }
}

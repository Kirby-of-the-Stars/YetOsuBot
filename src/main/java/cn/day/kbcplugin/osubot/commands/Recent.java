package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.card.ScoreCard;
import cn.day.kbcplugin.osubot.db.service.UserInfoServiceImpl;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import cn.day.kbcplugin.osubot.model.entity.Account;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import cn.day.kbcplugin.osubot.utils.ImgUtil;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.inject.Inject;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "recent", aliases = "pr")
@Prefix("/")
@Description("获取最近24h内的成绩 用法:/pr [mode] [server] mode server为可选")
public class Recent {

    //getCore().getUnsafe().getEmoji("✅");

    private static final Log logger = LogFactory.getLog("[Recent Command]");
    private final UserInfoServiceImpl userInfoService;

    @Inject
    public Recent(UserInfoServiceImpl userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Execute
    public void recentScore(
            @Context Account account,
            @Context Message message,
            @OptionalArg("mode") OsuModeEnum modeArg,
            @OptionalArg("server") ServerEnum serverArg,
            @Flag("-lazer") boolean lazerMode
    ) {
        MultipleCardComponent card = null;
        try {
            OsuModeEnum mode = modeArg == null ? account.getPreferredMode() : modeArg;
            ServerEnum server = serverArg == null ? account.getPreferredServer() : serverArg;
            message.sendReaction(Main.instance.getCore().getUnsafe().getEmoji("✅"));
            //query database
            UserInfo dbInfo = userInfoService.queryOsuUserInfo(account.getKookId(), server);
            if (dbInfo == null) {
                message.reply("你在该服务器下没有账号，请先进行绑定!");
                return;
            }
            String osuId = dbInfo.getOsuId();
            IUserInfo userInfo = APIHandler.INSTANCE.getAPI(server).getUserInfo(osuId, mode);
            if (userInfo == null) {
                message.reply(StrUtil.format("无法查询到osuId:{}在{}下的数据", osuId, server.getName()));
                return;
            }
            IScore score;
            if (lazerMode) {
                //TODO lazer score;
                message.reply("Lazer 成绩施工中....");
                return;
            } else {
                //Legacy
                score = APIHandler.INSTANCE.getAPI(server).getRecentScore(osuId, mode);
            }
            if (score == null) {
                message.reply("无法查询到成绩,可能是API抽风了");
                return;
            }
            IBeatmap beatmap = APIHandler.INSTANCE.getMapInfoProvider().getBeatmap(score.beatmapId().toString(), null);
            if (beatmap == null) {
                message.reply(StrUtil.format("获取bid:{}铺面信息失败", score.beatmapId().toString()));
                return;
            }
            String base64;
            try {
                base64 = ImgUtil.drawResult(userInfo.getUserName(), score, beatmap, mode.index);
            } catch (Exception e) {
                logger.error("绘图失败:{}", e.getLocalizedMessage(), e);
                message.reply("绘图失败了:" + e.getLocalizedMessage());
                return;
            }
            card = ScoreCard.build(base64);
            message.reply(card);
        } catch (Exception e) {
            message.reply("无法发出成绩,可能是被Kook拦截了");
            logger.warn("打印成绩失败:{}", e.getLocalizedMessage(), e);
            if (card != null) {
                logger.warn("报错的卡片json:{}", snw.kookbc.impl.entity.builder.CardBuilder.serialize(card));
            }
        }
    }
}

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
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import java.util.List;

@Command(name = "best", aliases = {"bp"})
@Prefix("/")
@Description("查询自己的bp,用法/bp #N [server] [mode],例如/bp #1 或/bp #1 bancho osu")
public class Best {
    private static final Log logger = LogFactory.getLog("[Best Command]");
    private static final int MAX_LIMIT = 100;
    private final UserInfoServiceImpl userInfoService;

    @Inject
    public Best(UserInfoServiceImpl userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Execute
    public void getBest(
            @Context User sender,
            @Context Message message,
            @Context Account account,
            @Arg("N") Integer index,
            @OptionalArg("server") ServerEnum serverArg,
            @OptionalArg("mode") OsuModeEnum modeArg
    ) {
        try {
            if (index > MAX_LIMIT) {
                message.reply("bp数字太大了,往前100查查吧");
                return;
            }
            String kookId = sender.getId();
            OsuModeEnum mode = modeArg == null ? account.getPreferredMode() : modeArg;
            ServerEnum server = serverArg == null ? account.getPreferredServer() : serverArg;
            message.sendReaction(Main.instance.getCore().getUnsafe().getEmoji("✅"));
            UserInfo dbUserInfo = userInfoService.queryOsuUserInfo(kookId, server);
            if (dbUserInfo == null) {
                message.reply("你还未在该服务器下绑定osu账号,请使用/bind 绑定");
                return;
            }
            String osuId = dbUserInfo.getOsuId();
            IUserInfo userInfo = APIHandler.INSTANCE.getAPI(server).getUserInfo(osuId, mode);
            if (userInfo == null) {
                message.reply("无法获取用户信息");
                return;
            }
            List<? extends IScore> scores = APIHandler.INSTANCE.getAPI(server).getTopNScores(osuId, mode, index);
            if (scores == null) {
                message.reply("无法获取到成绩数据,API异常");
                return;
            }
            if (scores.isEmpty()) {
                message.reply("你最近好像都没有游玩，去打一把吧");
            }
            IScore score = scores.getLast();
            IBeatmap beatmap = APIHandler.INSTANCE.getMapInfoProvider().getBeatmap(String.valueOf(score.beatmapId()), null);
            if (beatmap == null) {
                message.reply(StrUtil.format("无法查询到bid为{}的铺面信息", score.beatmapId()));
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
            message.reply(ScoreCard.build(base64));
        } catch (Exception e) {
            logger.warn("意外异常:{}", e.getLocalizedMessage(), e);
            message.reply("发送消息失败");
        }
    }
}

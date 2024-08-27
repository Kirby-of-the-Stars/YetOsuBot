package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import cn.day.kbcplugin.osubot.model.entity.Account;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import cn.day.kbcplugin.osubot.utils.ImgUtil;
import com.mybatisflex.core.query.QueryChain;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.inject.Inject;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.module.ContainerModule;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import java.util.Base64;
import java.util.List;

import static cn.day.kbcplugin.osubot.model.entity.table.UserInfoTableDef.USER_INFO;

@Command(name = "recent", aliases = "pr")
@Prefix("/")
@Description("获取最近24h内的成绩 用法:/pr [mode] [server] mode server为可选")
public class Recent {

    //getCore().getUnsafe().getEmoji("✅");

    private final UserInfoMapper userInfoMapper;

    private static final Log logger = LogFactory.getLog("[Recent Command]");

    @Inject
    public Recent(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
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
            //query
            List<UserInfo> dbUserInfos = QueryChain.of(userInfoMapper)
                    .select()
                    .from(USER_INFO)
                    .where(USER_INFO.KOOK_ID.eq(account.getKookId()).and(USER_INFO.SERVER.eq(server)))
                    .list();
            if (dbUserInfos == null || dbUserInfos.isEmpty()) {
                message.reply("你还未绑定任何osu账号,请使用/bind 绑定");
                return;
            }
            String osuId;
            IUserInfo userInfo;
            try {
                osuId = dbUserInfos.getFirst().getOsuId();
                userInfo = APIHandler.INSTANCE.getAPI(server).getUserInfo(osuId, mode);
                if (userInfo == null) {
                    message.reply("osu账号数据不存在");
                    return;
                }
            } catch (Exception e) {
                logger.warn("获取用户信息流程失败:{}", e.getLocalizedMessage(), e);
                message.reply("无法获取用户信息");
                return;
            }
            IScore score = null;
            if (!lazerMode) {
                try {
                    score = APIHandler.INSTANCE.getAPI(server).getRecentScore(osuId, mode);
                } catch (Exception e) {
                    logger.warn("获取用户成绩流程失败:{}", e.getLocalizedMessage(), e);
                    message.reply("无法获取成绩数据");
                    return;
                }
            } else {
                //TODO lazer score;
                message.reply("Lazer 成绩施工中....");
                return;
            }
            if (score == null) {
                message.reply("无法查询到成绩");
                return;
            }
            IBeatmap beatmap;
            try {
                beatmap = APIHandler.INSTANCE.getMapInfoProvider().getBeatmap(String.valueOf(score.beatmapId()), null);
            } catch (Exception e) {
                message.reply("获取地图信息失败");
                logger.warn("获取铺面数据流程失败:{}", e.getLocalizedMessage(), e);
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
            if (base64 == null) {
                message.reply("绘图失败了,没有任何结果");
                return;
            }
            CardBuilder builder = new CardBuilder();
            builder.setTheme(Theme.SUCCESS).setSize(Size.LG);
            byte[] base64File = Base64.getDecoder().decode(base64);
            String fileName = message.getId() + "recent" + "-" + System.currentTimeMillis() + ".png";
            String url = Main.instance.getCore().getHttpAPI().uploadFile(fileName, base64File);
            builder.addModule(new ContainerModule.Builder()
                    .add(new ImageElement(url, null, Size.LG, true)).build());
            card = builder.build();
            message.reply(card);
        } catch (Exception e) {
            message.reply("无法发出成绩,可能是被Kook拦截了");
            logger.warn("打印成绩失败:{}", e.getLocalizedMessage(), e);
            if(card != null) {
                logger.warn("报错的卡片json:{}", snw.kookbc.impl.entity.builder.CardBuilder.serialize(card));
            }
        }
    }
}

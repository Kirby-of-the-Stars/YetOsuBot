package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.dao.AccountMapper;
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
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.module.ContainerModule;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import java.util.Base64;
import java.util.List;

import static cn.day.kbcplugin.osubot.model.entity.table.UserInfoTableDef.USER_INFO;

@Command(name = "best", aliases = {"bp"})
@Prefix("/")
@Description("查询自己的bp,用法/bp #N [server] [mode],例如/bp #1 或/bp #1 bancho osu")
public class Best {
    private static final Log logger = LogFactory.getLog("[Best Command]");

    private final AccountMapper accountMapper;
    private final UserInfoMapper userInfoMapper;

    public Best(AccountMapper accountMapper, UserInfoMapper userInfoMapper) {
        this.accountMapper = accountMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void getBest(
            @Context CommandSender commandSender,
            @Context Message message,
            @Arg String indexStr,
            @OptionalArg String serverStr,
            @OptionalArg String modeStr
    ) {
        if (commandSender instanceof User sender) {
            try {
                String kookId = sender.getId();
                Account account = accountMapper.selectOneById(kookId);
                if (account == null) {
                    message.reply("你还未绑定任何osu账号,请使用/bind 绑定");
                    return;
                }
                OsuModeEnum mode = account.getPreferredMode();
                ServerEnum server = account.getPreferredServer();
                if (modeStr != null) {
                    OsuModeEnum argMode = OsuModeEnum.fromName(modeStr);
                    if (argMode == null) {
                        message.reply("无效的mode名字,可选的mode名:" + OsuModeEnum.AllNames());
                        return;
                    } else {
                        mode = argMode;
                    }
                }
                if (serverStr != null) {
                    ServerEnum argServer = ServerEnum.fromName(serverStr);
                    if (argServer == null) {
                        message.reply("无效的mode名字,可选的mode名:" + OsuModeEnum.AllNames());
                        return;
                    } else {
                        server = argServer;
                    }
                }
                if (!indexStr.startsWith("#")) {
                    message.reply("请输入正确的参数,例如/bp #1");
                    return;
                }
                indexStr = indexStr.replace("#", "");
                int index = Integer.parseInt(indexStr);
                if (index <= 0 || index > 20) {
                    message.reply("范围太大了呀,可选范围1~20");
                    return;
                }
                message.sendReaction(Main.instance.getCore().getUnsafe().getEmoji("✅"));
                List<UserInfo> dbUserInfos = QueryChain.of(userInfoMapper)
                        .select()
                        .from(USER_INFO)
                        .where(USER_INFO.KOOK_ID.eq(kookId).and(USER_INFO.SERVER.eq(server)))
                        .list();
                if (dbUserInfos == null || dbUserInfos.isEmpty()) {
                    message.reply("你还未绑定任何osu账号,请使用/bind 绑定");
                    return;
                }
                String osuId;
                IUserInfo userInfo;
                try {
                    osuId = dbUserInfos.getFirst().getOsuId();
                    userInfo = APIHandler.getAPI(server).getUserInfo(osuId, mode);
                    if (userInfo == null) {
                        message.reply("osu账号数据不存在");
                        return;
                    }
                } catch (Exception e) {
                    logger.warn("获取用户信息流程失败:{}", e.getLocalizedMessage(), e);
                    message.reply("无法获取用户信息");
                    return;
                }
                List<? extends IScore> scores;
                try {
                    scores = APIHandler.getAPI(server).getTopNScores(osuId, mode, index);
                    if (scores == null || scores.isEmpty()) {
                        message.reply("该账号下没有任何成绩哦~");
                        return;
                    }
                } catch (Exception e) {
                    logger.warn("无法获取到成绩数据:{}", e.getLocalizedMessage(), e);
                    message.reply("无法获取到成绩数据");
                    return;
                }
                IScore score = scores.getLast();
                IBeatmap beatmap;
                try {
                    beatmap = APIHandler.getMapInfoProvider().getBeatmap(String.valueOf(score.beatmapId()), null);
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
                message.reply(builder.build());
            } catch (NumberFormatException e) {
                message.reply("请输入正确的参数,例如/bp #1");
            } catch (Exception e) {
                logger.warn("Kook发送消息失败:{}", e.getLocalizedMessage(), e);
                message.reply("发送消息失败");
            }
        }
    }
}

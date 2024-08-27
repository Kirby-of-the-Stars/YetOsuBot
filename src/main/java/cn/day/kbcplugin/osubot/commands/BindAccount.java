package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import cn.day.kbcplugin.osubot.model.entity.Account;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.row.Db;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

import static cn.day.kbcplugin.osubot.model.entity.table.UserInfoTableDef.USER_INFO;

@Command(name = "bind")
@Prefix("/")
@Description(value = "绑定osu账号 用法:/bind osuId [server] [preferMode] server为需要绑定的服务器,默认为bancho,preferMode为偏好模式,默认为std")
public class BindAccount {

    private static final Log logger = LogFactory.getLog("[BindCommand]");

    private final AccountMapper accountMapper;
    private final UserInfoMapper userInfoMapper;

    @Inject
    public BindAccount(AccountMapper accountMapper, UserInfoMapper userInfoMapper) {
        this.accountMapper = accountMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void BindKookUser(
            @Context User sender,
            @Context Message message,
            @Arg("osuId") String osuId,
            @OptionalArg("sever") ServerEnum serverArg,
            @OptionalArg("mode") OsuModeEnum modeArg
    ) {
        try {
            final String kookId = sender.getId();
            OsuModeEnum mode = modeArg == null ? OsuModeEnum.STANDER : modeArg;
            ServerEnum server = serverArg == null ? ServerEnum.Bancho : serverArg;
            if (server.equals(ServerEnum.Bancho) && mode.index > OsuModeEnum.MANIA.index) {
                message.reply("Bancho下只支持std mania ctb taiko");
                return;
            }
            UserInfo dbInfo = QueryChain.of(userInfoMapper)
                    .select()
                    .from(USER_INFO)
                    .where(USER_INFO.OSU_ID.eq(osuId).and(USER_INFO.SERVER.eq(server)))
                    .one();
            if (dbInfo != null) {
                message.reply("该osu账号已被绑定");
                return;
            }
            boolean firstBind = accountMapper.selectOneById(sender.getId()) == null;
            IUserInfo userInfo;
            try {
                userInfo = APIHandler.INSTANCE.getAPI(server).getUserInfo(osuId, mode);
                if (userInfo == null) {
                    message.reply("无法查询到Osu账号信息");
                    return;
                }
            } catch (Exception e) {
                message.reply("无法查询到Osu账号信息");
                logger.warn("获取账号流程出错:{}", e.getLocalizedMessage(), e);
                return;
            }
            boolean flag = Db.tx(() -> {
                int res = 1;
                if (firstBind) {
                    Account newAccount = new Account(kookId, mode, server);
                    res = accountMapper.insert(newAccount);
                }
                res += userInfoMapper.insert(new UserInfo(userInfo, kookId, server, mode));
                return res == 2;
            });
            if (flag) message.reply("绑定成功");
            else message.reply("绑定失败,请联系管理员查看后台");
        } catch (Exception e) {
            message.reply("发送消息失败,可能是kook爆了");
            logger.error("插件遇到意外错误:{}" + e.getLocalizedMessage(), e);
        }
    }
}

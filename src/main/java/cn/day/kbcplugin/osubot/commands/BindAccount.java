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
@Description(value = "绑定osu账号 用法:/bind osuId [server] [preferMode]")
public class BindAccount {

    public static final String HELP_TEXT = "/bind osuId [server] [preferMode]  例如:/bind 144 server和mode默认为bancho 和 std,server可选:" + ServerEnum.AllNames() + "preferMode可选:" + OsuModeEnum.AllNames();
    private static final Log logger = LogFactory.getLog("[BindCommand]");

    private final AccountMapper accountMapper;
    private final UserInfoMapper userInfoMapper;

    public BindAccount(AccountMapper accountMapper, UserInfoMapper userInfoMapper) {
        this.accountMapper = accountMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void BindKookUser(
            @Context CommandSender commandSender,
            @Context Message message,
            @Arg String osuId,
            @OptionalArg String serverStr,
            @OptionalArg String modeStr
    ) {
        if (commandSender instanceof User sender) {
            try {
                String kookId = sender.getId();
                Account account = accountMapper.selectOneById(kookId);
                boolean firstBind = account==null;
                ServerEnum server = ServerEnum.Bancho;
                if (serverStr != null) {
                    server = ServerEnum.fromName(serverStr);
                    if (server == null) {
                        message.reply("server名称错误,可选server:" + ServerEnum.AllNames());
                        return;
                    }
                }
                OsuModeEnum mode = OsuModeEnum.STANDER;
                if (modeStr != null) {
                    mode = OsuModeEnum.fromName(modeStr);
                    if (mode == null) {
                        message.reply("mode名称错误,可选mode:" + OsuModeEnum.AllNames());
                        return;
                    }
                    if (server.equals(ServerEnum.Bancho)) {
                        if (mode.index > OsuModeEnum.MANIA.index) {
                            message.reply("Bancho下只支持std mania ctb taiko");
                            return;
                        }
                    }
                }
                IUserInfo userInfo;
                try {
                   userInfo = APIHandler.INSTANCE.getAPI(server).getUserInfo(osuId, mode);
                    if (userInfo == null) {
                        message.reply("无法查询到Osu账号信息");
                        return;
                    }
                }catch (Exception e){
                    message.reply("无法查询到Osu账号信息");
                    logger.warn("获取账号流程出错:{}",e.getLocalizedMessage(),e);
                    return;
                }
                //mulit userinfo
                UserInfo dbInfo = QueryChain.of(userInfoMapper)
                        .select()
                        .from(USER_INFO)
                        .where(USER_INFO.OSU_ID.eq(osuId).and(USER_INFO.SERVER.eq(server)))
                        .one();
                if(dbInfo!=null){
                    message.reply("该osu账号已被绑定");
                    return;
                }
                dbInfo = new UserInfo(userInfo, kookId, server,mode);
                OsuModeEnum finalMode = mode;
                ServerEnum finalServer = server;
                UserInfo finalDbInfo = dbInfo;
                boolean flag = Db.tx(() -> {
                    int res = 1;
                    if(firstBind){
                        Account newAccount = new Account(sender.getId(), finalMode, finalServer);
                        res = accountMapper.insert(newAccount);
                    }
                    res += userInfoMapper.insert(finalDbInfo);
                    return res == 2;
                });
                if (!flag) {
                    message.reply("数据库操作失败,请查看后台");
                } else {
                    message.reply("绑定成功");
                }
            } catch (Exception e) {
                message.reply("插件遇到意外错误:" + e.getLocalizedMessage());
                logger.error("插件遇到意外错误:{}" + e.getLocalizedMessage(), e);
            }
        }
    }
}

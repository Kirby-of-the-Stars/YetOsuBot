package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.model.entity.Account;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
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

@Command(name = "unbind")
@Prefix("/")
@Description("删除账号,用法 /bind [osuId] |带上osuId时，不会删除数据,否则删除所有数据")
public class UnBind {

    private final AccountMapper accountMapper;
    private final UserInfoMapper userInfoMapper;

    private static final Log logger = LogFactory.getLog("[Unbind Command]");

    public UnBind(AccountMapper accountMapper, UserInfoMapper userInfoMapper) {
        this.accountMapper = accountMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void unbindAccount(@Context CommandSender commandSender, @Context Message message, @OptionalArg String osuId) {
        if (commandSender instanceof User sender) {
            String kookId = sender.getId();
            Account account = accountMapper.selectOneById(kookId);
            try {
                if (account == null) {
                    message.reply("你还未绑定,请使用/bind绑定");
                    return;
                }
                boolean s = Db.tx(() -> {
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq(UserInfo::getKookId, kookId)
                            .eq(UserInfo::getOsuId, osuId, osuId != null);
                    userInfoMapper.deleteByQuery(wrapper);
                    if (osuId == null) {
                        int i = accountMapper.deleteById(kookId);
                        return i == 1;
                    }
                    return true;
                });
                if (s) {
                    message.reply("解绑成功");
                } else {
                    message.reply("数据库操作失败,请查看后台");
                }
            }catch (Exception e){
                logger.warn("Kook发送消息失败:{}",e.getLocalizedMessage(),e);
                message.reply("无法发送消息");
            }
        }
    }
}

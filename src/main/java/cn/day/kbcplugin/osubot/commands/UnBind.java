package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.db.dao.AccountMapper;
import cn.day.kbcplugin.osubot.db.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "unbind")
@Prefix("/")
@Description("删除账号,用法 /unbind [osuId] 删除指定osuId下的数据")
public class UnBind {

    private static final Log logger = LogFactory.getLog("[Unbind Command]");
    private final AccountMapper accountMapper;
    private final UserInfoMapper userInfoMapper;

    @Inject
    public UnBind(AccountMapper accountMapper, UserInfoMapper userInfoMapper) {
        this.accountMapper = accountMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void unbindAccount(
            @Context User sender,
            @Context Message message,
            @OptionalArg("osuId") String osuId) {
        try {
            String kookId = sender.getId();
            boolean delAll = osuId == null;
            boolean s = Db.tx(() -> {
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq(UserInfo::getKookId, kookId)
                        .eq(UserInfo::getOsuId, osuId, osuId != null);
                userInfoMapper.deleteByQuery(wrapper);
                if (delAll) {
                    int i = accountMapper.deleteById(kookId);
                    return i == 1;
                }
                return true;
            });
            if (s) message.reply("解绑成功");
            else message.reply("数据库操作失败,请查看后台");
        } catch (Exception e) {
            logger.warn("Kook发送消息失败:{}", e.getLocalizedMessage(), e);
            message.reply("无法发送消息");
        }
    }
    //TODO unbind all
}

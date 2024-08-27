package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.card.ProfileCard;
import cn.day.kbcplugin.osubot.model.entity.Account;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import com.mybatisflex.core.query.QueryChain;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;
import snw.kookbc.impl.entity.builder.CardBuilder;

import java.util.List;

import static cn.day.kbcplugin.osubot.model.entity.table.UserInfoTableDef.USER_INFO;

@Command(name = "profile", aliases = "pf")
@Prefix("/")
@Description("查询当前kook下的osu账号,用法/profile 或者/pf")
public class Profile {

    private final UserInfoMapper userInfoMapper;
    private static final Log logger = LogFactory.getLog("[Profile Command]");

    @Inject
    public Profile(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void infoMe(
            @Context User sender,
            @Context Message message) {
        MultipleCardComponent card = null;
        try {
            List<UserInfo> list = QueryChain.of(userInfoMapper)
                    .select()
                    .from(USER_INFO)
                    .where(USER_INFO.KOOK_ID.eq(sender.getId()))
                    .list();
            card = ProfileCard.build(list, sender.getName());
            message.reply(card);
        } catch (Exception e) {
            logger.warn("Kook发送消息失败:{}", e.getLocalizedMessage(), e);
            if (card != null) {
                logger.warn("报错的卡片json:{}", CardBuilder.serialize(card));
            }
            message.reply("发送消息失败");
        }
    }
}

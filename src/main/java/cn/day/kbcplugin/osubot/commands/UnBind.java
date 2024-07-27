package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.model.entity.Account;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "unbind")
@Prefix("/")
public class UnBind {

    private final AccountMapper accountMapper;
    private final UserInfoMapper userInfoMapper;

    public UnBind(AccountMapper accountMapper,UserInfoMapper userInfoMapper) {
        this.accountMapper = accountMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Execute
    public void unbindAccount(@Context CommandSender commandSender, @Context Message message) {
        if (commandSender instanceof User sender) {
            String kookId = sender.getId();
            Account account = accountMapper.selectOneById(kookId);
            if (account == null) {
                message.reply("你还未绑定,请使用/bind绑定");
                return;
            }
            int i = accountMapper.deleteById(kookId);
            if (i != 1) {
                message.reply("数据库操作失败,请查看后台");
            } else {
                message.reply("解绑成功");
            }
        }
    }
}

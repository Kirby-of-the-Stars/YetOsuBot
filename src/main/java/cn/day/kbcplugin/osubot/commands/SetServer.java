package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.entity.Account;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "setserver")
@Prefix("/")
public class SetServer {

    private final AccountMapper accountMapper;

    public SetServer(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Execute
    public void setPreferServer(
            @Context CommandSender commandSender,
            @Context Message message,
            @Arg String serverName
    ) {
        if (commandSender instanceof User sender) {
            String kookId = sender.getId();
            Account account = accountMapper.selectOneById(kookId);
            if (account == null) {
                message.reply("还未绑定,请先使用/bind 绑定");
                return;
            }
            ServerEnum server = ServerEnum.fromName(serverName);
            if (server == null) {
                message.reply("无效的服务器名字,可使用的服务器名:" + ServerEnum.AllNames());
                return;
            }
            account.setPreferredServer(server);
            boolean reSetMode = false;
            if (server.equals(ServerEnum.Bancho)) {
                if (account.getPreferredMode().index > OsuModeEnum.MANIA.index) {
                    account.setPreferredMode(OsuModeEnum.STANDER);
                    reSetMode = true;
                }
            }
            int i = accountMapper.update(account, true);
            if (i != 1) {
                message.reply("数据库操作失败,请查看后台");
            } else {
                if (reSetMode) {
                    message.reply("修改成功,默认mode因为服务器不兼容已被修改为std");
                } else {
                    message.reply("修改成功");
                }
            }

        }
    }
}

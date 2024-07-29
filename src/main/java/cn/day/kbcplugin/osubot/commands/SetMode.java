package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.entity.Account;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "setmode")
@Prefix("/")
@Description("设置默认模式,用法/setmode mode,例如/setmode std_rx")
public class SetMode {

    private final AccountMapper accountMapper;

    public SetMode(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Execute
    public void setPreferMode(
            @Context CommandSender commandSender,
            @Context Message message,
            @Arg String modeName
    ) {
        if (commandSender instanceof User sender) {
            String kookId = sender.getId();
            Account account = accountMapper.selectOneById(kookId);
            if (account == null) {
                message.reply("还未绑定,请先使用/bind 绑定");
                return;
            }
            OsuModeEnum mode = OsuModeEnum.fromName(modeName);
            if (mode == null) {
                message.reply("无效的mode名字,可使用的mode名:" + OsuModeEnum.AllNames());
                return;
            }
            if (account.getPreferredMode().index > OsuModeEnum.MANIA.index) {
                message.reply("当前服务器不兼容该mode");
                return;
            }
            account.setPreferredMode(mode);
            int i = accountMapper.update(account, true);
            if (i != 1) {
                message.reply("数据库操作失败,请查看后台");
            } else {
                message.reply("修改成功");
            }
        }
    }
}

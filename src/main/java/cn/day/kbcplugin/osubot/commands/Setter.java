package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.entity.Account;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.inject.Inject;
import org.dromara.hutool.log.LogUtil;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "set")
@Prefix("/")
@Description("设置根命令,用于设置默认模式、服务器")
public class Setter {

    private final AccountMapper accountMapper;

    @Inject
    public Setter(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }


    @Execute(name = "mode")
    @Description("设置默认偏好模式,例子:/set mode 模式")
    public void setMode(
            @Context Account account,
            @Context Message message,
            @Arg OsuModeEnum mode
    ){
        try{
            if (account.getPreferredMode().index > OsuModeEnum.MANIA.index) {
                message.reply("当前服务器不兼容该mode");
                return;
            }
            account.setPreferredMode(mode);
            if (accountMapper.update(account, true)==1) {
                message.reply("数据库操作失败,请查看后台");
            } else {
                message.reply("修改成功");
            }
        }catch (Exception e){
            LogUtil.warn("Kook发送消息失败:{}", e.getLocalizedMessage(), e);
            message.reply("无法发送消息");
        }
    }

    @Execute(name = "server")
    @Description("设置默认偏好服务器,例子:/set server 服务器 目前支持ppysb和bancho")
    public void setServer(
            @Context Account account,
            @Context Message message,
            @Arg ServerEnum server
            ){
        try{
            account.setPreferredServer(server);
            OsuModeEnum mode = account.getPreferredMode();
            boolean reSetMode = server.equals(ServerEnum.Bancho) && (mode.index > OsuModeEnum.MANIA.index);
            if (reSetMode) {
                account.setPreferredMode(OsuModeEnum.STANDER);
            }
            if(accountMapper.update(account, true)==1) {
                if(reSetMode) message.reply("修改成功,默认mode因为服务器不兼容已被修改为std");
                else message.reply("修改成功");
            }else {
                message.reply("数据库操作失败,请查看后台");
            }
        }catch (Exception e){
            LogUtil.warn("Kook发送消息失败:{}", e.getLocalizedMessage(), e);
            message.reply("无法发送消息");
        }
    }
}

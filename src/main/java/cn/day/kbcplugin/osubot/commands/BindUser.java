package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.AutoAPI;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.pojo.Account;
import cn.day.kbcplugin.osubot.pojo.PluginUser;
import cn.day.kbcplugin.osubot.pojo.base.Response;
import cn.day.kbcplugin.osubot.pojo.common.AbstractUserInfo;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class BindUser implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (message == null) return;
        if (arguments.length < 1) {
            message.reply("参数错误:至少需要一个参数");
            return;
        }
        String _osuId = (String) arguments[0];
        if (_osuId == null) {
            message.reply("参数错误:osuid为空");
            return;
        }
        int osuId;
        try {
            osuId = Integer.parseInt(_osuId);
        } catch (NumberFormatException e) {
            message.reply("请输入正确的id格式");
            return;
        }
        ServerEnum serverEnum = ServerEnum.Bancho;
        int mode = 0;
        boolean resetMode = false;
        if (arguments.length >= 3) {
            String Server = (String) arguments[1];
            if (Server.equals("ppysb")) {
                serverEnum = ServerEnum.ppysb;
            }
            String _mode = (String) arguments[2];
            try {
                mode = Integer.parseInt(_mode);
                resetMode = true;
            } catch (NumberFormatException e) {
                message.reply("mode格式错误，请输入数字");
                return;
            }
        }
        if (arguments.length == 2) {
            String Server = (String) arguments[1];
            if (Server.equals("ppysb")) {
                serverEnum = ServerEnum.ppysb;
            }
        }
        String kookId = sender.getId();
        try {
            PluginUser pluginUser = Main.userDao.getUser(sender.getId());
            Account account = new Account(osuId, sender.getId(), serverEnum.ordinal());
            Response<AbstractUserInfo> userInfoRes = AutoAPI.getUserInfo(osuId, serverEnum, mode);
            if (!userInfoRes.success) {
                message.reply("获取osu信息失败:" + userInfoRes.message);
                return;
            }
            if (userInfoRes.data == null) {
                message.reply("查无此人:" + osuId + " in " + serverEnum.name());
                return;
            }
            account.setUserName(userInfoRes.data.getUserName());
            if (Main.accountDao.isBound(osuId)) {
                message.reply("该osuid已经被绑定过了");
                return;
            }
            if (!Main.accountDao.bindAccount(account)) {
                message.reply("创建账号失败");
                return;
            }
            if (pluginUser != null) {
                pluginUser.setOsuId(osuId);
                pluginUser.setServerId(serverEnum.ordinal());
                if (resetMode) {
                    pluginUser.setMode(mode);
                }
                if (Main.userDao.updateById(pluginUser) == 1) {
                    message.reply("绑定成功");
                } else {
                    message.reply("插件内部错误");
                }
                return;
            }
            //不存在则新建
            pluginUser = new PluginUser(kookId, osuId, mode, serverEnum.ordinal());
            if (Main.userDao.registerUser(pluginUser)) {
                message.reply("绑定成功");
            } else {
                message.reply("插件内部错误");
            }
        } catch (Exception e) {
            message.reply("插件内部错误" + e.getLocalizedMessage());
            Main.logger.error("Bind命令出错",e);
        }
    }
}

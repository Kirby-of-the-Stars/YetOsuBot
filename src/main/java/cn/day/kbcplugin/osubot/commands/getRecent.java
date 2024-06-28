package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.AutoAPI;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.pojo.PluginUser;
import cn.day.kbcplugin.osubot.pojo.base.Response;
import cn.day.kbcplugin.osubot.pojo.common.AbstractBeatmap;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.pojo.common.AbstractUserInfo;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.module.ContainerModule;

import java.util.Base64;

public class getRecent implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (message == null) return;

        PluginUser pluginUser = Main.userDao.getUser(sender.getId());
        if (pluginUser == null) {
            message.reply("请先绑定一个osu账号");
            return;
        }
        int Mode = pluginUser.getMode();
        ServerEnum serverEnum = pluginUser.getCurrentServer();
        if (arguments.length == 1) {
            try {
                Mode = Integer.parseInt((String) arguments[0]);
            } catch (Exception ignore) {
                Mode = pluginUser.getMode();
            }
        }
        if(arguments.length > 1){
            try {
                serverEnum = ServerEnum.parseInt(Integer.parseInt((String) arguments[0]));
                Mode = Integer.parseInt((String) arguments[1]);
            } catch (Exception ignore) {
                Mode = pluginUser.getMode();
                serverEnum = pluginUser.getCurrentServer();
            }
        }
        Response<AbstractUserInfo> res_info = AutoAPI.getUserInfo(pluginUser.getOsuId(),serverEnum,Mode);
        if(!res_info.success){
            message.reply(res_info.message);
            return;
        }
        if(res_info.data==null){
            message.reply("用户不存在");
            return;
        }
        AbstractUserInfo userInfo = res_info.data;
        Response<AbstractScore> res_score = AutoAPI.getRecent(userInfo,Mode,serverEnum);
        if(!res_score.success){
            message.reply(res_info.message);
            return;
        }
        if(res_score.data==null){
            message.reply("最近还没玩过呢,去打一把");
            return;
        }
        AbstractScore recent = res_score.data;
        AbstractBeatmap beatmap = Main.sayobotApi.getMap(recent.beatmapId());
        if(beatmap==null){
            message.reply("铺面未找到,你的成绩来源一个unrank?");
            return;
        }
        String base64Str = null;
        try {
            base64Str = Main.imgUtil.drawResult(userInfo.getUserName(), recent, beatmap, Mode);
        }catch (Exception e){
            Main.logger.error("绘图异常",e);
        }
        if(base64Str==null){
            message.reply("绘图出现了些错误呢..");
            return;
        }
        CardBuilder builder = new CardBuilder();
        builder.setTheme(Theme.SUCCESS).setSize(Size.LG);
        byte[] base64File = Base64.getDecoder().decode(base64Str);
        String fileName = message.getId() + "recent" + "-" + System.currentTimeMillis() + ".png";
        String url = Main.instance.getCore().getHttpAPI().uploadFile(fileName, base64File);
        builder.addModule(new ContainerModule.Builder()
                .add(new ImageElement(url, null, Size.LG, true)).build());
        message.reply(builder.build());
    }
}

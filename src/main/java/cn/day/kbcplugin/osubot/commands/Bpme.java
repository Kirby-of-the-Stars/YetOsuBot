package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.AutoAPI;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.pojo.PluginUser;
import cn.day.kbcplugin.osubot.pojo.bancho.Beatmap;
import cn.day.kbcplugin.osubot.pojo.bancho.BanchoScore;
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
import snw.jkook.message.component.card.module.SectionModule;

import java.util.Base64;
import java.util.List;

public class Bpme implements UserCommandExecutor {

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (message == null) return;
        if (arguments.length < 1) {
            message.reply("至少需要一个参数");
            return;
        }
        String _mode = null;
        String _num = null;
        if (arguments.length >= 2) {
            _mode = (String) arguments[0];
            _num = (String) arguments[1];
        } else {
            _num = (String) arguments[0];
        }
        if (!_num.startsWith("#")) {
            message.reply("参数错误:请输入正确的bp参数");
            return;
        }
        PluginUser pluginUser = Main.userDao.getUser(sender.getId());
        if (pluginUser == null) {
            message.reply("请先绑定一个osu账号");
            return;
        }
        ServerEnum serverEnum = ServerEnum.Bancho;
        if(pluginUser.getServerId().equals(ServerEnum.ppysb.ordinal())){
            serverEnum = ServerEnum.ppysb;
        }
        int num = Integer.parseInt(_num.substring(1));
        int mode;
        if (_mode == null) {
            mode = pluginUser.getMode();
        } else {
            try {
                mode = Integer.parseInt(_mode);
            }catch (NumberFormatException e){
                message.reply("参数错误:请输入正确的mode参数");
                return;
            }
        }
        List<? extends AbstractScore> BpList;
        Response<AbstractUserInfo> infoRes = AutoAPI.getUserInfo(pluginUser.getOsuId(), ServerEnum.parseInt(pluginUser.getServerId()),mode);
        if(!infoRes.success){
            message.reply(infoRes.message);
            return;
        }
        if (infoRes.data == null) {
            message.reply("该osu账号不存在");
            return;
        }
        AbstractUserInfo userInfo = infoRes.data;
        Response<List<AbstractScore>> bpRes = AutoAPI.getBpList(userInfo,20,serverEnum,mode);
        if(!bpRes.success){
            message.reply(bpRes.message);
            return;
        }
        BpList = bpRes.data;
        if (BpList==null || BpList.isEmpty()) {
            message.reply("没找到任何bp,试着去玩几局把?");
            return;
        }
        if (num > BpList.size()) {
            message.reply("指定的bp数字太大了");
            return;
        }
        CardBuilder preReply = new CardBuilder();
        preReply.setTheme(Theme.PRIMARY).setSize(Size.SM);
        preReply.addModule(new SectionModule("获取中，可能会有点久"));
        String replyMessageId = message.reply(preReply.build());

        AbstractScore score = BpList.get(num - 1);
        Main.logger.info("获得了玩家" + userInfo.getUserName() + "在模式：" + mode + "的第" + num + "个BP：" + score.beatmapId() + "，正在获取歌曲名称");
        AbstractBeatmap map = Main.sayobotApi.getMap(score.beatmapId());

        String base64Result = Main.imgUtil.drawResult(userInfo.getUserName(), score, map, mode);

        CardBuilder builder = new CardBuilder();
        Message replyMessage = Main.instance.getCore().getUnsafe().getTextChannelMessage(replyMessageId);
        replyMessage.delete();
        if (base64Result == null) {
            builder.setTheme(Theme.DANGER).setSize(Size.SM)
                    .addModule(new SectionModule("获取失败"));
            message.reply(builder.build());
        } else {
            builder.setTheme(Theme.SUCCESS).setSize(Size.LG);
            byte[] base64File = Base64.getDecoder().decode(base64Result);
            String fileName = message.getId() + "bpme" + "-" + System.currentTimeMillis() + ".png";
            String url = Main.instance.getCore().getHttpAPI().uploadFile(fileName, base64File);
            builder.addModule(new ContainerModule.Builder()
                    .add(new ImageElement(url, null, Size.LG, true)).build());
            message.reply(builder.build());
        }
    }
}

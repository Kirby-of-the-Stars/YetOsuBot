//package cn.day.kbcplugin.osubot.commands;
//
//import cn.day.kbcplugin.osubot.Main;
//import cn.day.kbcplugin.osubot.api.AutoAPI;
//import cn.day.kbcplugin.osubot.enums.ServerEnum;
//import cn.day.kbcplugin.osubot.pojo.PluginUser;
//import cn.day.kbcplugin.osubot.pojo.base.Response;
//import cn.day.kbcplugin.osubot.model.api.base.AbstractScore;
//import cn.day.kbcplugin.osubot.model.api.base.AbstractUserInfo;
//import org.jetbrains.annotations.Nullable;
//import snw.jkook.command.UserCommandExecutor;
//import snw.jkook.entity.User;
//import snw.jkook.message.Message;
//
//import java.util.List;
//
//public class BpList implements UserCommandExecutor {
//
//    private final int MAX_LIMIT = 100;
//
//    private final int DEFAULT_LIMIT = 10;
//
//    @Override
//    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
//        if (message == null) return;
//        int limit = DEFAULT_LIMIT;
//        if (arguments.length > 1) {
//            try{
//                limit = Integer.parseInt((String)arguments[0] );
//            }catch (NumberFormatException e){
//                message.reply("请输入正确的数字");
//                return;
//            }
//            if(limit>MAX_LIMIT){
//                limit = MAX_LIMIT;
//            }
//        }
//        PluginUser pluginUser = Main.userDao.getUser(sender.getId());
//        if(pluginUser==null){
//            message.reply("请先绑定一个账号");
//            return;
//        }
//        List<? extends AbstractScore> BpList;
//        Response<AbstractUserInfo> infoRes = AutoAPI.getUserInfo(pluginUser.getOsuId(), ServerEnum.parseInt(pluginUser.getServerId()), pluginUser.getMode());
//        if(!infoRes.success){
//            message.reply(infoRes.message);
//            return;
//        }
//        if (infoRes.data == null) {
//            message.reply("该osu账号不存在");
//            return;
//        }
//        AbstractUserInfo userInfo = infoRes.data;
//        Response<List<AbstractScore>> bpRes = AutoAPI.getBpList(userInfo,limit,pluginUser.getCurrentServer(), pluginUser.getMode());
//        if(!bpRes.success){
//            message.reply(bpRes.message);
//            return;
//        }
//        BpList = bpRes.data;
//        if (BpList==null || BpList.isEmpty()) {
//            message.reply("没找到任何bp,试着去玩几局把?");
//            return;
//        }
//
//
//    }
//}

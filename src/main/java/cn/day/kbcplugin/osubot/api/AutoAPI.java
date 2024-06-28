package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.pojo.bancho.BanchoUser;
import cn.day.kbcplugin.osubot.pojo.base.Response;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.pojo.common.AbstractUserInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//根据server判断用哪个api
public class AutoAPI {

    public static @NotNull Response<AbstractUserInfo> getUserInfo(int osuId, @NotNull ServerEnum serverEnum, int mode){
        Response<AbstractUserInfo> res = new Response<>();
        AbstractUserInfo userInfo = null;
        switch (serverEnum){
            case Bancho:
                userInfo = Main.banchoApi.getUser(mode,String.valueOf(osuId));
                break;
            case ppysb:
                userInfo = Main.ppySbApi.getUserInfo(osuId);
                break;
            default:
                res.success = false;
                res.message = "Not Supported Server!";
                return res;
        }
        res.data = userInfo;
        return res;
    }

    public static @NotNull Response<List<AbstractScore>> getBpList(AbstractUserInfo userInfo, int limit, @NotNull ServerEnum serverEnum, int mode){
        Response<List<AbstractScore>> res = new Response<>();
        List<? extends AbstractScore> list = null;
        switch (serverEnum){
            case Bancho:
                list = Main.banchoApi.getBP(mode,String.valueOf(userInfo.getUserId()));break;
            case ppysb:
                list = Main.ppySbApi.getBestScores(userInfo.getUserId(),limit, mode);break;
            default:
                res.success = false;
                res.message = "No Supported Server!";
                return res;
        }
        if(list==null){
            res.data = null;
            return res;
        }
        List<AbstractScore> temp = new ArrayList<>(list.size());
        temp.addAll(list);
        res.data = temp;
        return res;
    }

    public static @NotNull Response<AbstractScore> getRecent(AbstractUserInfo userInfo, int mode, @NotNull ServerEnum serverEnum){
        Response<AbstractScore> res = new Response<>();
        AbstractScore score = null;
        switch (serverEnum){
            case Bancho:
                score = Main.banchoApi.getUserRecent(userInfo.getUserId(),mode);
                break;
            case ppysb:
                score = Main.ppySbApi.getRecent(userInfo.getUserId(), mode);
                break;
            default:
                res.success = false;
                res.message = "Not Supported Server!";
                return res;
        }
        res.data = score;
        return res;
    }

}

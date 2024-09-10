package cn.day.kbcplugin.osubot.model.api.base;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;

public interface IUserInfo {

    String getUserId();

    String getUserName();

    Float getPP();

    Long getRankScore();

    Long getTotalScore();

    Integer getRank();

    Integer getPlayCount();

    Float getAcc();

    Integer getLevel();

    OsuModeEnum getMode();
}

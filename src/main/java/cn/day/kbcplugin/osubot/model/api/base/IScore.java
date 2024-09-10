package cn.day.kbcplugin.osubot.model.api.base;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;

import java.time.LocalDateTime;

public interface IScore {

    Long scoreId();

    Long Score();

    Float PP();

    Float Acc();

    Float AimPP();

    Float SpeedPP();

    Float AccPP();

    Float MaxPP();

    Float FcPP();

    Integer maxCombo();

    Integer mods();

    Integer Count300();

    Integer Count100();

    Integer Count50();

    Integer CountMiss();

    Integer CountGeki();

    Integer CountKatu();

    String Grade();

    OsuModeEnum mode();

    boolean isPerfect();

    Long beatmapId();

    Long beatmapSetId();

    LocalDateTime date();

    IScore setMode(OsuModeEnum mode);

}

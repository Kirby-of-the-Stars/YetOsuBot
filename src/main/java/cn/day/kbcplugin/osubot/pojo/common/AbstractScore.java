package cn.day.kbcplugin.osubot.pojo.common;

import java.util.Date;

public interface AbstractScore {

    Integer scoreId();
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
    Integer mode();
    boolean isPerfect();
    Integer beatmapId();
    Integer beatmapSetId();
    Date date();

}

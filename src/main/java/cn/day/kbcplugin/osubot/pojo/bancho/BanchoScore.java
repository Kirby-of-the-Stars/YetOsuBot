package cn.day.kbcplugin.osubot.pojo.bancho;

import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.hutool.core.annotation.Alias;

import java.util.Date;

/**
 * The type Score.
 */

public class BanchoScore implements AbstractScore {

    private Float acc;

    //仅用于get_user_best,其他API没有
    private String beatmapId;
    //这个不在API返回值，画BP的时候手动拼接的
    private Integer beatmapSetId;
    private String beatmapName;
    //这六个仅仅用于db解析
    private Byte mode;
    @Alias("score_id")
    private Long onlineId;
    //用于存储BP的位数
    private Integer bpId;
    private Long score;
    @Alias("maxcombo")
    private Integer maxCombo;
    private Integer count50;
    private Integer count100;
    private Integer count300;
    @Alias("countmiss")
    private Integer countMiss;
    @Alias("countkatu")
    private Integer countKatu;
    @Alias("countgeki")
    private Integer countGeki;
    private Integer perfect;
    private Integer enabledMods;
    //更换为LocalDateTime会出反序列化异常
    private Date date;
    private String rank;
    //recent的API里压根没有这个字段
    private Float pp;
    private Float aim_pp;
    private Float speed_pp;
    private Float acc_pp;
    private Float max_pp;
    private Float fc_pp;
    //md ppysb
    @Alias("username")
    private String userName;
    private Integer userId;
    //为兼容get_match
    private Integer pass;

    public String getBeatmapId() {
        return beatmapId;
    }

    public void setBeatmapId(String beatmapId) {
        this.beatmapId = beatmapId;
    }

    public String getBeatmapName() {
        return beatmapName;
    }

    public void setBeatmapName(String beatmapName) {
        this.beatmapName = beatmapName;
    }

    public Byte getMode() {
        return mode;
    }

    public void setMode(Byte mode) {
        this.mode = mode;
    }

    public Long getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    public Integer getBpId() {
        return bpId;
    }

    public void setBpId(Integer bpId) {
        this.bpId = bpId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Integer getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(Integer maxCombo) {
        this.maxCombo = maxCombo;
    }

    public Integer getCount50() {
        return count50;
    }

    public void setCount50(Integer count50) {
        this.count50 = count50;
    }

    public Integer getCount100() {
        return count100;
    }

    public void setCount100(Integer count100) {
        this.count100 = count100;
    }

    public Integer getCount300() {
        return count300;
    }

    public void setCount300(Integer count300) {
        this.count300 = count300;
    }

    public Integer getCountMiss() {
        return countMiss;
    }

    public void setCountMiss(Integer countMiss) {
        this.countMiss = countMiss;
    }

    public Integer getCountKatu() {
        return countKatu;
    }

    public void setCountKatu(Integer countKatu) {
        this.countKatu = countKatu;
    }

    public Integer getCountGeki() {
        return countGeki;
    }

    public void setCountGeki(Integer countGeki) {
        this.countGeki = countGeki;
    }

    public Integer getPerfect() {
        return perfect;
    }

    public void setPerfect(Integer perfect) {
        this.perfect = perfect;
    }

    public Integer getEnabledMods() {
        return enabledMods;
    }

    public void setEnabledMods(Integer enabledMods) {
        this.enabledMods = enabledMods;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Float getPp() {
        return pp;
    }

    public void setPp(Float pp) {
        this.pp = pp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    public Float getAcc() {
        return acc;
    }

    public void setAcc(Float acc) {
        this.acc = acc;
    }

    @Override
    public String toString() {
        return "Score{" +
                "acc=" + acc +
                ", beatmapId='" + beatmapId + '\'' +
                ", beatmapName='" + beatmapName + '\'' +
                ", mode=" + mode +
                ", onlineId=" + onlineId +
                ", bpId=" + bpId +
                ", score=" + score +
                ", maxCombo=" + maxCombo +
                ", count50=" + count50 +
                ", count100=" + count100 +
                ", count300=" + count300 +
                ", countMiss=" + countMiss +
                ", countKatu=" + countKatu +
                ", countGeki=" + countGeki +
                ", perfect=" + perfect +
                ", enabledMods=" + enabledMods +
                ", date=" + date +
                ", rank='" + rank + '\'' +
                ", pp=" + pp +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", pass=" + pass +
                '}';
    }

    @Override
    public Integer scoreId() {
        return this.onlineId.intValue();
    }

    @Override
    public Long Score() {
        return this.score;
    }

    @Override
    public Float PP() {
        return this.pp;
    }

    @Override
    public Float Acc() {
        return this.acc;
    }

    @Override
    public Float AimPP() {
        return this.aim_pp;
    }

    @Override
    public Float SpeedPP() {
        return this.speed_pp;
    }

    @Override
    public Float AccPP() {
        return this.acc_pp;
    }

    @Override
    public Float MaxPP() {
        return this.max_pp;
    }

    @Override
    public Float FcPP() {
        return this.fc_pp;
    }

    @Override
    public Integer maxCombo() {
        return this.maxCombo;
    }

    @Override
    public Integer mods() {
        return this.enabledMods;
    }

    @Override
    public Integer Count300() {
        return this.count300;
    }

    @Override
    public Integer Count100() {
        return this.count100;
    }

    @Override
    public Integer Count50() {
        return this.count50;
    }

    @Override
    public Integer CountMiss() {
        return this.countMiss;
    }

    @Override
    public Integer CountGeki() {
        return this.countGeki;
    }

    @Override
    public Integer CountKatu() {
        return this.countKatu;
    }

    @Override
    public String Grade() {
        return this.rank;
    }

    @Override
    public Integer mode() {
        return this.mode.intValue();
    }

    @Override
    public boolean isPerfect() {
        return this.perfect.equals(1);
    }

    @Override
    public Integer beatmapId() {
        return Integer.parseInt(this.beatmapId);
    }

    @Override
    public Integer beatmapSetId() {
        return this.beatmapSetId;
    }

    @Override
    public Date date() {
        return this.date;
    }
}

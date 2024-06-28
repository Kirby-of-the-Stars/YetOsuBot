package cn.day.kbcplugin.osubot.pojo.bancho;

import cn.day.kbcplugin.osubot.pojo.common.AbstractUserInfo;
import cn.hutool.core.annotation.Alias;


import java.time.LocalDate;

public class Userinfo implements AbstractUserInfo {
    /**
     * 这个字段不写入数据库
     */
    @Alias("username")
    private String userName;
    private Integer mode;
    private int userId;
    private int count300;
    private int count100;
    private int count50;
    @Alias("playcount")
    private int playCount;
    private float accuracy;
    private float ppRaw;
    private long rankedScore;
    private long totalScore;
    private float level;
    private int ppRank;
    private int countRankSs;
    private int countRankSsh;
    private int countRankS;
    private int countRankSh;
    private int countRankA;
    private LocalDate queryDate;


    @Override
    public Integer getUserId() {
        return this.userId;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public Float getPP() {
        return this.ppRaw;
    }

    @Override
    public Long getRankScore() {
        return this.rankedScore;
    }

    @Override
    public Long getTotalScore() {
        return this.totalScore;
    }

    @Override
    public Integer getRank() {
        return this.ppRank;
    }

    @Override
    public Integer getMode() {
        return this.mode;
    }

    @Override
    public Integer getPlayCount() {
        return this.playCount;
    }

    @Override
    public Float getAcc() {
        return this.accuracy;
    }

    @Override
    public Float getLevel() {
        return this.level;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
    public void setQueryDate(LocalDate queryDate) {
        this.queryDate = queryDate;
    }

    public int getCountRankSs() {
        return countRankSs;
    }

    public void setCountRankSs(int countRankSs) {
        this.countRankSs = countRankSs;
    }

    public int getCountRankSsh() {
        return countRankSsh;
    }

    public void setCountRankSsh(int countRankSsh) {
        this.countRankSsh = countRankSsh;
    }

    public int getCountRankS() {
        return countRankS;
    }

    public void setCountRankS(int countRankS) {
        this.countRankS = countRankS;
    }

    public int getCountRankSh() {
        return countRankSh;
    }

    public void setCountRankSh(int countRankSh) {
        this.countRankSh = countRankSh;
    }

    public int getCountRankA() {
        return countRankA;
    }

    public void setCountRankA(int countRankA) {
        this.countRankA = countRankA;
    }

    public int getCount300() {
        return count300;
    }

    public int getCount100() {
        return count100;
    }

    public int getCount50() {
        return count50;
    }

    public LocalDate getQueryDate() {
        return queryDate;
    }
}

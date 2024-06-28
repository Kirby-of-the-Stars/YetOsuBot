package cn.day.kbcplugin.osubot.pojo.ppysb;

import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;

import java.util.Date;

public class SbScore implements AbstractScore {
    private int id;
    private long score;
    private float pp;
    private float aim_pp;
    private float speed_pp;
    private float acc_pp;
    private float max_pp;
    private float fc_pp;
    private float acc;
    private int maxCombo;
    private int mods;
    private int n300;
    private int n100;
    private int n50;
    private int nmiss;
    private int ngeki;
    private int nkatu;
    private String grade;
    private int status;
    private int mode;
    private Date playTime;
    private long timeElapsed;
    private int perfect;
    public SbBeatMap beatmap;

    @Override
    public Integer scoreId() {
        return this.id;
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
        return this.mods;
    }

    @Override
    public Integer Count300() {
        return this.n300;
    }

    @Override
    public Integer Count100() {
        return this.n100;
    }

    @Override
    public Integer Count50() {
        return this.n50;
    }

    @Override
    public Integer CountMiss() {
        return this.nmiss;
    }

    @Override
    public Integer CountGeki() {
        return this.ngeki;
    }

    @Override
    public Integer CountKatu() {
        return this.nkatu;
    }

    @Override
    public String Grade() {
        return this.grade;
    }

    @Override
    public Integer mode() {
        return this.mode;
    }

    @Override
    public boolean isPerfect() {
        return this.perfect==1;
    }

    @Override
    public Integer beatmapId() {
        return this.beatmap.id;
    }

    @Override
    public Integer beatmapSetId() {
        return this.beatmap.setId;
    }

    @Override
    public Date date() {
        return this.playTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setPp(float pp) {
        this.pp = pp;
    }

    public void setAim_pp(float aim_pp) {
        this.aim_pp = aim_pp;
    }

    public void setSpeed_pp(float speed_pp) {
        this.speed_pp = speed_pp;
    }

    public void setAcc_pp(float acc_pp) {
        this.acc_pp = acc_pp;
    }

    public void setMax_pp(float max_pp) {
        this.max_pp = max_pp;
    }

    public void setFc_pp(float fc_pp) {
        this.fc_pp = fc_pp;
    }

    public void setAcc(float acc) {
        this.acc = acc;
    }

    public void setMaxCombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public void setMods(int mods) {
        this.mods = mods;
    }

    public void setN300(int n300) {
        this.n300 = n300;
    }

    public void setN100(int n100) {
        this.n100 = n100;
    }

    public void setN50(int n50) {
        this.n50 = n50;
    }

    public void setNmiss(int nmiss) {
        this.nmiss = nmiss;
    }

    public void setNgeki(int ngeki) {
        this.ngeki = ngeki;
    }

    public void setNkatu(int nkatu) {
        this.nkatu = nkatu;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public void setPerfect(int perfect) {
        this.perfect = perfect;
    }

    public int getId() {
        return id;
    }

    public long getScore() {
        return score;
    }

    public float getPp() {
        return pp;
    }

    public float getAim_pp() {
        return aim_pp;
    }

    public float getSpeed_pp() {
        return speed_pp;
    }

    public float getAcc_pp() {
        return acc_pp;
    }

    public float getMax_pp() {
        return max_pp;
    }

    public float getFc_pp() {
        return fc_pp;
    }

    public float getAcc() {
        return acc;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public int getMods() {
        return mods;
    }

    public int getN300() {
        return n300;
    }

    public int getN100() {
        return n100;
    }

    public int getN50() {
        return n50;
    }

    public int getNmiss() {
        return nmiss;
    }

    public int getNgeki() {
        return ngeki;
    }

    public int getNkatu() {
        return nkatu;
    }

    public String getGrade() {
        return grade;
    }

    public int getStatus() {
        return status;
    }

    public int getMode() {
        return mode;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public int getPerfect() {
        return perfect;
    }
}
package cn.day.kbcplugin.osubot.pojo.api;

import cn.day.kbcplugin.osubot.pojo.common.AbstractBeatmap;

public class SayoMapInfo_old implements AbstractBeatmap {
    private int sid;
    private float AR;
    private float CS;
    private float HP;
    private float OD;
    private float aim;
    private String audio;
    private String bg;
    private int bid;
    private int circles;
    private int hit300window;
    private String img;
    private int length;
    private int maxcombo;
    private int mode;
    private int passcount;
    private int playcount;
    private float pp;
    private float pp_acc;
    private float pp_aim;
    private float pp_speed;
    private int sliders;
    private float speed;
    private int spinners;
    private float star;
    private String strain_aim;
    private String strain_speed;
    private String version;
    private String artist;
    private String title;
    private String creator;

    public float getAR() {
        return AR;
    }

    public void setAR(float AR) {
        this.AR = AR;
    }

    public float getCS() {
        return CS;
    }

    public void setCS(float CS) {
        this.CS = CS;
    }

    public float getHP() {
        return HP;
    }

    public void setHP(float HP) {
        this.HP = HP;
    }

    public float getOD() {
        return OD;
    }

    public void setOD(float OD) {
        this.OD = OD;
    }

    public float getAim() {
        return aim;
    }

    public void setAim(float aim) {
        this.aim = aim;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public Integer getBid() {
        return bid;
    }

    @Override
    public String getArtist() {
        return this.artist;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Integer getMaxCombo() {
        return this.maxcombo;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getCircles() {
        return circles;
    }

    public void setCircles(int circles) {
        this.circles = circles;
    }

    public int getHit300window() {
        return hit300window;
    }

    public void setHit300window(int hit300window) {
        this.hit300window = hit300window;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMaxcombo() {
        return maxcombo;
    }

    public void setMaxcombo(int maxcombo) {
        this.maxcombo = maxcombo;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPasscount() {
        return passcount;
    }

    public void setPasscount(int passcount) {
        this.passcount = passcount;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public float getPp() {
        return pp;
    }

    public void setPp(float pp) {
        this.pp = pp;
    }

    public float getPp_acc() {
        return pp_acc;
    }

    public void setPp_acc(float pp_acc) {
        this.pp_acc = pp_acc;
    }

    public float getPp_aim() {
        return pp_aim;
    }

    public void setPp_aim(float pp_aim) {
        this.pp_aim = pp_aim;
    }

    public float getPp_speed() {
        return pp_speed;
    }

    public void setPp_speed(float pp_speed) {
        this.pp_speed = pp_speed;
    }

    public int getSliders() {
        return sliders;
    }

    public void setSliders(int sliders) {
        this.sliders = sliders;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getSpinners() {
        return spinners;
    }

    public void setSpinners(int spinners) {
        this.spinners = spinners;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getStrain_aim() {
        return strain_aim;
    }

    public void setStrain_aim(String strain_aim) {
        this.strain_aim = strain_aim;
    }

    public String getStrain_speed() {
        return strain_speed;
    }

    public void setStrain_speed(String strain_speed) {
        this.strain_speed = strain_speed;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String getCreator() {
        return null;
    }

    @Override
    public String getBgName() {
        return this.bg;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}

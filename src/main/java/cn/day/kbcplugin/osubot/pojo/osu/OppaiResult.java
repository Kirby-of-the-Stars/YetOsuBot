package cn.day.kbcplugin.osubot.pojo.osu;



/**
 * Created by QHS on 2017/9/11.
 */

public class OppaiResult {
    private String oppaiVersion;
    private int code;
    private String errstr;
    private String artist;
    private String artistUnicode;
    private String title;
    private String titleUnicode;
    private String creator;
    private String version;
    private String modsStr;
    private int mods;
    private double od;
    private double ar;
    private double cs;
    private double hp;
    private int combo;
    private int maxCombo;
    private int numCircles;
    private int numSliders;
    private int numSpinners;
    private int misses;
    private int scoreVersion;
    private double stars;
    private double starsLegacy;
    private double speedStars;
    private double aimStars;
    private int nsingles;
    private int nsinglesThreshold;
    private double aimPp;
    private double speedPp;
    private double accPp;
    private double pp;
    private double ppLegacy;
    private double speedMultiplier;
    private double maxPP;

    public String getOppaiVersion() {
        return oppaiVersion;
    }

    public void setOppaiVersion(String oppaiVersion) {
        this.oppaiVersion = oppaiVersion;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrstr() {
        return errstr;
    }

    public void setErrstr(String errstr) {
        this.errstr = errstr;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public void setArtistUnicode(String artistUnicode) {
        this.artistUnicode = artistUnicode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModsStr() {
        return modsStr;
    }

    public void setModsStr(String modsStr) {
        this.modsStr = modsStr;
    }

    public int getMods() {
        return mods;
    }

    public void setMods(int mods) {
        this.mods = mods;
    }

    public double getOd() {
        return od;
    }

    public void setOd(double od) {
        this.od = od;
    }

    public double getAr() {
        return ar;
    }

    public void setAr(double ar) {
        this.ar = ar;
    }

    public double getCs() {
        return cs;
    }

    public void setCs(double cs) {
        this.cs = cs;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public int getNumCircles() {
        return numCircles;
    }

    public void setNumCircles(int numCircles) {
        this.numCircles = numCircles;
    }

    public int getNumSliders() {
        return numSliders;
    }

    public void setNumSliders(int numSliders) {
        this.numSliders = numSliders;
    }

    public int getNumSpinners() {
        return numSpinners;
    }

    public void setNumSpinners(int numSpinners) {
        this.numSpinners = numSpinners;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public int getScoreVersion() {
        return scoreVersion;
    }

    public void setScoreVersion(int scoreVersion) {
        this.scoreVersion = scoreVersion;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public double getStarsLegacy() {
        return starsLegacy;
    }

    public void setStarsLegacy(double starsLegacy) {
        this.starsLegacy = starsLegacy;
    }

    public double getSpeedStars() {
        return speedStars;
    }

    public void setSpeedStars(double speedStars) {
        this.speedStars = speedStars;
    }

    public double getAimStars() {
        return aimStars;
    }

    public void setAimStars(double aimStars) {
        this.aimStars = aimStars;
    }

    public int getNsingles() {
        return nsingles;
    }

    public void setNsingles(int nsingles) {
        this.nsingles = nsingles;
    }

    public int getNsinglesThreshold() {
        return nsinglesThreshold;
    }

    public void setNsinglesThreshold(int nsinglesThreshold) {
        this.nsinglesThreshold = nsinglesThreshold;
    }

    public double getAimPp() {
        return aimPp;
    }

    public void setAimPp(double aimPp) {
        this.aimPp = aimPp;
    }

    public double getSpeedPp() {
        return speedPp;
    }

    public void setSpeedPp(double speedPp) {
        this.speedPp = speedPp;
    }

    public double getAccPp() {
        return accPp;
    }

    public void setAccPp(double accPp) {
        this.accPp = accPp;
    }

    public double getPp() {
        return pp;
    }

    public void setPp(double pp) {
        this.pp = pp;
    }

    public double getPpLegacy() {
        return ppLegacy;
    }

    public void setPpLegacy(double ppLegacy) {
        this.ppLegacy = ppLegacy;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public double getMaxPP() {
        return maxPP;
    }

    public void setMaxPP(double maxPP) {
        this.maxPP = maxPP;
    }

    public OppaiResult(String oppaiVersion, int code, String errstr, String artist, String artistUnicode, String title, String titleUnicode, String creator, String version, String modsStr, int mods, double od, double ar, double cs, double hp, int combo, int maxCombo, int numCircles, int numSliders, int numSpinners, int misses, int scoreVersion, double stars, double starsLegacy, double speedStars, double aimStars, int nsingles, int nsinglesThreshold, double aimPp, double speedPp, double accPp, double pp, double ppLegacy, double speedMultiplier, double maxPP) {
        this.oppaiVersion = oppaiVersion;
        this.code = code;
        this.errstr = errstr;
        this.artist = artist;
        this.artistUnicode = artistUnicode;
        this.title = title;
        this.titleUnicode = titleUnicode;
        this.creator = creator;
        this.version = version;
        this.modsStr = modsStr;
        this.mods = mods;
        this.od = od;
        this.ar = ar;
        this.cs = cs;
        this.hp = hp;
        this.combo = combo;
        this.maxCombo = maxCombo;
        this.numCircles = numCircles;
        this.numSliders = numSliders;
        this.numSpinners = numSpinners;
        this.misses = misses;
        this.scoreVersion = scoreVersion;
        this.stars = stars;
        this.starsLegacy = starsLegacy;
        this.speedStars = speedStars;
        this.aimStars = aimStars;
        this.nsingles = nsingles;
        this.nsinglesThreshold = nsinglesThreshold;
        this.aimPp = aimPp;
        this.speedPp = speedPp;
        this.accPp = accPp;
        this.pp = pp;
        this.ppLegacy = ppLegacy;
        this.speedMultiplier = speedMultiplier;
        this.maxPP = maxPP;
    }
    public OppaiResult(){}
}

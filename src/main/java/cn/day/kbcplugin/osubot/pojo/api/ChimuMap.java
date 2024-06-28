package cn.day.kbcplugin.osubot.pojo.api;

public class ChimuMap {
    private int BeatmapId;
    private int ParentSetId;
    private String FileMD5;
    private long Mode;
    private float BPM;
    private float AR;
    private float OD;
    private float CS;
    private float HP;
    private int TotalLength;
    private int HitLength;
    private long Playcount;
    private long Passcount;
    private int MaxCombo;
    private float DifficultyRating;
    private String OsuFile;
    private String DownloadPath;

    public int getBeatmapId() {
        return BeatmapId;
    }

    public void setBeatmapId(int beatmapId) {
        BeatmapId = beatmapId;
    }

    public int getParentSetId() {
        return ParentSetId;
    }

    public void setParentSetId(int parentSetId) {
        ParentSetId = parentSetId;
    }

    public String getFileMD5() {
        return FileMD5;
    }

    public void setFileMD5(String fileMD5) {
        FileMD5 = fileMD5;
    }

    public long getMode() {
        return Mode;
    }

    public void setMode(long mode) {
        Mode = mode;
    }

    public float getBPM() {
        return BPM;
    }

    public void setBPM(float BPM) {
        this.BPM = BPM;
    }

    public float getAR() {
        return AR;
    }

    public void setAR(float AR) {
        this.AR = AR;
    }

    public float getOD() {
        return OD;
    }

    public void setOD(float OD) {
        this.OD = OD;
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

    public int getTotalLength() {
        return TotalLength;
    }

    public void setTotalLength(int totalLength) {
        TotalLength = totalLength;
    }

    public int getHitLength() {
        return HitLength;
    }

    public void setHitLength(int hitLength) {
        HitLength = hitLength;
    }

    public long getPlaycount() {
        return Playcount;
    }

    public void setPlaycount(long playcount) {
        Playcount = playcount;
    }

    public long getPasscount() {
        return Passcount;
    }

    public void setPasscount(long passcount) {
        Passcount = passcount;
    }

    public int getMaxCombo() {
        return MaxCombo;
    }

    public void setMaxCombo(int maxCombo) {
        MaxCombo = maxCombo;
    }

    public float getDifficultyRating() {
        return DifficultyRating;
    }

    public void setDifficultyRating(float difficultyRating) {
        DifficultyRating = difficultyRating;
    }

    public String getOsuFile() {
        return OsuFile;
    }

    public void setOsuFile(String osuFile) {
        OsuFile = osuFile;
    }

    public String getDownloadPath() {
        return DownloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        DownloadPath = downloadPath;
    }
}

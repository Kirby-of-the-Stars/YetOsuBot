package cn.day.kbcplugin.osubot.pojo.bancho;

import cn.day.kbcplugin.osubot.pojo.common.AbstractBeatmap;
import cn.hutool.core.annotation.Alias;
import org.jetbrains.annotations.Nullable;

import java.util.Date;


public class Beatmap implements AbstractBeatmap {
    //提供兼容osu search的API
    @Alias("beatmapset_id")
    private Integer beatmapSetId;
    @Alias("beatmap_id")
    private Integer beatmapId;
    @Alias("approved")
    private Integer approved;
    @Alias("total_length")
    private Integer totalLength;
    @Alias("hit_length")
    private Integer hitLength;
    @Alias("version")
    private String version;
    @Alias("file_md5")
    private String fileMd5;
    @Alias("diff_size")
    private Float diffSize;
    @Alias("diff_overall")
    private Float diffOverall;
    @Alias("diff_approach")
    private Float diffApproach;
    @Alias("diff_drain")
    private Float diffDrain;

    private Integer mode;
    @Alias("approved_date")
    private Date approvedDate;
    @Alias("last_update")
    private Date lastUpdate;
    private String artist;
    private String title;
    private String creator;
    private Double bpm;
    private String source;
    private String tags;

    @Alias("genre_id")
    private Integer genreId;

    @Alias("language_id")
    private Integer languageId;
    @Alias("favourite_count")
    private Integer favouriteCount;
    @Alias("playcount")
    private Long playCount;
    @Alias("passcount")
    private Long passCount;
    @Alias("max_combo")
    private Integer maxCombo;
    @Alias("difficulty")
    private Double difficultyRating;
    @Alias("artist_unicode")
    private String artistUnicode;
    @Alias("title_unicode")
    private String titleUnicode;

    public Integer getBeatmapSetId() {
        return beatmapSetId;
    }

    public void setBeatmapSetId(Integer beatmapSetId) {
        this.beatmapSetId = beatmapSetId;
    }

    public Integer getBeatmapId() {
        return beatmapId;
    }

    public void setBeatmapId(Integer beatmapId) {
        this.beatmapId = beatmapId;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Integer totalLength) {
        this.totalLength = totalLength;
    }

    public Integer getHitLength() {
        return hitLength;
    }

    public void setHitLength(Integer hitLength) {
        this.hitLength = hitLength;
    }
    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Float getDiffSize() {
        return diffSize;
    }

    public void setDiffSize(Float diffSize) {
        this.diffSize = diffSize;
    }

    public Float getDiffOverall() {
        return diffOverall;
    }

    public void setDiffOverall(Float diffOverall) {
        this.diffOverall = diffOverall;
    }

    public Float getDiffApproach() {
        return diffApproach;
    }

    public void setDiffApproach(Float diffApproach) {
        this.diffApproach = diffApproach;
    }

    public Float getDiffDrain() {
        return diffDrain;
    }

    public void setDiffDrain(Float diffDrain) {
        this.diffDrain = diffDrain;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public Integer getSid() {
        return this.beatmapSetId;
    }

    @Override
    public Integer getBid() {
        return this.beatmapId;
    }
    @Override
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String getCreator() {
        return creator;
    }

    @Nullable
    @Override
    public String getBgName() {
        return null;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Double getBpm() {
        return bpm;
    }

    public void setBpm(Double bpm) {
        this.bpm = bpm;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Integer favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public Long getPassCount() {
        return passCount;
    }

    public void setPassCount(Long passCount) {
        this.passCount = passCount;
    }

    @Override
    public Integer getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(Integer maxCombo) {
        this.maxCombo = maxCombo;
    }

    public Double getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(Double difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public void setArtistUnicode(String artistUnicode) {
        this.artistUnicode = artistUnicode;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }
}


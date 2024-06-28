package cn.day.kbcplugin.osubot.pojo.api;

import java.util.List;

public class SayoMapSetInfo_old {
    private int sid;
    private int approved;
    private int approved_date;
    private String artist;
    private String artistU;
    private int bids_amount;
    private int bpm;
    private String creator;
    private int creator_id;
    private int favourite_count;
    private int genre;
    private int language;
    private long last_update;
    private long local_update;
    private int preview;
    private String source;
    private int storyboard;
    private String tags;
    private String title;
    private String titleU;
    private int video;

    public List<SayoMapInfo_old> bid_data;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(int approved_date) {
        this.approved_date = approved_date;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistU() {
        return artistU;
    }

    public void setArtistU(String artistU) {
        this.artistU = artistU;
    }

    public int getBids_amount() {
        return bids_amount;
    }

    public void setBids_amount(int bids_amount) {
        this.bids_amount = bids_amount;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getFavourite_count() {
        return favourite_count;
    }

    public void setFavourite_count(int favourite_count) {
        this.favourite_count = favourite_count;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public long getLast_update() {
        return last_update;
    }

    public void setLast_update(long last_update) {
        this.last_update = last_update;
    }

    public long getLocal_update() {
        return local_update;
    }

    public void setLocal_update(long local_update) {
        this.local_update = local_update;
    }

    public int getPreview() {
        return preview;
    }

    public void setPreview(int preview) {
        this.preview = preview;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStoryboard() {
        return storyboard;
    }

    public void setStoryboard(int storyboard) {
        this.storyboard = storyboard;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleU() {
        return titleU;
    }

    public void setTitleU(String titleU) {
        this.titleU = titleU;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}

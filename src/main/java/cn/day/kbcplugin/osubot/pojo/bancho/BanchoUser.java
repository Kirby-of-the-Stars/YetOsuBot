package cn.day.kbcplugin.osubot.pojo.bancho;

import cn.day.kbcplugin.osubot.pojo.common.AbstractUserInfo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class BanchoUser implements AbstractUserInfo {
    private String user_id;              // User ID
    private String username;             // Username
    private String join_date;            // Join date in UTC
    private String count300;             // Total amount for all ranked, approved, and loved beatmaps played
    private String count100;             // Total amount for all ranked, approved, and loved beatmaps played
    private String count50;              // Total amount for all ranked, approved, and loved beatmaps played
    private String playcount;            // Only counts ranked, approved, and loved beatmaps
    private String ranked_score;         // Counts the best individual score on each ranked, approved, and loved beatmaps
    private String total_score;          // Counts every score on ranked, approved, and loved beatmaps
    private String pp_rank;              // PP rank
    private String level;                // Level
    private String pp_raw;               // PP raw (0 for inactive players)
    private String accuracy;             // Accuracy
    private String count_rank_ss;        // Counts for SS ranks on maps
    private String count_rank_ssh;       // Counts for SSH ranks on maps
    private String count_rank_s;         // Counts for S ranks on maps
    private String count_rank_sh;        // Counts for SH ranks on maps
    private String count_rank_a;         // Counts for A ranks on maps
    private String country;              // Country (ISO3166-1 alpha-2 country code)
    private String total_seconds_played; // Total seconds played
    private String pp_country_rank;      // User's rank in the country
    private List<Event> events;          // List of events for the user
    private Integer mode;               //ppy 居然没有mode字段了，逆天!
    private LocalDate QueryDate;            //查询时间;

    public LocalDate getQueryDate() {
        return QueryDate;
    }

    public void setQueryDate(LocalDate queryDate) {
        QueryDate = queryDate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getCount300() {
        return count300;
    }

    public void setCount300(String count300) {
        this.count300 = count300;
    }

    public String getCount100() {
        return count100;
    }

    public void setCount100(String count100) {
        this.count100 = count100;
    }

    public String getCount50() {
        return count50;
    }

    public void setCount50(String count50) {
        this.count50 = count50;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getRanked_score() {
        return ranked_score;
    }

    public void setRanked_score(String ranked_score) {
        this.ranked_score = ranked_score;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getPp_rank() {
        return pp_rank;
    }

    public void setPp_rank(String pp_rank) {
        this.pp_rank = pp_rank;
    }

    @Override
    public Integer getUserId() {
        return Integer.parseInt(this.user_id);
    }

    @Override
    public String getUserName() {
        return this.username;
    }

    @Override
    public Float getPP() {
        return Float.parseFloat(this.pp_raw);
    }

    @Override
    public Long getRankScore() {
        return Long.parseLong(this.ranked_score);
    }

    @Override
    public Long getTotalScore() {
        return Long.parseLong(this.total_score);
    }

    @Override
    public Integer getRank() {
        return Integer.parseInt(this.pp_rank);
    }

    @Override
    public Integer getMode() {
        return mode==null?0:mode;
    }

    @Override
    public Integer getPlayCount() {
        return Integer.parseInt(this.playcount);
    }

    @Override
    public Float getAcc() {
        return Float.parseFloat(this.accuracy);
    }

    @Override
    public Float getLevel() {
        return Float.parseFloat(this.accuracy);
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPp_raw() {
        return pp_raw;
    }

    public void setPp_raw(String pp_raw) {
        this.pp_raw = pp_raw;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getCount_rank_ss() {
        return count_rank_ss;
    }

    public void setCount_rank_ss(String count_rank_ss) {
        this.count_rank_ss = count_rank_ss;
    }

    public String getCount_rank_ssh() {
        return count_rank_ssh;
    }

    public void setCount_rank_ssh(String count_rank_ssh) {
        this.count_rank_ssh = count_rank_ssh;
    }

    public String getCount_rank_s() {
        return count_rank_s;
    }

    public void setCount_rank_s(String count_rank_s) {
        this.count_rank_s = count_rank_s;
    }

    public String getCount_rank_sh() {
        return count_rank_sh;
    }

    public void setCount_rank_sh(String count_rank_sh) {
        this.count_rank_sh = count_rank_sh;
    }

    public String getCount_rank_a() {
        return count_rank_a;
    }

    public void setCount_rank_a(String count_rank_a) {
        this.count_rank_a = count_rank_a;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTotal_seconds_played() {
        return total_seconds_played;
    }

    public void setTotal_seconds_played(String total_seconds_played) {
        this.total_seconds_played = total_seconds_played;
    }

    public String getPp_country_rank() {
        return pp_country_rank;
    }

    public void setPp_country_rank(String pp_country_rank) {
        this.pp_country_rank = pp_country_rank;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}



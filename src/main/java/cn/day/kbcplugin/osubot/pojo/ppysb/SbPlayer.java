package cn.day.kbcplugin.osubot.pojo.ppysb;

import cn.day.kbcplugin.osubot.pojo.common.AbstractUserInfo;

public class SbPlayer implements AbstractUserInfo {
    private int id;
    private String name;
    private String safe_name;
    private int priv;
    private String country;
    private int silence_end;
    private int donor_end;
    private long creation_time;
    private long latest_activity;
    private int clan_id;
    private int clan_priv;
    private int preferred_mode;
    private int play_style;
    private String custom_badge_name;
    private String custom_badge_icon;
    private String userpage_content;

    public SbPlayerStats stats;

    public SbPlayer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSafe_name() {
        return safe_name;
    }

    public void setSafe_name(String safe_name) {
        this.safe_name = safe_name;
    }

    public int getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = priv;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSilence_end() {
        return silence_end;
    }

    public void setSilence_end(int silence_end) {
        this.silence_end = silence_end;
    }

    public int getDonor_end() {
        return donor_end;
    }

    public void setDonor_end(int donor_end) {
        this.donor_end = donor_end;
    }

    public long getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(long creation_time) {
        this.creation_time = creation_time;
    }

    public long getLatest_activity() {
        return latest_activity;
    }

    public void setLatest_activity(long latest_activity) {
        this.latest_activity = latest_activity;
    }

    public int getClan_id() {
        return clan_id;
    }

    public void setClan_id(int clan_id) {
        this.clan_id = clan_id;
    }

    public int getClan_priv() {
        return clan_priv;
    }

    public void setClan_priv(int clan_priv) {
        this.clan_priv = clan_priv;
    }

    public int getPreferred_mode() {
        return preferred_mode;
    }

    public void setPreferred_mode(int preferred_mode) {
        this.preferred_mode = preferred_mode;
    }

    public int getPlay_style() {
        return play_style;
    }

    public void setPlay_style(int play_style) {
        this.play_style = play_style;
    }

    public String getCustom_badge_name() {
        return custom_badge_name;
    }

    public void setCustom_badge_name(String custom_badge_name) {
        this.custom_badge_name = custom_badge_name;
    }

    public String getCustom_badge_icon() {
        return custom_badge_icon;
    }

    public void setCustom_badge_icon(String custom_badge_icon) {
        this.custom_badge_icon = custom_badge_icon;
    }

    public String getUserpage_content() {
        return userpage_content;
    }

    public void setUserpage_content(String userpage_content) {
        this.userpage_content = userpage_content;
    }

    @Override
    public Integer getUserId() {
        return this.id;
    }

    @Override
    public String getUserName() {
        return this.name;
    }

    @Override
    public Float getPP() {
        return this.stats.pp;
    }

    @Override
    public Long getRankScore() {
        return this.stats.rscore;
    }

    @Override
    public Long getTotalScore() {
        return this.stats.tscore;
    }

    @Override
    public Integer getRank() {
        return this.stats.rank;
    }

    @Override
    public Integer getMode() {
        return this.preferred_mode;
    }

    @Override
    public Integer getPlayCount() {
        return this.stats.plays;
    }

    @Override
    public Float getAcc() {
        return this.stats.acc;
    }

    @Override
    public Float getLevel() {
        return 0.0f;
    }


}

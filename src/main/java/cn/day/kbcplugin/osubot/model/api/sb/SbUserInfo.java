package cn.day.kbcplugin.osubot.model.api.sb;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Accessors(chain = true)
public class SbUserInfo implements IUserInfo {
    private Long id;
    private String name;
    @Alias("safe_name")
    private String safeName;
    private Integer priv;
    private String country;
    //unknown
    @Alias("silence_end")
    private Integer silenceEnd;
    //unknown
    @Alias("donor_end")
    private Integer donorEnd;
    @Alias("creation_time")
    private LocalDateTime creationTime;
    @Alias("latest_activity")
    private LocalDateTime latestActivity;

    //clan system
    @Alias("clan_id")
    private Long clanId;
    @Alias("clean_priv")
    private Integer cleanPriv;

    @Alias("preferred_mode")
    private Integer preferredMode;
    private Integer currentMode;
    @Alias("play_style")
    private Integer playStyle;

    //custom
    @Alias("custom_badge_name")
    private String customBadgeName;
    @Alias("custom_badge_icon")
    private Object customBadgeIcon;//unknown type
    //others
    @Alias("userpage_content")
    private String userPageContent;

    private Map<OsuModeEnum, SbUserState> stats;


    public SbUserState getPreferModeStat() {
        return stats.get(OsuModeEnum.get(getCurrentMode()));
    }

    public Integer getCurrentMode() {
        if(currentMode == null) {
            currentMode = preferredMode;
        }
        return currentMode;
    }

    @Override
    public String getUserId() {
        return String.valueOf(id);
    }

    @Override
    public String getUserName() {
        return safeName;
    }

    @Override
    public Float getPP() {
        return getPreferModeStat().getPp().floatValue();
    }

    @Override
    public Long getRankScore() {
        return getPreferModeStat().getRankScore();
    }

    @Override
    public Long getTotalScore() {
        return getPreferModeStat().getTotalScore();
    }

    @Override
    public Integer getRank() {
        return getPreferModeStat().getRank();
    }

    @Override
    public Integer getPlayCount() {
        return getPreferModeStat().getPlayCount().intValue();
    }

    @Override
    public Float getAcc() {
        return getPreferModeStat().getAcc().floatValue();
    }

    @Override
    public Integer getLevel() {
        return CalLevel(getPreferModeStat().getTotalScore());
    }

    @Override
    public OsuModeEnum getMode() {
        return OsuModeEnum.get(getCurrentMode());
    }

    /**
     * 计算sb服的等级
     *
     * @param totalScore 总分数
     * @see <a href="https://github.com/ppy-sb/minato/blob/master/objects/user.py">Orgini method</a>
     */
    protected int CalLevel(long totalScore) {
        int level = 1;
        final int MAX_LEVEL = 120;
        while (true) {
            if (level > MAX_LEVEL) return level;
            //get require score
            float reqScore = getReqScore(level);
            // is total score is under require score
            if (totalScore <= reqScore) {
                return level - 1;
            } else {
                level++;
            }
        }
    }

    /**
     * 计算当前等级需要的分数
     *
     * @param level 等级
     * @see <a href="https://github.com/ppy-sb/minato/blob/master/objects/user.py">Orgini method</a>
     */
    protected float getReqScore(int level) {
        if (level <= 100) {
            if (level >= 2) {
                return BigDecimal.valueOf(5000 / 3.0 * (4 * (Math.pow(level, 3)) - 3 * (Math.pow(level, 2)) - level) + 1.25 * (Math.pow(1.8, (level - 60)))).floatValue();
            } else {
                return 1.0f;
            }
        } else {
            return BigDecimal.valueOf(26931190829L).add(BigDecimal.valueOf(1e11)).multiply(BigDecimal.valueOf((level - 100))).floatValue();
        }
    }
}

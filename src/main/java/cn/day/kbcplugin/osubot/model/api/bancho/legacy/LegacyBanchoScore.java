package cn.day.kbcplugin.osubot.model.api.bancho.legacy;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.utils.ScoreUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.dromara.hutool.core.annotation.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class LegacyBanchoScore implements IScore {

    @Alias("beatmap_id")
    private Long bid;
    private Long score;
    @Alias("maxcombo")
    private Integer maxCombo;
    private Integer count50;
    private Integer count100;
    private Integer count300;
    @Alias("countmiss")
    private Integer countMiss;
    @Alias("countkatu")
    private Integer countKatu;
    @Alias("countgeki")
    private Integer countGeki;
    private Integer perfect;
    @Alias("enabled_mods")
    private Integer mods;
    @Alias("user_id")
    private String userId;
    private LocalDateTime date;
    private String rank;
    //额外字段，需要手动指定
    private OsuModeEnum mode;

    @Nullable
    @Override
    public Long scoreId() {
        return null;
    }

    @Override
    public Long Score() {
        return score;
    }
    @Nullable
    @Override
    public Float PP() {
        return null;
    }
    @Nullable
    @Override
    public Float Acc() {
        return BigDecimal.valueOf(ScoreUtil.genAccDouble(this,mode().index)).floatValue();
    }
    @Nullable
    @Override
    public Float AimPP() {
        return 0f;
    }
    @Nullable
    @Override
    public Float SpeedPP() {
        return 0f;
    }
    @Nullable
    @Override
    public Float AccPP() {
        return 0f;
    }
    @Nullable
    @Override
    public Float MaxPP() {
        return 0f;
    }
    @Nullable
    @Override
    public Float FcPP() {
        return 0f;
    }

    @Override
    public Integer maxCombo() {
        return maxCombo;
    }

    @Override
    public Integer mods() {
        return mods;
    }

    @Override
    public Integer Count300() {
        return count300;
    }

    @Override
    public Integer Count100() {
        return count100;
    }

    @Override
    public Integer Count50() {
        return count50;
    }

    @Override
    public Integer CountMiss() {
        return countMiss;
    }

    @Override
    public Integer CountGeki() {
        return countGeki;
    }

    @Override
    public Integer CountKatu() {
        return countKatu;
    }

    @Override
    public String Grade() {
        return rank;
    }

    @Nullable
    @Override
    public OsuModeEnum mode() {
        return mode;
    }

    @Override
    public boolean isPerfect() {
        return perfect==1;
    }

    @Override
    public Long beatmapId() {
        return bid;
    }

    @Nullable
    @Override
    public Long beatmapSetId() {
        return null;
    }

    @Override
    public LocalDateTime date() {
        return date;
    }

    @Override
    public LegacyBanchoScore setMode(OsuModeEnum mode) {
        this.mode = mode;
        return this;
    }
}

package cn.day.kbcplugin.osubot.model.api.sb;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.base.IScore;

import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.dromara.hutool.core.annotation.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class SbScoreInfo implements IScore {

    private Long id;
    private Long score;
    private BigDecimal pp;
    private BigDecimal acc;
    @Alias("max_combo")
    private Integer maxCombo;
    private Integer mods;
    private Integer n300;
    private Integer n50;
    private Integer n100;
    @Alias("nmiss")
    private Integer nMiss;
    @Alias("ngeki")
    private Integer nGeki;
    @Alias("nkatu")
    private Integer nKatu;
    private String grade;
    private Integer status;
    private Integer mode;
    @Alias("play_time")
    private LocalDateTime playTime;
    @Alias("time_elapsed")
    private Long timeElapsed;
    private Integer perfect;
    private SbBeatmapInfo beatmap;

    @Override
    public Long scoreId() {
        return id;
    }

    @Override
    public Long Score() {
        return score;
    }

    @Override
    public Float PP() {
        return pp.floatValue();
    }

    @Override
    public Float Acc() {
        return acc.floatValue();
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
        return n300;
    }

    @Override
    public Integer Count100() {
        return n100;
    }

    @Override
    public Integer Count50() {
        return n50;
    }

    @Override
    public Integer CountMiss() {
        return nMiss;
    }

    @Override
    public Integer CountGeki() {
        return nGeki;
    }

    @Override
    public Integer CountKatu() {
        return nKatu;
    }

    @Override
    public String Grade() {
        return grade;
    }

    @Override
    public OsuModeEnum mode() {
        return OsuModeEnum.get(mode);
    }

    @Override
    public boolean isPerfect() {
        return perfect.equals(1);
    }

    @Override
    public Long beatmapId() {
        return beatmap.getBid();
    }

    @Override
    public Long beatmapSetId() {
        return beatmap.getSetId();
    }

    @Override
    public LocalDateTime date() {
        return playTime;
    }

    @Override
    public SbScoreInfo setMode(OsuModeEnum mode) {
        this.mode = mode.ordinal();
        return this;
    }
}

package cn.day.kbcplugin.osubot.model.api.bancho.legacy;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class LegacyBanchoUserInfo implements IUserInfo {

    @Alias("user_id")
    private String id;
    private String username;
    //UTC time(thinking convent)
    @Alias("join_date")
    private String joinDate;
    @Alias("count300")
    private Integer n300;
    @Alias("count100")
    private Integer n100;
    @Alias("count50")
    private Integer n50;
    @Alias("playcount")
    private Integer playCount;
    @Alias("ranked_score")
    private Long rankedScore;
    @Alias("total_score")
    private Long totalScore;
    @Alias("pp_rank")
    private Integer rank;
    @Alias("level")
    private BigDecimal Blevel;
    @Alias("pp_raw")
    private BigDecimal pp;
    private BigDecimal accuracy;
    @Alias("count_rank_ssh")
    private Integer SSHCount;
    @Alias("count_rank_ss")
    private Integer SSCount;
    @Alias("count_rank_sh")
    private Integer SHCount;
    @Alias("count_rank_s")
    private Integer sCount;
    @Alias("count_rank_a")
    private Integer ACount;
    private String country;
    @Alias("total_seconds_played")
    private Long totalSecondsPlayed;
    @Alias("pp_country_rank")
    private Integer countryRank;
    private List<LegacyBanchoUserEvent> events;
    private OsuModeEnum mode;


    @Override
    public String getUserId() {
        return id;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public Float getPP() {
        return pp.floatValue();
    }

    @Override
    public Long getRankScore() {
        return rankedScore;
    }

    @Override
    public Float getAcc() {
        return accuracy.floatValue();
    }

    @Override
    public Integer getLevel() {
        return Blevel.intValue();
    }

    @Nullable
    @Override
    public OsuModeEnum getMode() {
        return mode;
    }
}

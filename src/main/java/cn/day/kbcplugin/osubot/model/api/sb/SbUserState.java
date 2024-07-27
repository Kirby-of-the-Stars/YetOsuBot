package cn.day.kbcplugin.osubot.model.api.sb;

import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class SbUserState {
    private Long id;
    private Integer mode;
    @Alias("tscore")
    private Long totalScore;
    @Alias("rscore")
    private Long rankScore;
    private BigDecimal pp;
    @Alias("plays")
    private Long playCount;
    @Alias("playtime")
    private long playTime;
    private BigDecimal acc;
    @Alias("max_combo")
    private Integer maxCombo;
    @Alias("total_hits")
    private Long totalHits;
    @Alias("replay_views")
    private Integer replayViews;
    @Alias("xh_count")
    private Integer SSHCount;
    @Alias("x_count")
    private Integer SSCount;
    @Alias("sh_count")
    private Integer SHCount;
    @Alias("s_count")
    private Integer sCount;
    @Alias("a_count")
    private Integer ACount;
    @Alias("rank")
    private Integer rank;
    @Alias("country_rank")
    private Integer countryRank;
}

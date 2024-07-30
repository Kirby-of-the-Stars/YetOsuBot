package cn.day.kbcplugin.osubot.model.api.chimu;

import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.hutool.core.annotation.Alias;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ChimuBeatmapInfo implements IBeatmap {
    @Alias("beatmapset_id")
    private Long sid;
    @Alias("difficulty_rating")
    private BigDecimal difficultyRating;
    @Alias("id")
    private Long bid;
    private String mode;
    private String status;
    private String version;
    @Alias("accuracy")
    private BigDecimal od;
    private BigDecimal ar;
    private BigDecimal cs;
    private BigDecimal bpm;
    @Alias("drain")
    private BigDecimal hp;
    @Alias("max_combo")
    private Integer maxCombo;
    @Alias("last_updated")
    private BigDecimal lastUpdated;
    //self add
    private String titleUnicode;
    private String artistUnicode;
    @Override
    public String getArtist() {
        return artistUnicode;
    }
    @Override
    public String getTitle() {
        return titleUnicode;
    }
    @Override
    public String getCreator() {
        return "";
    }
}

package cn.day.kbcplugin.osubot.model.api.chimu;


import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.hutool.core.annotation.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//tooooooo many field , only impl which may use
@Data
@Accessors(chain = true)
public class ChimuBeatmap {
    @Alias("id")
    private Long sid;
    private String artist;
    @Alias("artist_unicode")
    private String artistUnicode;
    private String creator;
    private String title;
    @Alias("title_unicode")
    private String titleUnicode;
    private ChimuMapCover covers;
    private String status;
    private BigDecimal bpm;
    private Integer ranked;
    @Alias("ranked_date")
    private LocalDateTime rankedDate;
    private List<ChimuBeatmapInfo> beatmaps;
}

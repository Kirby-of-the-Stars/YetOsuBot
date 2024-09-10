package cn.day.kbcplugin.osubot.model.api.bancho.legacy;

import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.hutool.core.annotation.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LegacyBanchoBeatmap implements IBeatmap {

    private Integer approved;
    @Alias("submit_date")
    private LocalDateTime submitDate;
    @Alias("approved_date")
    private LocalDateTime approvedDate;
    @Alias("last_update")
    private LocalDateTime lastUpdate;
    private String artist;
    @Alias("beatmap_id")
    private Long beatmapId;
    @Alias("beatmapset_id")
    private Long beatmapsetId;
    private BigDecimal bpm;
    private String creator;
    @Alias("creator_id")
    private String creatorId;
    @Alias("difficultyrating")
    private BigDecimal difficultyRating;
    @Alias("diff_aim")
    private BigDecimal diffAim;
    @Alias("diff_speed")
    private BigDecimal diffSpeed;
    @Alias("diff_size")
    private BigDecimal diffSize;//aka cs
    @Alias("diff_overall")
    private BigDecimal diffOverall;//aka od
    @Alias("diff_approach")
    private BigDecimal diffApproach;//aka ar
    @Alias("diff_drain")
    private BigDecimal diffDrain;//aka hp
    @Alias("hit_length")
    private Long hitLength;//second
    private String source;
    // 0 = any, 1 = unspecified, 2 = video game, 3 = anime, 4 = rock, 5 = pop, 6 = other, 7 = novelty, 9 = hip hop, 10 = electronic, 11 = metal, 12 = classical, 13 = folk, 14 = jazz (note that there's no 8)
    @Alias("genre_id")
    private String genreId;
    // 0 = any, 1 = unspecified, 2 = english, 3 = japanese, 4 = chinese, 5 = instrumental, 6 = korean, 7 = french, 8 = german, 9 = swedish, 10 = spanish, 11 = italian, 12 = russian, 13 = polish, 14 = other
    @Alias("language_id")
    private String languageId;
    private String title;
    @Alias("total_length")
    private String totalLength;//second
    private String version;
    @Alias("file_md5")
    private String fileMd5;
    private Integer mode;
    private String tags;
    @Alias("favourite_count")
    private Integer favouriteCount;
    private BigDecimal rating;
    private Integer playcount;
    private Integer passcount;
    @Alias("count_normal")
    private Integer countNormal;
    @Alias("count_slider")
    private Integer countSlider;
    @Alias("count_spinner")
    private Integer countSpinner;
    @Alias("max_combo")
    private Integer maxCombo;
    private Integer storyboard;
    private Integer video;
    @Alias("download_unavailable")
    private Integer downloadUnavailable;
    @Alias("audio_unavailable")
    private Integer audioUnavailable;

    @Override
    public Long getSid() {
        return beatmapsetId;
    }

    @Override
    public Long getBid() {
        return beatmapId;
    }
}

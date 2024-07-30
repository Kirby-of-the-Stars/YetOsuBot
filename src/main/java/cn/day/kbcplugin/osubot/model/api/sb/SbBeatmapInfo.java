package cn.day.kbcplugin.osubot.model.api.sb;

import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SbBeatmapInfo implements IBeatmap {

    private Long id;
    private String server;
    @Alias("set_id")
    private Long setId;
    private Integer status;
    private String md5;
    private String artist;
    private String title;
    private String version;
    private String creator;
    private String filename;
    @Alias("last_update")
    private LocalDateTime lastUpdate;
    @Alias("total_length")
    private Long totalLength;
    @Alias("max_combo")
    private Integer maxCombo;
    private boolean frozen;
    private Integer plays;
    private Integer passes;
    private Integer mode;
    private BigDecimal bpm;
    private BigDecimal cs;
    private BigDecimal ar;
    private BigDecimal od;
    private BigDecimal hp;
    private BigDecimal diff;

    @Override
    public Long getSid() {
        return setId;
    }

    @Override
    public Long getBid() {
        return id;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Integer getMaxCombo() {
        return maxCombo;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getCreator() {
        return creator;
    }
}

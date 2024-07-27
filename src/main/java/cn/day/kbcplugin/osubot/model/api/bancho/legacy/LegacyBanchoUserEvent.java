package cn.day.kbcplugin.osubot.model.api.bancho.legacy;

import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户信息当中的<最近活动>
 */
@Data
@Accessors(chain = true)
public class LegacyBanchoUserEvent {
    @Alias("display_html")
    private String html;
    @Alias("beatmap_id")
    private String bid;
    @Alias("beatmapset_id")
    private String sid;
    private LocalDateTime date;
    private Integer epicfactor;
}

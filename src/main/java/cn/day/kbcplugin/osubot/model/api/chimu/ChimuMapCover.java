package cn.day.kbcplugin.osubot.model.api.chimu;

import lombok.Data;
import org.dromara.hutool.core.annotation.Alias;

@Data
public class ChimuMapCover {
    private String cover;
    @Alias("cover@2x")
    private String coverAt2x;
    private String card;
    @Alias("card@2x")
    private String cardAt2x;
    private String list;
    @Alias("list@2x")
    private String listAt2x;
    @Alias("slimcover@2x")
    private String slimcover;
    private String slimcoverAt2x;
}

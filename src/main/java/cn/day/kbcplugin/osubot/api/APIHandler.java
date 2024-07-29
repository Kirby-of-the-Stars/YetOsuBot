package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IAPIHandler;
import cn.day.kbcplugin.osubot.api.base.IBeatMapBGProvider;
import cn.day.kbcplugin.osubot.api.base.IBeatmapDownLoadProvider;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class APIHandler {
    @Setter
    @Getter
    private static ChimuAPI chimuAPI = new ChimuAPI();
    @Setter
    @Getter
    private static LegacyBanchoAPI legacyBanchoAPI;
    @Setter
    @Getter
    private static SBApi sbApi;
    @Setter
    @Getter
    private static IAPIHandler mapInfoProvider;
    @Setter
    @Getter
    private static IBeatmapDownLoadProvider beatmapDownLoadProvider = chimuAPI;//as default
    @Setter
    @Getter
    private static IBeatMapBGProvider beatMapBGProvider = chimuAPI;

    public static IAPIHandler getAPI(ServerEnum server) {
        return switch (server) {
            case Bancho -> legacyBanchoAPI;
            case ppySb -> sbApi;
        };
    }

    public static String getAvatar(ServerEnum server,String osuId) {
        return switch (server) {
            case Bancho -> legacyBanchoAPI.getUserAvatar(osuId);
            case ppySb -> sbApi.getUserAvatar(osuId);
        };
    }
}

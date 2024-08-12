package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IAPIHandler;
import cn.day.kbcplugin.osubot.api.base.IBeatMapBGProvider;
import cn.day.kbcplugin.osubot.api.base.IBeatmapDownLoadProvider;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum APIHandler {
    INSTANCE {
        @Override
        public IAPIHandler getAPI(ServerEnum server) {
            return switch (server) {
                case Bancho -> this.legacyBanchoAPI;
                case ppySb -> sbApi;
            };
        }

        @Override
        public String getAvatar(ServerEnum server, String osuId) {
            return switch (server) {
                case Bancho -> legacyBanchoAPI.getUserAvatar(osuId);
                case ppySb -> sbApi.getUserAvatar(osuId);
            };
        }

        @Override
        public void init(ChimuAPI chimuAPI, LegacyBanchoAPI legacyBanchoAPI, SBApi sbApi) {
            this.chimuAPI = chimuAPI;
            this.legacyBanchoAPI = legacyBanchoAPI;
            this.sbApi = sbApi;
            this.mapInfoProvider = legacyBanchoAPI;
            this.beatmapDownLoadProvider = chimuAPI;
            this.beatMapBGProvider = chimuAPI;
        }
    };
    protected ChimuAPI chimuAPI;
    protected LegacyBanchoAPI legacyBanchoAPI;
    protected SBApi sbApi;
    protected IAPIHandler mapInfoProvider;
    @Setter
    protected IBeatmapDownLoadProvider beatmapDownLoadProvider;//as default
    @Setter
    protected IBeatMapBGProvider beatMapBGProvider;

    public abstract void init(ChimuAPI chimuAPI, LegacyBanchoAPI legacyBanchoAPI, SBApi sbApi);

    public abstract IAPIHandler getAPI(ServerEnum server);

    public abstract String getAvatar(ServerEnum server, String osuId);
}

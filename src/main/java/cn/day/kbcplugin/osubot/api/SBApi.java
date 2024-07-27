package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IAPIHandler;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.model.api.sb.SbBeatmapInfo;
import cn.day.kbcplugin.osubot.model.api.sb.SbScoreInfo;
import cn.day.kbcplugin.osubot.model.api.sb.SbUserInfo;
import cn.day.kbcplugin.osubot.model.api.sb.SbUserState;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import cn.day.kbcplugin.osubot.utils.URLBuilder;

import okhttp3.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SBApi implements IAPIHandler {

    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://api.ppy.sb";
    private static final Log logger = LogFactory.getLog("[ppy.sb API]");

    @Nullable
    @Override
    public IUserInfo getUserInfo(String osuId, OsuModeEnum mode) {
        final String url = StrUtil.format("{}{}", BASE_URL, "/v1/get_player_info");
        try {
            HttpUrl httpUrl = URLBuilder.builder(url)
                    .put("scope", "all")
                    .put("id", osuId)
                    .build();
            JSONObject response = getRaw(httpUrl);
            JSONObject player = response.getJSONObject("player");
            SbUserInfo userInfo = player.getJSONObject("info").toBean(SbUserInfo.class);
            Map<String, Object> rawMap = player.getJSONObject("stats").getRaw();
            Map<OsuModeEnum, SbUserState> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                if(entry.getValue() instanceof JSONObject val){
                    SbUserState state = val.toBean(SbUserState.class);
                    map.put(OsuModeEnum.get(Integer.parseInt(entry.getKey())), state);
                }
            }
            userInfo.setStats(map);
            if (mode != null) userInfo.setCurrentMode(mode.index);
            return userInfo;
        } catch (IOException e) {
            logger.error("获取用户信息失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Nullable
    @Override
    public IBeatmap getBeatmap(String bid, String sid) {
        //query by sid;
        final String url = StrUtil.format("{}{}{}", BASE_URL, "/v2/maps/", bid);
        try {
            return get(url, SbBeatmapInfo.class,"data");
        } catch (IOException e) {
            logger.error("获取铺面信息失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Nullable
    @Override
    public IScore getRecentScore(String osuId, OsuModeEnum mode) {
        final String url = StrUtil.format("{}{}", BASE_URL, "/v1/get_player_scores");
        try {
            HttpUrl httpUrl = URLBuilder.builder(url)
                    .put("scope", "recent")
                    .put("id", osuId)
                    .put("mode", String.valueOf(mode.index))
                    .put("limit", "1")
                    .put("include_loved", "true")
                    .put("include_failed", "true")
                    .build();
            return getList(httpUrl, SbScoreInfo.class, "scores").getFirst();
        } catch (IOException e) {
            logger.error("获取成绩信息失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public List<SbScoreInfo> getTopNScores(String osuId, OsuModeEnum mode, int count) {
        final String url = StrUtil.format("{}{}", BASE_URL, "/v1/get_player_scores");
        try {
            HttpUrl httpUrl = URLBuilder.builder(url)
                    .put("scope", "recent")
                    .put("id", osuId)
                    .put("mode", String.valueOf(mode.index))
                    .put("limit", "1")
                    .put("include_loved", "true")
                    .put("include_failed", "true")
                    .build();
            return getList(httpUrl, SbScoreInfo.class, "scores");
        } catch (IOException e) {
            logger.error("获取成绩信息失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public String getName() {
        return "ppy.sbAPI";
    }

    private <T> T get(String url, Class<T> clazz,String dataPath) throws Exception {
        HttpUrl httpUrl = URLBuilder.builder(url).build();
        return get(httpUrl, clazz,dataPath);
    }

    private <T> T get(HttpUrl httpUrl, Class<T> clazz,String dataPath) throws Exception {
        JSONObject response = getRaw(httpUrl);
        return response.getJSONObject(dataPath).toBean(clazz);
    }

    private <T> List<T> getList(HttpUrl httpUrl, Class<T> clazz,String dataPath) throws Exception {
        JSONObject response = getRaw(httpUrl);
        return response.getJSONArray(dataPath).toList(clazz);
    }

    private JSONObject getRaw(HttpUrl httpUrl) throws Exception {
        Request request = new Request.Builder()
                .url(httpUrl)
                .get().build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            ResponseBody resBody = response.body();
            if (resBody == null) throw new IOException("Empty Response" + response);
            String rawBody = IoUtil.read(resBody.byteStream(), StandardCharsets.UTF_8);
            JSONObject resp = JSONUtil.parseObj(rawBody);
            String status = resp.getStr("status", null);
            if (status == null) throw new IOException("Response status is null");
            if (!status.equals("success")) {
                if (resp.containsKey("error")) {
                    throw new IOException("Response error:" + resp.getStr("error"));
                } else {
                    throw new IOException("Response error:" + status);
                }
            }
            return resp;
        }
    }

}

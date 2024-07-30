package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IAPIHandler;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.exception.APIException;
import cn.day.kbcplugin.osubot.model.api.bancho.legacy.LegacyBanchoBeatmap;
import cn.day.kbcplugin.osubot.model.api.bancho.legacy.LegacyBanchoScore;
import cn.day.kbcplugin.osubot.model.api.bancho.legacy.LegacyBanchoUserInfo;
import cn.day.kbcplugin.osubot.utils.URLBuilder;

import okhttp3.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Bancho api v1 的实现
 *
 * @author DAYGood_Time
 * @see <a href=https://github.com/ppy/osu-api/wiki>Legacy Api v1 doc</a>
 */
public class LegacyBanchoAPI implements IAPIHandler {

    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://osu.ppy.sh/api";
    private static final String AVATAR_URL = "https://a.ppy.sh";
    private static final Log logger = LogFactory.getLog("[Bancho Legacy API]");
    private final String apiKey;

    public LegacyBanchoAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    @Nullable
    @Override
    public LegacyBanchoUserInfo getUserInfo(String osuId, OsuModeEnum mode) {
        int modeIndex = mode.index;
        if (modeIndex >= 4) throw new APIException("BanchoAPI only support 0~3 mode");
        final String url = StrUtil.format("{}{}", BASE_URL, "/get_user");
        try {
            Request request = urlBuilder(url)
                    .put("u", osuId)
                    .put("type", "id")
                    .put("m", String.valueOf(modeIndex))
                    .buildWithGetRequest();
            List<LegacyBanchoUserInfo> result = getList(request, LegacyBanchoUserInfo.class);
            if (result == null) return null;
            return result.getFirst();
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
    public LegacyBanchoBeatmap getBeatmap(String bid, String sid) {
        //use bid
        final String url = StrUtil.format("{}{}", BASE_URL, "/get_beatmaps");
        try {
            Request request = urlBuilder(url)
                    .put("b", bid)
                    .put("a", "1")//include convent map
                    .buildWithGetRequest();
            List<LegacyBanchoBeatmap> result = getList(request, LegacyBanchoBeatmap.class);
            if (result == null || result.isEmpty()) return null;
            return result.getFirst();
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
    public LegacyBanchoScore getRecentScore(String osuId, OsuModeEnum mode) {
        final String url = StrUtil.format("{}{}", BASE_URL, "/get_user_recent");
        try {
            Request request = urlBuilder(url)
                    .put("u", osuId)
                    .put("m", String.valueOf(mode.index))
                    .put("limit ", "1")
                    .buildWithGetRequest();
            List<LegacyBanchoScore> result = getList(request, LegacyBanchoScore.class);
            if (result == null || result.isEmpty()) return null;
            return result.getFirst().setMode(mode);
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
    public List<LegacyBanchoScore> getTopNScores(String osuId, OsuModeEnum mode, int count) {
        final String url = StrUtil.format("{}{}", BASE_URL, "/get_user_best");
        try {
            Request request = urlBuilder(url)
                    .put("u", osuId)
                    .put("m", String.valueOf(mode.index))
                    .put("limit", String.valueOf(count))
                    .put("type", "id")
                    .buildWithGetRequest();
            List<LegacyBanchoScore> scoreList = getList(request, LegacyBanchoScore.class);
            if(scoreList == null || scoreList.isEmpty()) return null;
            scoreList.forEach((s)->s.setMode(mode));
            return scoreList;
        } catch (IOException e) {
            logger.error("获取成绩失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public String getUserAvatar(String osuId) {
        return StrUtil.format("{}/{}",AVATAR_URL,osuId);
    }

    @Override
    public String getName() {
        return "BanchoAPI(v1)";
    }

    private JSONArray getRawList(Request request) throws Exception {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            ResponseBody resBody = response.body();
            if (resBody == null) throw new IOException("Empty Response" + response);
            String rawBody = IoUtil.read(resBody.byteStream(), StandardCharsets.UTF_8);
            return JSONUtil.parseArray(rawBody);
        }
    }

    //default builder
    private URLBuilder urlBuilder(String url) {
        return URLBuilder.builder(url).put("k", apiKey);
    }

    private <T> List<T> getList(Request request, Class<T> clazz) throws Exception {
        JSONArray rawList = getRawList(request);
        if (rawList.isEmpty()) return null;
        return rawList.toList(clazz);
    }

}
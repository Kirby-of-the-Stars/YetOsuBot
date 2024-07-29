package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.base.IBeatMapBGProvider;
import cn.day.kbcplugin.osubot.api.base.IBeatmapDownLoadProvider;
import cn.day.kbcplugin.osubot.utils.URLBuilder;
import okhttp3.*;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.File;
import java.io.IOException;

public class ChimuAPI implements IBeatmapDownLoadProvider, IBeatMapBGProvider {

    public static final String BASE_URL = "https://catboy.best";
    public static final String BG_URL = "/preview/background/";
    private static final String API_VERSION = "/api/v2";
    private final OkHttpClient client = new OkHttpClient();
    private static final Log logger = LogFactory.getLog("[Chimu API]");

    @Override
    public boolean downloadMap(String beatmapId, File target) {
        final String url = StrUtil.format("{}{}{}", BASE_URL, "/osu/", beatmapId);
        try {
            HttpUrl httpUrl = URLBuilder.builder(url).build();
            Request request = new Request.Builder().url(httpUrl).get().build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                ResponseBody resBody = response.body();
                if (resBody == null) throw new IOException("Empty Response" + response);
                MediaType contentType = resBody.contentType();
                if (contentType == null) throw new IOException("Empty Response Null Content Type" + response);
                if (contentType.equals(MediaType.parse("application/json"))) {
                    String error = JSONUtil.parseObj(resBody.string()).getStr("error");
                    throw new IOException("Bad Request:" + error);
                }
                FileUtil.touch(target);
                FileUtil.writeBytes(resBody.bytes(), target);
                return true;
            }
        } catch (IOException e) {
            logger.error("下载地图失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public String getName() {
        return "Chimu API";
    }


    @Override
    public File downloadBG(String beatmapId, File target) {
        final String url = StrUtil.format("{}{}{}", BASE_URL, BG_URL, beatmapId);
        try {
            HttpUrl httpUrl = URLBuilder.builder(url).build();
            Request request = new Request.Builder().url(httpUrl).get().build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                ResponseBody resBody = response.body();
                if (resBody == null) throw new IOException("Empty Response" + response);
                MediaType contentType = resBody.contentType();
                //okhttp 把非标准的contentType 判断为了null
                if (contentType == null && resBody.contentLength() == 0)
                    throw new IOException("Empty Response Null Content Type" + response);
                else if (MediaType.get("application/json").equals(contentType)) {
                    String error = JSONUtil.parseObj(resBody.string()).getStr("error");
                    throw new IOException("Bad Request:" + error);
                }
                // observer which is none png file
                // current : jgp "(Nico" (not stand type)
                if (contentType != null && !MediaType.get("image/png").equals(contentType)) {
                    logger.warn("some file doesn't support image/png :{}", contentType);
                }
                target = new File(target, beatmapId + "-bg" + ".png");
                FileUtil.touch(target);
                FileUtil.writeBytes(resBody.bytes(), target);
                return target;
            }
        } catch (IOException e) {
            logger.error("下载地图失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return null;
    }
}
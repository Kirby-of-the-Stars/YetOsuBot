package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IBeatMapBGProvider;
import cn.day.kbcplugin.osubot.api.base.IBeatmapDownLoadProvider;
import cn.day.kbcplugin.osubot.interceptor.RetryInterceptor;
import cn.day.kbcplugin.osubot.utils.URLBuilder;
import okhttp3.*;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.client.engine.httpclient5.HttpClient5Engine;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChimuAPI implements IBeatmapDownLoadProvider, IBeatMapBGProvider {

    public static final String BASE_URL = "https://catboy.best";
    public static final String BG_URL = "/preview/background/";
    private static final String API_VERSION = "/api/v2";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(10L, TimeUnit.SECONDS)//add readTimeout limit for resource download
            .addInterceptor(new RetryInterceptor(3))
            .build();
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

    private static final List<String> subTypeList = Arrays.asList("png", "jpeg", "jpg");

    @Override
    public File downloadBG(String beatmapId, File target) {
        final String url = StrUtil.format("{}{}{}", BASE_URL, BG_URL, beatmapId);
        try {
            HttpUrl httpUrl = URLBuilder.builder(url).build();
            Request request = new Request.Builder().url(httpUrl).get().build();
            long current = System.currentTimeMillis();
            logger.info("开始下载地图:{}", beatmapId);
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
                if (contentType == null) {
                    //add default type
                    contentType = MediaType.get("image/png");
                }
                //if it has not image type?
                String type = contentType.type();
                if (!type.equals("image")) {
                    logger.warn("{} is not image type,return null result", type);
                    return null;
                }
                // observer which is none png file
                // current : jgp "(Nico" (not stand type)
                String prefix;
                prefix = contentType.subtype();
                if (!subTypeList.contains(prefix)) {
                    logger.warn("{} dit not png or jpeg,try to use png", prefix);
                    prefix = "png";
                }
                target = new File(target, beatmapId + "-bg" + "." + prefix);
                FileUtil.touch(target);
                FileUtil.writeBytes(resBody.bytes(), target);
                logger.info("下载完成用时:{}s", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - current));
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

    public File downloadBGWithHutool(String beatmapId, File target) {
        final String url = StrUtil.format("{}{}{}", BASE_URL, BG_URL, beatmapId);
        logger.info("开始下载地图:{}", beatmapId);
        long current = System.currentTimeMillis();
        org.dromara.hutool.http.client.Request request =
                org.dromara.hutool.http.client.Request.of(url)
                        .method(Method.GET);
        org.dromara.hutool.http.client.Response response = request.send(new HttpClient5Engine());
        FileUtil.touch(target);
        FileUtil.writeBytes(response.bodyBytes(), target);
        logger.info("下载地图完成，用时:{}", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - current));
        return target;
    }
}
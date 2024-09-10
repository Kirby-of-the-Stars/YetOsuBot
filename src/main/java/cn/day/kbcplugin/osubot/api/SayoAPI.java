package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.api.base.IBeatMapBGProvider;
import cn.day.kbcplugin.osubot.api.base.IBeatmapDownLoadProvider;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.utils.URLBuilder;
import okhttp3.*;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.UrlEncoder;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SayoAPI implements IBeatMapBGProvider, IBeatmapDownLoadProvider {

    public static final String BASE_URL = "https://dl.sayobot.cn/beatmaps/files/";
    public static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final String FIXED_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36";
    private static final List<String> imagePrefixes = Arrays.asList("jpg", "png", "webp");
    private static final Log logger = LogFactory.getLog("[Sayo API]");

    @Override
    public File downloadBG(IBeatmap beatmap, File target) {
        logger.info("开始下载背景图:{}", beatmap.getSid());
        final String fileListUrl = StrUtil.format("{}{}", BASE_URL, beatmap.getSid());
        try {
            //getFiles info
            List<JSONObject> array = JSONUtil.parseArray(HttpUtil.get(fileListUrl)).toList(JSONObject.class);
            Optional<JSONObject> bgObj = array.stream().filter(obj -> imagePrefixes.contains(obj.getStr("name"))).findFirst();
            if (bgObj.isEmpty()) throw new IOException("Bid:" + beatmap.getSid() + " cant find the background image");
            JSONObject bg = bgObj.get();
            final String url = StrUtil.format("{}{}{}", BASE_URL, beatmap.getSid(), bg.getStr("name"));
            HttpUrl httpUrl = URLBuilder.builder(UrlEncoder.encodeAll(url)).build();
            Request request = new Request.Builder().url(httpUrl)
                    .addHeader("User-Agent", FIXED_USER_AGENT)
                    .addHeader("Referer", "https://github.com/Kirby-of-the-Stars/YetOsuBot")
                    .get().build();
            try (Response response = httpClient.newCall(request).execute()) {
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
                FileUtil.writeBytes(IoUtil.readBytes(resBody.byteStream()), target);
                return target;
            }
        } catch (IOException e) {
            logger.error("下载铺面失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public boolean downloadMap(IBeatmap beatmap, File target) {
        final String fileListUrl = StrUtil.format("{}{}", BASE_URL, beatmap.getSid());
        logger.info("开始下载铺面文件:{}", beatmap.getBid());
        try {
            final String version = beatmap.getVersion();
            //getFiles info
            org.dromara.hutool.http.client.Request fileListRequest = HttpUtil.createGet(fileListUrl);
            String redirect;
            try (org.dromara.hutool.http.client.Response response = fileListRequest.send()) {
                redirect = response.header("Location");
            }
            List<JSONObject> array = JSONUtil.parseArray(HttpUtil.get(redirect)).toList(JSONObject.class);
            Optional<JSONObject> bgObj = array.stream().filter(obj -> {
                String name = obj.getStr("name");
                return name.contains(version) && name.contains(".osu");
            }).findFirst();
            if (bgObj.isEmpty()) throw new IOException("Bid:" + beatmap.getSid() + " cant find the background image");
            JSONObject bg = bgObj.get();
            final String url = StrUtil.format("{}{}/{}", BASE_URL, beatmap.getSid(), UrlEncoder.encodeAll(bg.getStr("name")));
            HttpUrl httpUrl = URLBuilder.builder(url).build();
            Request request = new Request.Builder().url(httpUrl)
                    .addHeader("User-Agent", FIXED_USER_AGENT)
                    .addHeader("Referer", "https://github.com/Kirby-of-the-Stars/YetOsuBot")
                    .get().build();
            try (Response response = httpClient.newCall(request).execute()) {
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
                FileUtil.writeBytes(IoUtil.readBytes(resBody.byteStream()), target);
                return true;
            }
        } catch (IOException e) {
            logger.error("下载铺面失败:{}", e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON解析异常:{}", e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error("API意外异常:{}", e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public String getName() {
        return "SayoAPI";
    }
}

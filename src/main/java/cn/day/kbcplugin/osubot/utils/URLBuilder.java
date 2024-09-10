package cn.day.kbcplugin.osubot.utils;

import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class URLBuilder {

    private final Map<String, String> param = new HashMap<>();

    private final String url;

    private URLBuilder(String url) {
        this.url = url;
    }

    public static URLBuilder builder(String url) {
        return new URLBuilder(url);
    }

    public URLBuilder put(String key, String value) {
        param.put(key, value);
        return this;
    }

    public URLBuilder putAll(Map<String, String> map) {
        param.putAll(map);
        return this;
    }

    public HttpUrl build() throws IOException {
        HttpUrl parse = HttpUrl.parse(url);
        if (parse == null) throw new IOException("bad url:" + url);
        HttpUrl.Builder builder = parse.newBuilder();
        if (!param.isEmpty()) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                builder.addEncodedQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public Request buildWithGetRequest() throws IOException {
        return new Request.Builder().url(build()).get().build();
    }
}

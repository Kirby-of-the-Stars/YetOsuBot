package cn.day.kbcplugin.osubot.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {

    private final int MAX_RETRIES;

    public RetryInterceptor(int maxRetries) {
        MAX_RETRIES = maxRetries;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        IOException lastException = null;
        for (int tryCount = 0; tryCount < MAX_RETRIES; tryCount++) {
            try {
                return chain.proceed(request);
            } catch (IOException e) {
                lastException = e;
            }
        }
        if(lastException != null) {
            throw lastException;
        }else {
            throw new IOException("timeout");
        }
    }
}

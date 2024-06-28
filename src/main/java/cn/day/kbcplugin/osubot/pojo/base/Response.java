package cn.day.kbcplugin.osubot.pojo.base;

import org.jetbrains.annotations.Nullable;

public class Response<T> {

    public boolean success;
    public String message;

    @Nullable
    public T data;

    public Response(boolean success, String message, @Nullable T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Response(){
        this.success = true;
        this.message = "success";
    }
}

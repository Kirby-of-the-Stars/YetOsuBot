package cn.day.kbcplugin.osubot.enums;

public enum ServerEnum {
    Bancho,
    ppysb;

    public static ServerEnum parseInt(int i){
        return ServerEnum.values()[i];
    }
}

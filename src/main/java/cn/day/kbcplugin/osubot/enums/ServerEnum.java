package cn.day.kbcplugin.osubot.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ServerEnum {
    Bancho("bancho"),
    ppySb("ppysb");


    private final String name;

    ServerEnum(String name) {
        this.name = name;
    }

    public static List<String> toNameList() {
        return Arrays.stream(ServerEnum.values()).map(ServerEnum::getName).toList();
    }

    public static String AllNames(){
        return Arrays.toString(toNameList().toArray());
    }

    @Nullable
    public static ServerEnum fromName(String name) {
        return Arrays.stream(ServerEnum.values()).filter(e->e.name.equals(name)).findFirst().orElse(null);
    }

    @EnumValue
    public int getValue() {
        return this.ordinal();
    }

    public static ServerEnum parseInt(int i) {
        return ServerEnum.values()[i];
    }
}

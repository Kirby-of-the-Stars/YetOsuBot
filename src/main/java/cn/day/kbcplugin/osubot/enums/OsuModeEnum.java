package cn.day.kbcplugin.osubot.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.List;

@Getter
public enum OsuModeEnum {
    STANDER(0, "osu"),
    TAIKO(1, "taiko"),
    CATCH(2, "catch"),
    MANIA(3, "mania"),
    STANDER_RX(4, "std_rx"),
    TAIKO_RX(5, "taiko_rx"),
    CATCH_RX(6, "catch_rx"),
    STANDER_AP(8, "std_ap");

    public final int index;
    public final String name;

    OsuModeEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }


    public static List<String> toNameList() {
        return Arrays.stream(OsuModeEnum.values()).map(OsuModeEnum::getName).toList();
    }

    public static String AllNames() {
        return Arrays.toString(toNameList().toArray());
    }

    @Nullable
    public static OsuModeEnum fromName(String name) {
        return Arrays.stream(OsuModeEnum.values()).filter(e -> e.name.equals(name)).findFirst().orElse(null);
    }

    public static OsuModeEnum get(int index) {
        if (index < 0 || index > 8) throw new IndexOutOfBoundsException();
        if (index == 8) index = 7;
        return values()[index];
    }

    @EnumValue
    public int getIndex() {
        return index;
    }
}

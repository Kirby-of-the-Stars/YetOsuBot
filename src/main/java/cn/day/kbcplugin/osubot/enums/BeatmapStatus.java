package cn.day.kbcplugin.osubot.enums;

import com.mybatisflex.annotation.EnumValue;

public enum BeatmapStatus {

    RANKED(1, "ranked"),
    LOVED(4, "loved"),
    UNRANKED(0, "unranked");

    public final int index;
    public final String name;

    BeatmapStatus(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static boolean isOnline(int index) {
        return index == 1 || index == 4;
    }

    @EnumValue
    public int getIndex() {
        return index;
    }
}

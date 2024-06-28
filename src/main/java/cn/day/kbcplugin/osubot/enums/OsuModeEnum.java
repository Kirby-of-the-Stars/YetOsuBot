package cn.day.kbcplugin.osubot.enums;

import java.util.Arrays;
import java.util.Optional;

public enum OsuModeEnum {
    STANDER(0,"stander"),
    TAIKO(1,"taiko"),
    CATCH(2,"catch"),
    MANIA(3,"mania"),
    STANDER_RX(4,"std_rx"),
    STANDER_AP(8,"std_ap");

    public final int index;
    public final String name;

    OsuModeEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static OsuModeEnum get(int index){
        Optional<OsuModeEnum> optional = Arrays.stream(OsuModeEnum.values()).findAny().filter(u -> u.index == index);
        return optional.orElse(null);
    }
}

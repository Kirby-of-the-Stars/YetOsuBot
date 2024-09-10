package cn.day.kbcplugin.osubot.model.api;

import com.sun.jna.Structure;


@Structure.FieldOrder({"path", "mods", "acc", "miss", "combo", "max_combo"})
public class OsuMap extends Structure {
    public String path;
    public Long mods;
    public double acc;
    public Long miss;
    public Long combo;
    public Long max_combo;

    public OsuMap(String path, Integer mods, double acc, Integer miss, Integer combo, Integer max_combo) {
        this.path = path;
        this.mods = Long.valueOf(mods);
        this.acc = acc;
        this.miss = Long.valueOf(miss);
        this.combo = Long.valueOf(combo);
        this.max_combo = Long.valueOf(max_combo);
    }
}

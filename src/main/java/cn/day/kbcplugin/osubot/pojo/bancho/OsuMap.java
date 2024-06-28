package cn.day.kbcplugin.osubot.pojo.bancho;

import com.sun.jna.Structure;

@Structure.FieldOrder({"path","mods","acc","miss","combo","max_combo"})
public class OsuMap extends Structure {

    public String path;

    public Long mods;
    public double acc;
    public Long miss;
    public Long combo;
    public Long max_combo;

    public OsuMap(String path, long mods, double acc, long miss, long combo, long max_combo) {
        this.path = path;
        this.mods = mods;
        this.acc = acc;
        this.miss = miss;
        this.combo = combo;
        this.max_combo = max_combo;
    }
}
package cn.day.kbcplugin.osubot.model.api.base;


import java.util.LinkedHashMap;

public class Mods {
    private long result = 0;

    public Long ModsChain() {
        if (result < 0) {
            throw new IllegalArgumentException("mods should bigger then 0");
        } else {
            return result;
        }
    }

    private Mods() {
    }

    public static Mods Builder() {
        return new Mods();
    }

    public Mods NF() {
        this.result += 1;
        return this;
    }

    public Mods EZ() {
        this.result += 2;
        return this;
    }

    public Mods TD() {
        this.result += 4;
        return this;
    }

    public Mods HD() {
        this.result += 8;
        return this;
    }

    public Mods HR() {
        this.result += 16;
        return this;
    }

    public Mods DT() {
        this.result += 64;
        return this;
    }

    public Mods RX() {
        this.result += 128;
        return this;
    }

    public Mods HT() {
        this.result += 256;
        return this;
    }

    public Mods FL() {
        this.result += 1024;
        return this;
    }

    public Mods SO() {
        this.result += 4096;
        return this;
    }

    public static Long None() {
        return 0L;
    }

    public Mods auto(Byte mode) {
        result += mode.longValue();
        return this;
    }

    public Mods auto(long mode) {
        result += mode;
        return this;
    }

    /**
     * 将Mod数字转换为字符串，用于ImgUtil类绘制Mod图标，所以不会包含Unrank Mod。
     *
     * @param mod 表示mod的数字
     * @return 带顺序的LinkedHashMap，用于存储Mod字符串（Key是简称，Value是全称(对应皮肤文件)）
     */
    public static LinkedHashMap<String, String> convertModToHashMap(Integer mod) {
        String modBin = Integer.toBinaryString(mod);
        //反转mod
        modBin = new StringBuffer(modBin).reverse().toString();
        LinkedHashMap<String, String> mods = new LinkedHashMap<>();
        char[] c = modBin.toCharArray();
        if (mod != 0) {
            for (int i = c.length - 1; i >= 0; i--) {
                //字符串中第i个字符是1,意味着第i+1个mod被开启了
                if (c[i] == '1') {
                    switch (i) {
                        case 0:
                            mods.put("NF", "nofail");
                            break;
                        case 1:
                            mods.put("EZ", "easy");
                            break;
                        //虽然TD已经实装，但是MOD图标还是 不做 不画
                        case 3:
                            mods.put("HD", "hidden");
                            break;
                        case 4:
                            mods.put("HR", "hardrock");
                            break;
                        case 5:
                            mods.put("SD", "suddendeath");
                            break;
                        case 6:
                            mods.put("DT", "doubletime");
                            break;
                        //7是RX，不会上传成绩
                        //现在有了私服之后，RX也会是成绩
                        case 7:
                            mods.put("RX", "relax");
                            break;
                        case 8:
                            mods.put("HT", "halftime");
                            break;
                        case 9:
                            mods.put("NC", "nightcore");
                            break;
                        case 10:
                            mods.put("FL", "flashlight");
                            break;
                        //11是Auto
                        case 12:
                            mods.put("SO", "spunout");
                            break;
                        //13是AutoPilot
                        case 13:
                            mods.put("AP", "autopilot");
                            break;
                        case 14:
                            mods.put("PF", "perfect");
                            break;
                        case 15:
                            mods.put("4K", "key4");
                            break;
                        case 16:
                            mods.put("5K", "key5");
                            break;
                        case 17:
                            mods.put("6K", "key6");
                            break;
                        case 18:
                            mods.put("7K", "key7");
                            break;
                        case 19:
                            mods.put("8K", "key8");
                            break;
                        case 20:
                            mods.put("FI", "fadein");
                            break;
                        //21是RD，Mania的Note重新排布
                        //22是Cinema，但是不知道为什么有一个叫LastMod的名字
                        //23是Target Practice
                        case 24:
                            mods.put("9K", "key9");
                            break;
                        //25是Mania的双人合作模式，Unrank
                        //Using 1K, 2K, or 3K mod will result in an unranked play.
                        //The mod does not work on osu!mania-specific beatmaps.
                        //26 1K，27 3K，28 2K

                        default:
                            break;
                    }
                }
            }
            if (mods.containsKey("NC")) {
                mods.remove("DT");
            }
            if (mods.containsKey("PF")) {
                mods.remove("SD");
            }
        } else {
            mods.put("None", "None");
        }
        return mods;
    }

    public static String convertModToString(Integer mod) {
        LinkedHashMap<String, String> map = convertModToHashMap(mod);
        StringBuilder sb = new StringBuilder();
        map.keySet().forEach(sb::append);
        return sb.toString();
    }
}

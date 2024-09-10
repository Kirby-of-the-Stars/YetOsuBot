package cn.day.kbcplugin.osubot.model.api.base;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Mods {
    private static final List<String> modList = Arrays.asList("HR", "NF", "EZ", "TD", "HD", "HR", "SD", "DT", "RX",
            "HT", "NC", "FL", "AT", "SO", "AP", "PF", "4K", "5K", "6K", "7K", "8K", "FI", "RM", "CN", "TG", "9K", "KC", "1K",
            "3K", "2K", "V2", "MR");
    private long result = 0;

    private Mods() {
    }

    public static Mods Builder() {
        return new Mods();
    }

    public static Long None() {
        return 0L;
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

    public static Integer StringToMod(String mods) {
        int res = 0;
        for (String mod : modList) {
            if (mods.contains(mod)) {
                res += modeInt(mod);
            }
        }
        return res;
    }

    public static int modeInt(String mod) {
        return switch (mod) {
            case "NF" -> 1;
            case "EZ" -> 2;
            case "TD" -> 4;
            case "HD" -> 8;
            case "HR" -> 16;
            case "SD" -> 32;
            case "DT" -> 64;
            case "RX" -> 128;
            case "HT" -> 256;
            case "NC" -> 512;
            case "FL" -> 1024;
            case "AT" -> 2048;
            case "SO" -> 4096;
            case "AP" -> 8192;
            case "PF" -> 16384;
            case "4K" -> 32768;
            case "5K" -> 65536;
            case "6K" -> 131072;
            case "7K" -> 262144;
            case "8K" -> 524288;
            case "FI" -> 1048576;
            case "RM" -> 2097152;
            case "CN" -> 4194304;
            case "TG" -> 8388608;
            case "9K" -> 16777216;
            case "KC" -> 33554432;
            case "1K" -> 67108864;
            case "3K" -> 134217728;
            case "2K" -> 268435456;
            case "V2" -> 536870912;
            case "MR" -> 1073741824;
            default -> 0;
        };
    }

    public Long build() {
        if (result < 0) {
            throw new IllegalArgumentException("mods should bigger then 0");
        } else {
            return result;
        }
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

    public Mods auto(Byte mods) {
        result += mods.longValue();
        return this;
    }

    public Mods auto(long mods) {
        result += mods;
        return this;
    }
}

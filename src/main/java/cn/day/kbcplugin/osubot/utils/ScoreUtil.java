package cn.day.kbcplugin.osubot.utils;


import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.BanchoAPI;
import cn.day.kbcplugin.osubot.api.ROSU_PP;
import cn.day.kbcplugin.osubot.pojo.bancho.OsuMap;
import cn.day.kbcplugin.osubot.pojo.bancho.BanchoScore;
import cn.day.kbcplugin.osubot.pojo.common.AbstractBeatmap;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.pojo.osu.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

/**
 * The type Score util.
 *
 * @author QHS
 */
public class ScoreUtil {

    private final BanchoAPI banchoApi;

    public ScoreUtil(BanchoAPI banchoApi) {
        this.banchoApi = banchoApi;
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
            if (mods.keySet().contains("NC")) {
                mods.remove("DT");
            }
            if (mods.keySet().contains("PF")) {
                mods.remove("SD");
            }
        } else {
            mods.put("None", "None");
        }
        return mods;
    }

    public static String convertModToString(Integer mod) {
        return convertModToHashMap(mod).keySet().toString().replaceAll("\\[\\]", "");
    }

    public static String genAccString(AbstractScore score, Integer mode) {
        return new DecimalFormat("###.00").format(genAccDouble(score, mode));
    }

    public static Double genAccDouble(AbstractScore score, Integer mode) {
        switch (mode) {
            case 0:
                return 100.0 * (6 * score.Count300() + 2 * score.Count100() + score.Count50())
                        / (6 * (score.Count50() + score.Count100() + score.Count300() + score.CountMiss()));
            case 1:
                //太鼓
                return 100.0 * (2 * score.Count300() + score.Count100())
                        / (2 * (score.Count100() + score.Count300() + score.CountMiss()));
            case 2:
                //ctb
                return 100.0 * (score.Count50() + score.Count100() + score.Count300())
                        / (score.CountKatu() + score.Count50() + score.Count100() + score.Count300() + score.CountMiss());
            case 3:
                //mania
                return 100.0 * (300 * (score.Count300() + score.CountGeki()) + 200 * score.CountKatu() + 100 * score.Count100() + 50 * score.Count50())
                        / (300 * (score.Count50() + score.Count100() + score.Count300() + score.CountMiss() + score.CountKatu() + score.CountGeki()));
            default:
                return 0D;
        }

    }

    /**
     * @param score   the score
     * @param beatmap the beatmap
     * @return only rosu-pp result
     * @author DAY
     */
    public OppaiResult calcPP_rosu(AbstractScore score, AbstractBeatmap beatmap) {
        Main.logger.info("开始计算PP");
        OppaiResult result = new OppaiResult();
        STDMods stdMods = STDMods.Builder();
        stdMods.auto(score.mods());
        File osu = Main.banchoApi.getOsuFile(beatmap.getBid(),beatmap.getSid());
        if(osu==null){
            Main.logger.error("无法获取到地图文件,不计算pp");
            return null;
        }
        OsuMap osuMap = new OsuMap(
                osu.getAbsolutePath(),
                stdMods.ModsChain(),
                ScoreUtil.genAccDouble(score, 0),
                score.CountMiss(),
                score.maxCombo(),
                beatmap.getMaxCombo()
        );
        PPResult ppResult = ROSU_PP.rosu_calc_pp(osuMap);
        if (ppResult == null) {
            return null;
        }
        result.setAccPp(ppResult.getPp_acc());
        result.setPp(ppResult.getPp());
        result.setSpeedPp(ppResult.getPp_speed());
        result.setAimPp(ppResult.getPp_aim());
        result.setMaxPP(ppResult.getPp_fc());
        return result;
    }


    public String convertGameModeToString(Integer mode) {
        switch (mode) {
            case 0:
                return "Standard";
            case 1:
                return "Taiko";
            case 2:
                return "Catch The Beat";
            case 3:
                return "osu!Mania";
            default:
                return null;

        }
    }
}

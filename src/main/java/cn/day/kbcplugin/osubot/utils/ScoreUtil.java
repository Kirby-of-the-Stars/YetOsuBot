package cn.day.kbcplugin.osubot.utils;


import cn.day.kbcplugin.osubot.api.RustOsuPPCalculator;
import cn.day.kbcplugin.osubot.model.api.OsuMap;
import cn.day.kbcplugin.osubot.model.api.PPResult;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import cn.day.kbcplugin.osubot.model.api.base.IScore;
import cn.day.kbcplugin.osubot.model.api.base.Mods;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Score util.
 */
public class ScoreUtil {

    private static final Log logger = LogFactory.getLog("[Score Util]");

    public static String genAccString(IScore score, Integer mode) {
        double res = genAccDouble(score, mode);
        if (res == 0.0) return "0.00";
        return new DecimalFormat("###.00").format(res);
    }

    public static Double genAccDouble(IScore score, Integer mode) {
        return switch (mode) {
            case 0, 4, 8 -> 100.0 * (6 * score.Count300() + 2 * score.Count100() + score.Count50())
                            / (6 * (score.Count50() + score.Count100() + score.Count300() + score.CountMiss()));
            case 1, 5 ->
                //太鼓
                    100.0 * (2 * score.Count300() + score.Count100())
                    / (2 * (score.Count100() + score.Count300() + score.CountMiss()));
            case 2, 6 ->
                //ctb
                    100.0 * (score.Count50() + score.Count100() + score.Count300())
                    / (score.CountKatu() + score.Count50() + score.Count100() + score.Count300() + score.CountMiss());
            case 3 ->
                //mania
                    100.0 * (300 * (score.Count300() + score.CountGeki()) + 200 * score.CountKatu() + 100 * score.Count100() + 50 * score.Count50())
                    / (300 * (score.Count50() + score.Count100() + score.Count300() + score.CountMiss() + score.CountKatu() + score.CountGeki()));
            default -> 0D;
        };

    }

    /**
     * @param score   the score
     * @param beatmap the beatmap
     * @return only rosu-pp result
     * @author DAY
     */
    public static PPResult calcPPWithRosu(IScore score, IBeatmap beatmap) {
        logger.info("开始计算PP");
        Mods stdMods = Mods.Builder();
        stdMods.auto(score.mods());
        File osu = MapHelper.getOsuFile(String.valueOf(beatmap.getBid()), String.valueOf(beatmap.getSid()));
        if (osu == null) {
            logger.error("无法获取到地图文件,不计算pp");
            return null;
        }
        OsuMap osuMap = new OsuMap(
                osu.getAbsolutePath(),
                stdMods.build().intValue(),
                score.Acc(),
                score.CountMiss(),
                score.maxCombo(),
                beatmap.getMaxCombo()
        );
        return RustOsuPPCalculator.CalPP(osuMap);
    }

    public static String convertGameModeToString(Integer mode) {
        return switch (mode) {
            case 0 -> "Standard";
            case 1 -> "Taiko";
            case 2 -> "Catch The Beat";
            case 3 -> "osu!Mania";
            default -> null;
        };
    }

    public static List<PPResult> calPPFromMap(IBeatmap beatmap, Float acc, Integer mods, Integer miss) {
        List<PPResult> results = new ArrayList<>();
        logger.info("开始计算PP");
        Mods stdMods = Mods.Builder().auto(mods);
        File osu = MapHelper.getOsuFile(String.valueOf(beatmap.getBid()), String.valueOf(beatmap.getSid()));
        if (osu == null) {
            logger.error("无法获取到地图文件,不计算pp");
            return null;
        }
        if (acc == null) {
            //multi result
            OsuMap Map = new OsuMap(
                    osu.getAbsolutePath(),
                    stdMods.build().intValue(),
                    100, 0,
                    beatmap.getMaxCombo(),
                    beatmap.getMaxCombo()
            );
            //100 acc result
            results.add(RustOsuPPCalculator.CalPP(Map));
            //98 acc result
            Map.acc = 98;
            results.add(RustOsuPPCalculator.CalPP(Map));
            //95 acc result
            Map.acc = 95;
            results.add(RustOsuPPCalculator.CalPP(Map));
        } else {
            OsuMap osuMap = new OsuMap(
                    osu.getAbsolutePath(),
                    stdMods.build().intValue(),
                    acc,
                    miss == null ? 0 : miss,
                    beatmap.getMaxCombo()-miss,
                    beatmap.getMaxCombo()
            );
            results.add(RustOsuPPCalculator.CalPP(osuMap));
        }
        return results;
    }
}

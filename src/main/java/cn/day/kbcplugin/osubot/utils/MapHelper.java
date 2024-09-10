package cn.day.kbcplugin.osubot.utils;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import org.dromara.hutool.core.io.file.FileUtil;

import java.io.File;

public class MapHelper {

    public static File getOsuFile(IBeatmap beatmap) {
        File mapFolder = new File(Main.BeatMapsPath, beatmap.getSid().toString());
        if (!mapFolder.exists() || !mapFolder.isDirectory()) {
            mapFolder.mkdirs();
        }
        File map = new File(mapFolder, beatmap.getBid().toString() + ".osu");
        if (map.exists()) return map;
        //getMap
        if (APIHandler.INSTANCE.getBeatmapDownLoadProvider().downloadMap(beatmap, map)) {
            return map;
        } else {
            //获取失败后，应该删除其文件夹,让下次获取时重新创建
            MapHelper.RemoveDir(beatmap.getSid().toString());
            return null;
        }
    }

    public static File getBgFile(IBeatmap beatmap) {
        File mapFolder = new File(Main.BeatMapsPath, beatmap.getSid().toString());
        if (!mapFolder.exists() || !mapFolder.isDirectory()) {
            mapFolder.mkdirs();
        }
        File BgFile = findFile(mapFolder, beatmap.getBid().toString() + "-bg");
        if (BgFile == null || !BgFile.exists()) {
            BgFile = APIHandler.INSTANCE.getBeatMapBGProvider().downloadBG(beatmap, mapFolder);
        }
        if (BgFile == null || !BgFile.exists()) {
            //获取失败后，应该删除其文件夹,让下次获取时重新创建
            MapHelper.RemoveDir(beatmap.getSid().toString());
        }
        return BgFile;

    }

    private static File findFile(File folder, String fileName) {
        File[] files = folder.listFiles();
        if (files == null) return null;
        File result = null;
        for (File file : files) {
            String rawName = file.getName();
            if (rawName.contains(fileName) && !rawName.contains(".osu")) {
                result = file;
                break;
            }
        }
        return result;
    }

    public static void RemoveDir(String sid) {
        File mapFolder = new File(Main.BeatMapsPath, sid);
        if (!mapFolder.exists() || !mapFolder.isDirectory()) {
            FileUtil.del(mapFolder);
        }
        //do nothing if not dir
    }
}

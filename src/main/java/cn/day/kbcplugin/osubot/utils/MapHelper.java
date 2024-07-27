package cn.day.kbcplugin.osubot.utils;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;

import java.io.File;

public class MapHelper {

    public static File getOsuFile(String bid, String sid) {
        File mapFolder = new File(Main.BeatMapsPath, String.valueOf(sid));
        if (!mapFolder.exists() || !mapFolder.isDirectory()) {
            mapFolder.mkdirs();
        }
        File map = new File(mapFolder, bid + ".osu");
        if (map.exists()) return map;
        //getMap
        if (APIHandler.getBeatmapDownLoadProvider().downloadMap(bid, map)) {
            return map;
        } else {
            return null;
        }
    }

    public static File getBgFile(String bid, String sid) {
        File mapFolder = new File(Main.BeatMapsPath, String.valueOf(sid));
        if (!mapFolder.exists() || !mapFolder.isDirectory()) {
            mapFolder.mkdirs();
        }
        File BgFile = findFile(mapFolder, bid + "-bg");
        if (BgFile == null || !BgFile.exists()) {
            BgFile = APIHandler.getBeatMapBGProvider().downloadBG(bid, mapFolder);
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
}

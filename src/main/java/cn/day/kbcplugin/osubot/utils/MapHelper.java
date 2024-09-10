package cn.day.kbcplugin.osubot.utils;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.APIHandler;
import cn.day.kbcplugin.osubot.api.SayoAPI;
import cn.day.kbcplugin.osubot.model.api.base.IBeatmap;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class MapHelper {

    private static final Log logger = LogFactory.getLog("[MapHelper]");

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
        String BgFileName = getBgFileName(beatmap);
        if(BgFileName==null) return null;
        File BgFile = new File(mapFolder,BgFileName);
        if (!BgFile.exists()) {
            BgFile = APIHandler.INSTANCE.getBeatMapBGProvider().downloadBG(beatmap, mapFolder,BgFileName);
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
    public static String getBgFileName(IBeatmap beatmap) {
        File osuFile = getOsuFile(beatmap);
        if (osuFile == null) return null;
        BufferedReader reader = FileUtil.getReader(osuFile, StandardCharsets.UTF_8);
        try{
            while (reader.ready()) {
                String line = reader.readLine();
                if(line.equals("//Background and Video events")){
                    String rawLine = reader.readLine();
                    Optional<String> res = Arrays.stream(rawLine.split(",")).filter(s-> SayoAPI.imagePrefixes.stream().anyMatch(s::contains)).findFirst();
                    return res.map(s -> s.replaceAll("\"", "")).orElse(null);
                }
            }
        } catch (IOException e) {
            logger.error("无法读取铺面文件 {}:{}",beatmap.getSid(),e.getLocalizedMessage(),e);
        }
        return null;
    }
}

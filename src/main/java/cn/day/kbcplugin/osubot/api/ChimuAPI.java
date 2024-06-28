package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.pojo.api.ChimuMap;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

//因为返回的osu文件名有特殊符号的问题，建议弃用
@Deprecated
public class ChimuAPI {

    private static final String GET_MAP_URL = "https://api.chimu.moe/v1/map/";

    private static final String DOWNLOAD_URL = "https://api.chimu.moe/v1/download/";

    public ChimuMap getMapInfo(String BeatMapId) {
        JSONObject jobj = JSONUtil.parseObj(HttpUtil.get(GET_MAP_URL + BeatMapId));
        if (jobj.containsKey("error_message")) {
            Main.logger.warn("获取chimuAPI失败,原因:{}", jobj.getStr("error_message"));
            return null;
        }
        //TODO should add to DB
        return jobj.toBean(ChimuMap.class);
    }

    public String getOsuFilePath(String BeatMapId) {
        //TODO need store cache
        // if cache not exit
        ChimuMap chimuMap = getMapInfo(BeatMapId);
        if (chimuMap == null) return null;
        StringBuffer filePath = new StringBuffer(String.valueOf(chimuMap.getParentSetId()));
        String osuFileName = chimuMap.getOsuFile().replace("/","");
        filePath.append(File.separator)
                .append(osuFileName);
        File osuFile = new File(Main.BeatMapsPath, filePath.toString());
        if(osuFile.exists()){
            return osuFile.getAbsolutePath();
        }else if (downloadMap(String.valueOf(chimuMap.getParentSetId()))) {
            if(osuFile.exists()) return osuFile.getAbsolutePath();
        }
        return null;
    }

    //TODO API REQUEST NEED TASK QUE
    public boolean downloadMap(String MapSetId) {
        File osuFile = new File(Main.BeatMapsPath, MapSetId + ".osz");
        try (OutputStream os = Files.newOutputStream(osuFile.toPath())) {
            long size = HttpUtil.download(DOWNLOAD_URL + MapSetId, os, true);
            Main.logger.info("下载地图{}成功,大小:{}", MapSetId, size);
            File mapFolder = ZipUtil.unzip(osuFile);
            if (mapFolder.exists() && mapFolder.isDirectory()) {
                for (File file : mapFolder.listFiles()) {
                    if (file.isDirectory()) {
                        FileUtil.del(file);
                        continue;
                    }
                    String fileName = file.getName();
                    if (fileName.contains(".mp3")
                            || fileName.contains(".wav")
                            || fileName.contains(".ogg")
                            || fileName.contains(".osb")
                    ) {
                        file.delete();
                    }
                }
            }
        } catch (IOException ignore) {
            return false;
        }
        osuFile.delete();
        return true;
    }

}

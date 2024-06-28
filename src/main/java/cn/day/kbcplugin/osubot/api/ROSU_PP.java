package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.library.rosu_pp_native;
import cn.day.kbcplugin.osubot.pojo.bancho.OsuMap;
import cn.day.kbcplugin.osubot.pojo.osu.PPResult;
import com.sun.jna.Native;

import java.io.File;

public class ROSU_PP {
    public static boolean READY_TO_CALL = false;

    public static File dll_file;
    public static rosu_pp_native INSTANCE;

    public static void init() {
        dll_file = new File(Main.rootPath, "rosu_native.dll");
        if (dll_file.exists()) {
            INSTANCE = Native.load(dll_file.getAbsolutePath(), rosu_pp_native.class);
            READY_TO_CALL = true;
        } else {
            Main.logger.error("无法加载rosu-pp,离线计算功能关闭");
            READY_TO_CALL = false;
        }
    }

    public static PPResult rosu_calc_pp(OsuMap map) {
        if (READY_TO_CALL) {
            return INSTANCE.cal_pp(map);
        }
        Main.logger.warn("离线计算暂时不可用");
        return null;
    }

}

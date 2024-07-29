package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.library.RosuPPNative;
import cn.day.kbcplugin.osubot.model.api.OsuMap;
import cn.day.kbcplugin.osubot.model.api.PPResult;
import com.sun.jna.Native;

import java.io.File;

public class RustOsuPPCalculator {
    public static boolean READY_TO_CALL = false;

    public static File dll_file;
    public static RosuPPNative INSTANCE;

    public static void init() {
        dll_file = new File(Main.rootPath, "rosu_native.dll");
        if (dll_file.exists()) {
            INSTANCE = Native.load(dll_file.getAbsolutePath(), RosuPPNative.class);
            READY_TO_CALL = true;
        } else {
            Main.logger.error("无法加载rosu-pp,离线计算功能关闭");
            READY_TO_CALL = false;
        }
    }

    public static PPResult CalPP(OsuMap map) {
        if (READY_TO_CALL) {
            PPResult ppResult = INSTANCE.cal_pp(map);
            Main.logger.info("pp结果:{}", ppResult.toString());
            return ppResult;
        }
        Main.logger.warn("离线计算暂时不可用");
        return null;
    }

}

package cn.day.kbcplugin.osubot.api.library;

import cn.day.kbcplugin.osubot.model.api.OsuMap;
import cn.day.kbcplugin.osubot.model.api.PPResult;
import com.sun.jna.win32.StdCallLibrary;

public interface RosuPPNative extends StdCallLibrary {
    PPResult cal_pp(OsuMap map);
}
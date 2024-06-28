package cn.day.kbcplugin.osubot.api.library;

import cn.day.kbcplugin.osubot.pojo.bancho.OsuMap;
import cn.day.kbcplugin.osubot.pojo.osu.PPResult;
import com.sun.jna.win32.StdCallLibrary;

public interface rosu_pp_native extends StdCallLibrary {
    PPResult cal_pp(OsuMap map);

}

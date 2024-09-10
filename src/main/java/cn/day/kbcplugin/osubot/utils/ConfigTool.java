package cn.day.kbcplugin.osubot.utils;

import cn.day.kbcplugin.osubot.Main;
import snw.jkook.config.file.FileConfiguration;
import snw.jkook.config.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConfigTool {
    /**
     * 更新配置文件方法
     */
    public static void updateConfig() {
        // 旧配置文件
        File configFile = new File(Main.rootPath, "config.yml");
        FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);

        // 新配置文件
        InputStream resource = Main.class.getResourceAsStream("/config.yml");
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resource, StandardCharsets.UTF_8));

        // 检查并更新配置项
        for (String path : oldConfig.getKeys(true)) {
            if (newConfig.contains(path)) {
                newConfig.set(path, oldConfig.get(path));
            }
        }
        // 保存更新后的配置文件
        try {
            // 保存FileConfiguration到文件
            newConfig.save(new File(Main.rootPath, "config.yml"));
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}

package cn.day.kbcplugin.osubot;

import cn.day.kbcplugin.osubot.api.*;
import cn.day.kbcplugin.osubot.commands.CommandInit;
import cn.day.kbcplugin.osubot.dao.AccountDao;
import cn.day.kbcplugin.osubot.dao.BeatMapDao;
import cn.day.kbcplugin.osubot.dao.UserDao;
import cn.day.kbcplugin.osubot.utils.ConfigTool;
import cn.day.kbcplugin.osubot.utils.ImgUtil;
import cn.day.kbcplugin.osubot.utils.ScoreUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;
import cn.hutool.setting.Setting;
import org.slf4j.Logger;
import snw.jkook.config.file.FileConfiguration;
import snw.jkook.plugin.BasePlugin;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;


public class Main extends BasePlugin {

    public static Logger logger;
    public static DataSource ds;
    public static FileConfiguration config;
    public static Main instance;
    public static File rootPath;//插件的数据根目录
    public static File imgsPath;//用来放资源图片文件夹的目录
    public static File BeatMapsPath;
    public static BanchoAPI banchoApi;
    public static ScoreUtil scoreUtil;
    public static UserDao userDao;
    public static ImgUtil imgUtil;
    public static SayobotApi sayobotApi;
    public static ppySbApi ppySbApi;
    public static BeatMapDao beatMapDao;
    public static AccountDao accountDao;

    @Override
    public void onLoad() {
        LogFactory.setCurrentLogFactory(Slf4jLogFactory.class);
        instance = this;
        logger = getLogger();
        rootPath = getDataFolder();
        File configFile = new File(rootPath, "config.yml");
        if (!configFile.exists()) {
            logger.info("正在重新生成一份默认配置文件");
            saveDefaultConfig();
        }
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        File dbFile = new File(rootPath, "osubot.db");
        createFolder();
        boolean isNewDb = false;
        if (!dbFile.exists()) {
            logger.info("未找到数据库文件，开始创建数据库文件");
            saveResource("osubot.db", true, false);
            isNewDb = true;
        }
        if (imgsPath.exists() && imgsPath.isDirectory() && imgsPath.listFiles().length == 0) {
            saveResources();
        }

        logger.info("开始加载数据库");
        File dbSettingFile = new File(rootPath, "db.setting");
        if (!dbSettingFile.exists()) {
            saveResource("db.setting", false, false);
            logger.info("载入默认的数据库配置文件");
        }
        Setting dbSetting = new Setting(dbSettingFile, StandardCharsets.UTF_8, false);
        if (isNewDb) {
            dbSetting.set("url", "jdbc:sqlite:" + dbFile.getAbsolutePath());
            dbSetting.store();
        }
        ds = DSFactory.create(dbSetting).getDataSource();
        ConfigTool.updateConfig();
        logger.info("数据库加载完成");
    }

    @Override
    public void onEnable() {
        config = this.getConfig();
        logger.info("正在注册指令系统");
        CommandInit.registerCommand(this);
        logger.info("加载Rosu-pp-native库");
        ROSU_PP.init();
        loadInstance();
    }

    @Override
    public void onDisable() {
        saveConfig(); /* 4 */
        logger.info("插件卸载完成");
    }

    private void saveResources() {
        logger.info("正在释放资源");
        //加载字体
        saveResource("Gayatri.ttf", true, false);
        //加载Rosu_native
        saveResource("rosu_native.dll", true, false);
        //加载必要的素材文件
        for (String fileName : fileNameList) {
            saveResource("images" + File.separator + fileName, false, false);
        }
    }

    private void createFolder() {
        //创建素材文件夹
        imgsPath = new File(rootPath, "images");
        if (!imgsPath.exists()) {
            boolean success = imgsPath.mkdirs();
            if (!success) {
                throw new RuntimeException("create images folder fail");
            }
        }
        //创建地图文件夹
        BeatMapsPath = new File(rootPath, "maps");
        if (!BeatMapsPath.exists()) {
            boolean success = BeatMapsPath.mkdirs();
            if (!success) {
                throw new RuntimeException("create maps folder fail");
            }
        }
    }

    private void loadInstance() {
        banchoApi = new BanchoAPI();
        scoreUtil = new ScoreUtil(banchoApi);
        imgUtil = new ImgUtil(banchoApi, scoreUtil);
        userDao = new UserDao(ds);
        sayobotApi = new SayobotApi();
        ppySbApi = new ppySbApi();
        beatMapDao = new BeatMapDao(ds);
        accountDao = new AccountDao(ds);
    }

    //需要图片的列表
    //wdm，真就遍历？
    private static final String[] fileNameList = new String[]{"bpBanner.png", "creep.png", "hit0.png", "hit100.png", "hit300.png", "hit50.png", "layout.png", "mode-0.png", "mode-1.png", "mode-2.png", "mode-3.png", "ppBanner.png", "ranking-A.png", "ranking-B.png", "ranking-C.png", "ranking-D.png", "ranking-F.png", "ranking-perfect.png", "ranking-S.png", "ranking-SH.png", "ranking-X.png", "ranking-XH.png", "role-creep.png", "score-0.png", "score-1.png", "score-2.png", "score-3.png", "score-4.png", "score-5.png", "score-6.png", "score-7.png", "score-8.png", "score-9.png", "score-comma.png", "score-dot.png", "score-percent.png", "score-x.png", "scorerank.png", "selection-mod-autoplay.png", "selection-mod-cinema.png", "selection-mod-doubletime.png", "selection-mod-easy.png", "selection-mod-fadein.png", "selection-mod-flashlight.png", "selection-mod-halftime.png", "selection-mod-hardrock.png", "selection-mod-hidden.png", "selection-mod-key1.png", "selection-mod-key2.png", "selection-mod-key3.png", "selection-mod-key4.png", "selection-mod-key5.png", "selection-mod-key6.png", "selection-mod-key7.png", "selection-mod-key8.png", "selection-mod-key9.png", "selection-mod-keycoop.png", "selection-mod-nightcore.png", "selection-mod-nofail.png", "selection-mod-perfect.png", "selection-mod-random.png", "selection-mod-relax.png", "selection-mod-relax2.png", "selection-mod-spunout.png", "selection-mod-suddendeath.png", "selection-mod-target.png"};
}

package cn.day.kbcplugin.osubot;

import cn.day.kbcplugin.osubot.api.*;
import cn.day.kbcplugin.osubot.commands.*;
import cn.day.kbcplugin.osubot.dao.AccountMapper;
import cn.day.kbcplugin.osubot.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.utils.ConfigTool;
import cn.day.kbcplugin.osubot.utils.ImgUtil;
import com.mybatisflex.core.MybatisFlexBootstrap;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dromara.hutool.setting.Setting;
import org.slf4j.Logger;
import snw.jkook.config.file.FileConfiguration;
import snw.jkook.plugin.BasePlugin;
import snw.kookbc.impl.command.litecommands.LiteKookFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;

public class Main extends BasePlugin {

    public static Logger logger;
    public static FileConfiguration config;
    public static Main instance;
    public static File rootPath;//插件的数据根目录
    public static File imgsPath;//用来放资源图片文件夹的目录
    public static File BeatMapsPath;
    public static DataSource ds = null;
    public static MybatisFlexBootstrap dbContext;

    @Override
    public void onLoad() {
        instance = this;
        logger = getLogger();
        rootPath = getDataFolder();
        File configFile = new File(rootPath, "config.yml");
        if (!configFile.exists()) {
            logger.info("正在重新生成一份默认配置文件");
            saveDefaultConfig();
        }
        try {
            createFolder();//创建文件夹
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean success = initDb();
        if (!success) {
            throw new RuntimeException("数据初始化失败!");
        }
    }

    @Override
    public void onEnable() {
        config = this.getConfig();
        logger.info("正在注册指令系统");
        AccountMapper accountMapper = dbContext.getMapper(AccountMapper.class);
        UserInfoMapper userInfoMapper = dbContext.getMapper(UserInfoMapper.class);
        LiteKookFactory.builder(this).commands(
                new BindAccount(accountMapper, userInfoMapper),
                new UnBind(accountMapper, userInfoMapper),
                new SetMode(accountMapper),
                new SetServer(accountMapper),
                new Recent(accountMapper, userInfoMapper),
                new APIInfo(), new Best(accountMapper, userInfoMapper),
                new Profile(userInfoMapper),
                new SearchMap()
        ).build();
        logger.info("初始化API");
        APIHandler.INSTANCE.init(new ChimuAPI(), new LegacyBanchoAPI(APIKey.KEY), new SBApi());
        logger.info("加载Rosu-pp库");
        RustOsuPPCalculator.init();
        ImgUtil.Init();
    }

    @Override
    public void onDisable() {
        saveConfig();
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

    private void createFolder() throws IOException {
        //创建素材文件夹
        imgsPath = new File(rootPath, "images");
        if (!imgsPath.exists()) {
            boolean success = imgsPath.mkdirs();
            if (!success) {
                throw new IOException("create images folder fail");
            }
        }
        //创建地图文件夹
        BeatMapsPath = new File(rootPath, "maps");
        if (!BeatMapsPath.exists()) {
            boolean success = BeatMapsPath.mkdirs();
            if (!success) {
                throw new IOException("create maps folder fail");
            }
        }
    }

    private boolean initDb() {
        logger.info("正在初始化数据库");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.error("无法加载sqlite驱动:{}", e.getLocalizedMessage(), e);
            return false;
        }
        File dbFile = new File(rootPath, "osubot.db");
        boolean isNewDb = false;
        if (!dbFile.exists()) {
            logger.info("未找到数据库文件，开始创建数据库文件");
            saveResource("osubot.db", true, false);
            isNewDb = true;
        }
        if (imgsPath.exists() && imgsPath.isDirectory() && imgsPath.listFiles().length == 0) {
            saveResources();
        }
        File dbSettingFile = new File(rootPath, "db.setting");
        if (!dbSettingFile.exists()) {
            saveResource("db.setting", false, false);
            logger.info("载入默认的数据库配置文件");
        }
        Setting dbSetting = new Setting(dbSettingFile, StandardCharsets.UTF_8, false);
        if (isNewDb) {
            String DbPath = dbFile.getAbsolutePath().replaceAll("\\\\", "/");
            dbSetting.set("url", "jdbc:sqlite:" + DbPath);
            dbSetting.store();
        }
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(dbSetting.getStr("driver"));
        bds.setUrl(dbSetting.getStr("url").replaceAll("/", Matcher.quoteReplacement(File.separator)));
        ds = bds;
        try {
            Main.dbContext = MybatisFlexBootstrap.getInstance().setDataSource(Main.ds).addMapper(AccountMapper.class).addMapper(UserInfoMapper.class).start();
        } catch (Exception e) {
            logger.error("数据库初始化失败:{}", e.getLocalizedMessage(), e);
            return false;
        }
        ConfigTool.updateConfig();
        List<Row> accounts = Db.selectAll("accounts");
        logger.info("数据库加载完成");
        return true;
    }

    //需要图片的列表
    //wdm，真就遍历？
    private static final String[] fileNameList = new String[]{"bpBanner.png", "creep.png", "hit0.png", "hit100.png", "hit300.png", "hit50.png", "layout.png", "mode-0.png", "mode-1.png", "mode-2.png", "mode-3.png", "ppBanner.png", "ranking-A.png", "ranking-B.png", "ranking-C.png", "ranking-D.png", "ranking-F.png", "ranking-perfect.png", "ranking-S.png", "ranking-SH.png", "ranking-X.png", "ranking-XH.png", "role-creep.png", "score-0.png", "score-1.png", "score-2.png", "score-3.png", "score-4.png", "score-5.png", "score-6.png", "score-7.png", "score-8.png", "score-9.png", "score-comma.png", "score-dot.png", "score-percent.png", "score-x.png", "scorerank.png", "selection-mod-autoplay.png", "selection-mod-cinema.png", "selection-mod-doubletime.png", "selection-mod-easy.png", "selection-mod-fadein.png", "selection-mod-flashlight.png", "selection-mod-halftime.png", "selection-mod-hardrock.png", "selection-mod-hidden.png", "selection-mod-key1.png", "selection-mod-key2.png", "selection-mod-key3.png", "selection-mod-key4.png", "selection-mod-key5.png", "selection-mod-key6.png", "selection-mod-key7.png", "selection-mod-key8.png", "selection-mod-key9.png", "selection-mod-keycoop.png", "selection-mod-nightcore.png", "selection-mod-nofail.png", "selection-mod-perfect.png", "selection-mod-random.png", "selection-mod-relax.png", "selection-mod-relax2.png", "selection-mod-spunout.png", "selection-mod-suddendeath.png", "selection-mod-target.png"};
}

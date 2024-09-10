package cn.day.kbcplugin.osubot;

import cn.day.kbcplugin.osubot.api.*;
import cn.day.kbcplugin.osubot.commands.*;
import cn.day.kbcplugin.osubot.commands.base.*;
import cn.day.kbcplugin.osubot.db.dao.AccountMapper;
import cn.day.kbcplugin.osubot.db.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.db.service.UserInfoServiceImpl;
import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.entity.Account;
import cn.day.kbcplugin.osubot.utils.ConfigTool;
import cn.day.kbcplugin.osubot.utils.ImgUtil;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.MybatisFlexBootstrap;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.setting.Setting;
import org.slf4j.Logger;
import snw.jkook.config.file.FileConfiguration;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.plugin.BasePlugin;
import snw.kookbc.impl.command.litecommands.LiteKookFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
        LiteKookFactory.builder(this)
                .bind(AccountMapper.class, () -> dbContext.getMapper(AccountMapper.class))
                .bind(UserInfoMapper.class, () -> dbContext.getMapper(UserInfoMapper.class))
                .bind(UserInfoServiceImpl.class, () -> new UserInfoServiceImpl(dbContext.getMapper(UserInfoMapper.class)))
                .context(Account.class, new AccountContextProvider(dbContext.getMapper(AccountMapper.class)))
                .commands(
                        BindAccount.class,
                        UnBind.class,
                        Setter.class,
                        Recent.class,
                        APIInfo.class,
                        Best.class,
                        Profile.class,
                        SearchMap.class
                )
                .argument(ServerEnum.class, new ServerArgumentResolver())
                .argument(OsuModeEnum.class, new ModeArgumentResolver())
                .argumentParser(Integer.class, new IntegerParser())
                .result(MultipleCardComponent.class, new CardMessageResultHandler())
                .selfProcessor((builder, internal) -> builder.schematicGenerator(new DescSchematicGenerator(
                        internal.getValidatorService(),
                        internal.getWrapperRegistry())
                ))
                .invalidUsage(new BasicInvalidUsageHandler())
                .build();
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
        try {
            String path = ResourceUtil.getResource("config.yml").getUrl().getFile();
            path = path.substring(0, path.lastIndexOf('!'));
            try (JarFile jarFile = new JarFile(new URI(path).getPath())) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("images/") && !entry.isDirectory()) {
                        saveResource(name, false, false);
                    }
                }
            }
        } catch (URISyntaxException | IOException ignore) {
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
            FlexGlobalConfig globalConfig = FlexGlobalConfig.getDefaultConfig();
            globalConfig.setPrintBanner(false);
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
}

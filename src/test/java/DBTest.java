import cn.day.kbcplugin.osubot.api.BanchoAPI;
import cn.day.kbcplugin.osubot.api.SayobotApi;
import cn.day.kbcplugin.osubot.api.ppySbApi;
import cn.day.kbcplugin.osubot.dao.BeatMapDao;
import cn.day.kbcplugin.osubot.dao.UserDao;
import cn.day.kbcplugin.osubot.enums.BeatmapStatus;
import cn.day.kbcplugin.osubot.pojo.SayoDBMap;
import cn.day.kbcplugin.osubot.pojo.api.SayoMapInfo_old;
import cn.day.kbcplugin.osubot.pojo.api.SayoMapSetInfo_old;
import cn.day.kbcplugin.osubot.pojo.bancho.BanchoUser;
import cn.day.kbcplugin.osubot.pojo.bancho.Event;
import cn.day.kbcplugin.osubot.pojo.bancho.Userinfo;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.utils.ImgUtil;
import cn.day.kbcplugin.osubot.utils.ScoreUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import snw.jkook.config.file.YamlConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static cn.day.kbcplugin.osubot.Main.*;

public class DBTest {
    static {
        File dbSettingFile = new File("C:\\Users\\Time\\Desktop\\project\\debugkbc\\plugins\\OsuBot\\db.setting");
        Setting dbSetting = new Setting(dbSettingFile, StandardCharsets.UTF_8, false);
        ds = DSFactory.create(dbSetting).getDataSource();
        logger = LoggerFactory.getLogger("Testing");
        config = new YamlConfiguration();
        config.set("OSU_APIKEY","95ebd838492ccbfd97a5d1c0279e88fe603f2d45");
        banchoApi = new BanchoAPI();
        scoreUtil = new ScoreUtil(banchoApi);
        imgUtil = new ImgUtil(banchoApi, scoreUtil);
        userDao = new UserDao(ds);
        sayobotApi = new SayobotApi();
        ppySbApi = new ppySbApi();
        rootPath = new File("C:\\Users\\Time\\Desktop\\project\\debugkbc\\plugins\\OsuBot");
        imgsPath = new File(rootPath, "images");
        BeatMapsPath = new File(rootPath, "maps");
        beatMapDao = new BeatMapDao(ds);
    }

    @Test
    void SbAPI() {
        logger.info("BESTSCORES:");
        List<? extends AbstractScore> bestScores = ppySbApi.getBestScores(1096, 100, 0);
        if (bestScores != null) {
            int count = 0;
            for(AbstractScore score:bestScores){
                logger.info("{}=",count++);
                if(score.beatmapId()!=0){
                    logger.info(JSONUtil.parseObj(score).toJSONString(2));
                }
            }
        }
    }

    @Test
    void insertMap(){
        String BASE_URL = "https://api.sayobot.cn";
//        Map<String, Object> paramMap = MapUtil.builder(new HashMap<String, Object>())
//                .put("K", 809914)
//                .put("T", 1).build();
//        JSONObject response = JSONUtil.parseObj(HttpUtil.get(BASE_URL + "/v2/beatmapinfo", paramMap));
        JSONObject response = JSONUtil.parseObj(apiMapinfo);
        SayoMapSetInfo_old beatmapSet = response.getJSONObject("data").toBean(SayoMapSetInfo_old.class);
        logger.info("APIBeatmaps:");
        logger.info(response.getJSONObject("data").toJSONString(2));
        int count = beatmapSet.getBids_amount();
        List<SayoDBMap> list = new ArrayList<>(count);
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        for (int i = 0; i < count; i++) {
            SayoDBMap beatmap = new SayoDBMap();
            BeanUtil.copyProperties(beatmapSet,beatmap,copyOptions);
            SayoMapInfo_old info = beatmapSet.bid_data.get(i);
            BeanUtil.copyProperties(info,beatmap,copyOptions);
            beatmap.bpm = beatmapSet.getBpm();
            beatmap.sid = beatmapSet.getSid();
            list.add(beatmap);
            if(BeatmapStatus.isOnline(beatmapSet.getApproved())){
                beatMapDao.insert(beatmap);
            }
        }
        logger.info("Beatmaps:");
        for(SayoDBMap map:list){
            logger.info(JSONUtil.parseObj(map).toJSONString(2));
        }
    }

    @Test
    void selectMap(){
        logger.info("Select map:999944");
        SayoDBMap map = beatMapDao.selectById(999944);
        logger.info(JSONUtil.parseObj(map).toJSONString(2));
    }

    @Test
    void banchoGetUser(){
//        BanchoUser user = banchoApi.getUser(0, "10458474");
//        logger.info(JSONUtil.parseObj(user).toJSONString(2));
        JSONObject user = JSONUtil.parseObj(banchoUserInfo);
        JSONObject event = (JSONObject) user.getJSONArray("events").get(0);
        Event bean = event.toBean(Event.class);
    }


    static final String banchoUserInfo = "{\"user_id\":\"10458474\",\"username\":\"TuRou\",\"join_date\":\"2017-07-05 11:34:53\",\"count300\":\"18743426\",\"count100\":\"1633564\",\"count50\":\"146347\",\"playcount\":\"64745\",\"ranked_score\":\"43462955704\",\"total_score\":\"204196107133\",\"pp_rank\":\"8408\",\"level\":\"101.773\",\"pp_raw\":\"8500.96\",\"accuracy\":\"98.30065155029297\",\"count_rank_ss\":\"37\",\"count_rank_ssh\":\"8\",\"count_rank_s\":\"982\",\"count_rank_sh\":\"125\",\"count_rank_a\":\"1680\",\"country\":\"CN\",\"total_seconds_played\":\"4582317\",\"pp_country_rank\":\"151\",\"events\":[{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #186 on <a href='\\/b\\/3795538?m=0'>AAAA - Houseki no Furu Yoru ni [Neko]<\\/a> (osu!)\",\"beatmap_id\":\"3795538\",\"beatmapset_id\":\"1847694\",\"date\":\"2023-11-19 15:01:38\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #267 on <a href='\\/b\\/4025006?m=0'>u's - Bokura no LIVE Kimi to no LIFE [AdventureS]<\\/a> (osu!)\",\"beatmap_id\":\"4025006\",\"beatmapset_id\":\"1945496\",\"date\":\"2023-11-19 14:54:08\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/B_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #617 on <a href='\\/b\\/4197953?m=0'>yuikonnu &amp; ayaponzu* - Super Nuko World [Nya!]<\\/a> (osu!)\",\"beatmap_id\":\"4197953\",\"beatmapset_id\":\"2016414\",\"date\":\"2023-11-19 14:18:53\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #81 on <a href='\\/b\\/4027752?m=0'>nao feat. isui - Ukigumo [Sunshine After Rain]<\\/a> (osu!)\",\"beatmap_id\":\"4027752\",\"beatmapset_id\":\"1946710\",\"date\":\"2023-11-19 14:02:49\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #135 on <a href='\\/b\\/4027752?m=0'>nao feat. isui - Ukigumo [Sunshine After Rain]<\\/a> (osu!)\",\"beatmap_id\":\"4027752\",\"beatmapset_id\":\"1946710\",\"date\":\"2023-11-19 13:58:12\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #369 on <a href='\\/b\\/4027752?m=0'>nao feat. isui - Ukigumo [Sunshine After Rain]<\\/a> (osu!)\",\"beatmap_id\":\"4027752\",\"beatmapset_id\":\"1946710\",\"date\":\"2023-11-19 13:53:53\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/S_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved <b>rank #29<\\/b> on <a href='\\/b\\/3930006?m=0'>Yuki Setsuna (CV: Kusunoki Tomori) - LIKE IT! LOVE IT! [Fire]<\\/a> (osu!)\",\"beatmap_id\":\"3930006\",\"beatmapset_id\":\"1905903\",\"date\":\"2023-11-19 13:43:28\",\"epicfactor\":\"2\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #51 on <a href='\\/b\\/4264203?m=0'>Aqours - Deep Resonance [Melancholy]<\\/a> (osu!)\",\"beatmap_id\":\"4264203\",\"beatmapset_id\":\"2043278\",\"date\":\"2023-11-19 13:38:10\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #430 on <a href='\\/b\\/4322683?m=0'>Shigure Ui (9-sai) - Shukusei!! Loli-Kami Requiem* [Disgusting...]<\\/a> (osu!)\",\"beatmap_id\":\"4322683\",\"beatmapset_id\":\"2066239\",\"date\":\"2023-11-19 13:32:13\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #246 on <a href='\\/b\\/3112102?m=0'>nao - Toaru Shoukoku no Ohimesama ga... [Camo's Extreme]<\\/a> (osu!)\",\"beatmap_id\":\"3112102\",\"beatmapset_id\":\"1450155\",\"date\":\"2023-11-19 13:16:28\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/A_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #171 on <a href='\\/b\\/4213424?m=0'>Nakiri Ayame - Kawayo [Docchi]<\\/a> (osu!)\",\"beatmap_id\":\"4213424\",\"beatmapset_id\":\"2022917\",\"date\":\"2023-11-19 13:11:38\",\"epicfactor\":\"1\"},{\"display_html\":\"<img src='\\/images\\/S_small.png'\\/> <b><a href='\\/u\\/10458474'>TuRou<\\/a><\\/b> achieved rank #140 on <a href='\\/b\\/3845061?m=0'>YOASOBI - Shukufuku (TV Size) [Loli's Rebellious Extra]<\\/a> (osu!)\",\"beatmap_id\":\"3845061\",\"beatmapset_id\":\"1864974\",\"date\":\"2023-11-19 12:42:15\",\"epicfactor\":\"1\"}]}";

    static final String apiMapinfo = "{\n" +
            "    \"data\":{\n" +
            "        \"approved\": 1,\n" +
            "        \"approved_date\": 1475434847,\n" +
            "        \"artist\": \"yuikonnu\",\n" +
            "        \"artistU\": \"ゆいこんぬ\",\n" +
            "        \"bid_data\": [\n" +
            "          {\n" +
            "            \"AR\": 9.2,\n" +
            "            \"CS\": 4.2,\n" +
            "            \"HP\": 7,\n" +
            "            \"OD\": 9,\n" +
            "            \"aim\": 3.185,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 999944,\n" +
            "            \"circles\": 497,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 1027,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 189258,\n" +
            "            \"playcount\": 2320275,\n" +
            "            \"pp\": 324.5,\n" +
            "            \"pp_acc\": 100.3,\n" +
            "            \"pp_aim\": 157,\n" +
            "            \"pp_speed\": 157,\n" +
            "            \"sliders\": 246,\n" +
            "            \"speed\": 2.431,\n" +
            "            \"spinners\": 0,\n" +
            "            \"star\": 5.988,\n" +
            "            \"strain_aim\": \"013112000001124113223201111124222222330011333441330\",\n" +
            "            \"strain_speed\": \"003203210112112214214412302113233233240321233330230\",\n" +
            "            \"version\": \"Lost\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 5,\n" +
            "            \"CS\": 3.2,\n" +
            "            \"HP\": 4,\n" +
            "            \"OD\": 4.5,\n" +
            "            \"aim\": 1.165,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1001282,\n" +
            "            \"circles\": 82,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 552,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 28060,\n" +
            "            \"playcount\": 87773,\n" +
            "            \"pp\": 18.93,\n" +
            "            \"pp_acc\": 8.84,\n" +
            "            \"pp_aim\": 6.69,\n" +
            "            \"pp_speed\": 6.69,\n" +
            "            \"sliders\": 189,\n" +
            "            \"speed\": 0.8836,\n" +
            "            \"spinners\": 1,\n" +
            "            \"star\": 2.188,\n" +
            "            \"strain_aim\": \"330344332323334433442214442343332343023421333000000\",\n" +
            "            \"strain_speed\": \"343333341443234434443133444143443424024432423000000\",\n" +
            "            \"version\": \"Normal\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 3.5,\n" +
            "            \"CS\": 2.7,\n" +
            "            \"HP\": 3,\n" +
            "            \"OD\": 3,\n" +
            "            \"aim\": 0.8511,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1001410,\n" +
            "            \"circles\": 44,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 186,\n" +
            "            \"maxcombo\": 478,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 20949,\n" +
            "            \"playcount\": 55508,\n" +
            "            \"pp\": 8.1,\n" +
            "            \"pp_acc\": 3.91,\n" +
            "            \"pp_aim\": 2.7,\n" +
            "            \"pp_speed\": 2.7,\n" +
            "            \"sliders\": 137,\n" +
            "            \"speed\": 0.682,\n" +
            "            \"spinners\": 1,\n" +
            "            \"star\": 1.625,\n" +
            "            \"strain_aim\": \"223434423323134414204101344444314230014034300200000\",\n" +
            "            \"strain_speed\": \"322444441443334413414202411324224320114234110300000\",\n" +
            "            \"version\": \"Easy\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 7.3,\n" +
            "            \"CS\": 3.6,\n" +
            "            \"HP\": 5,\n" +
            "            \"OD\": 6.5,\n" +
            "            \"aim\": 1.62,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1001697,\n" +
            "            \"circles\": 197,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 789,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 58014,\n" +
            "            \"playcount\": 304870,\n" +
            "            \"pp\": 65.25,\n" +
            "            \"pp_acc\": 26.62,\n" +
            "            \"pp_aim\": 21.86,\n" +
            "            \"pp_speed\": 21.86,\n" +
            "            \"sliders\": 269,\n" +
            "            \"speed\": 1.519,\n" +
            "            \"spinners\": 1,\n" +
            "            \"star\": 3.283,\n" +
            "            \"strain_aim\": \"013232221212122233224124323322332324203323231220000\",\n" +
            "            \"strain_speed\": \"001110101113234234312112014134430242312212140100000\",\n" +
            "            \"version\": \"Hard\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 8.3,\n" +
            "            \"CS\": 4,\n" +
            "            \"HP\": 7,\n" +
            "            \"OD\": 7,\n" +
            "            \"aim\": 2.173,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1015434,\n" +
            "            \"circles\": 229,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 840,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 69000,\n" +
            "            \"playcount\": 907902,\n" +
            "            \"pp\": 110.2,\n" +
            "            \"pp_acc\": 34.35,\n" +
            "            \"pp_aim\": 39.81,\n" +
            "            \"pp_speed\": 39.81,\n" +
            "            \"sliders\": 294,\n" +
            "            \"speed\": 2.151,\n" +
            "            \"spinners\": 2,\n" +
            "            \"star\": 4.518,\n" +
            "            \"strain_aim\": \"013122221234123134322321222212324421102244122123000\",\n" +
            "            \"strain_speed\": \"003024111114221113422431041024223323002122102124000\",\n" +
            "            \"version\": \"Mochi's Insane\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 9,\n" +
            "            \"CS\": 3.8,\n" +
            "            \"HP\": 7,\n" +
            "            \"OD\": 8.4,\n" +
            "            \"aim\": 2.588,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1023492,\n" +
            "            \"circles\": 373,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 997,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 156614,\n" +
            "            \"playcount\": 1000305,\n" +
            "            \"pp\": 203.5,\n" +
            "            \"pp_acc\": 71.56,\n" +
            "            \"pp_aim\": 79.14,\n" +
            "            \"pp_speed\": 79.14,\n" +
            "            \"sliders\": 276,\n" +
            "            \"speed\": 2.329,\n" +
            "            \"spinners\": 0,\n" +
            "            \"star\": 5.153,\n" +
            "            \"strain_aim\": \"002011110001114112123100212022212222240111222340230\",\n" +
            "            \"strain_speed\": \"002303320002223014114402013034331234341130243330240\",\n" +
            "            \"version\": \"Expert\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 6.5,\n" +
            "            \"CS\": 3.4,\n" +
            "            \"HP\": 4.5,\n" +
            "            \"OD\": 5.5,\n" +
            "            \"aim\": 1.374,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1038425,\n" +
            "            \"circles\": 137,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 649,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 34820,\n" +
            "            \"playcount\": 128531,\n" +
            "            \"pp\": 31.59,\n" +
            "            \"pp_acc\": 15.69,\n" +
            "            \"pp_aim\": 10.51,\n" +
            "            \"pp_speed\": 10.51,\n" +
            "            \"sliders\": 232,\n" +
            "            \"speed\": 1.06,\n" +
            "            \"spinners\": 1,\n" +
            "            \"star\": 2.592,\n" +
            "            \"strain_aim\": \"133344233333433323344323322323434433103334442300000\",\n" +
            "            \"strain_speed\": \"224223324444444211322324323122224333102313340400000\",\n" +
            "            \"version\": \"Advanced\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 9.1,\n" +
            "            \"CS\": 4,\n" +
            "            \"HP\": 7,\n" +
            "            \"OD\": 8.7,\n" +
            "            \"aim\": 2.839,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1054525,\n" +
            "            \"circles\": 469,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 1023,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 134647,\n" +
            "            \"playcount\": 1278291,\n" +
            "            \"pp\": 253.5,\n" +
            "            \"pp_acc\": 86.94,\n" +
            "            \"pp_aim\": 108.9,\n" +
            "            \"pp_speed\": 108.9,\n" +
            "            \"sliders\": 253,\n" +
            "            \"speed\": 2.389,\n" +
            "            \"spinners\": 0,\n" +
            "            \"star\": 5.509,\n" +
            "            \"strain_aim\": \"013112001112134133222012122144212222311122224312000\",\n" +
            "            \"strain_speed\": \"004204100212223224124223013033332332401212433304000\",\n" +
            "            \"version\": \"Extra\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"AR\": 9,\n" +
            "            \"CS\": 4,\n" +
            "            \"HP\": 7,\n" +
            "            \"OD\": 7.5,\n" +
            "            \"aim\": 2.598,\n" +
            "            \"audio\": \"audio.mp3\",\n" +
            "            \"bg\": \"45345.png\",\n" +
            "            \"bid\": 1056320,\n" +
            "            \"circles\": 449,\n" +
            "            \"hit300window\": 0,\n" +
            "            \"img\": \"0\",\n" +
            "            \"length\": 187,\n" +
            "            \"maxcombo\": 967,\n" +
            "            \"mode\": 0,\n" +
            "            \"passcount\": 135799,\n" +
            "            \"playcount\": 1156809,\n" +
            "            \"pp\": 178.2,\n" +
            "            \"pp_acc\": 51.85,\n" +
            "            \"pp_aim\": 72.29,\n" +
            "            \"pp_speed\": 72.29,\n" +
            "            \"sliders\": 253,\n" +
            "            \"speed\": 2.409,\n" +
            "            \"spinners\": 2,\n" +
            "            \"star\": 5.238,\n" +
            "            \"strain_aim\": \"114322243122304222221321102202323232220022123233244\",\n" +
            "            \"strain_speed\": \"003313421221311022321323310302433233240033114233144\",\n" +
            "            \"version\": \"Frey's Insane\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"bids_amount\": 9,\n" +
            "        \"bpm\": 176,\n" +
            "        \"creator\": \"Lasse\",\n" +
            "        \"creator_id\": 896613,\n" +
            "        \"favourite_count\": 1416,\n" +
            "        \"genre\": 7,\n" +
            "        \"language\": 3,\n" +
            "        \"last_update\": 1474714918,\n" +
            "        \"local_update\": 1699760547,\n" +
            "        \"preview\": 1,\n" +
            "        \"sid\": 467337,\n" +
            "        \"source\": \"\",\n" +
            "        \"storyboard\": 0,\n" +
            "        \"tags\": \"40mp 40meter-p dream map cover utaite momochikun frey\",\n" +
            "        \"title\": \"Yume Chizu\",\n" +
            "        \"titleU\": \"夢地図\",\n" +
            "        \"video\": 0\n" +
            "      }\n" +
            "}";
}

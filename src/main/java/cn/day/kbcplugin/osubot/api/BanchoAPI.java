package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.pojo.bancho.BanchoUser;
import cn.day.kbcplugin.osubot.pojo.bancho.Beatmap;
import cn.day.kbcplugin.osubot.pojo.bancho.BanchoScore;
import cn.day.kbcplugin.osubot.pojo.bancho.Userinfo;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.utils.ScoreUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public class BanchoAPI {
    private static final String getUserURL = "https://osu.ppy.sh/api/get_user";
    private static final String getBPURL = "https://osu.ppy.sh/api/get_user_best";
    private static final String getMapURL = "https://osu.ppy.sh/api/get_beatmaps";
    private static final String getRecentURL = "https://osu.ppy.sh/api/get_user_recent";
    private static final String getScoreURL = "https://osu.ppy.sh/api/get_scores";
    private static final String getMatchURL = "https://osu.ppy.sh/api/get_match";
    private static final String getReplayURL = "https://osu.ppy.sh/api/get_replay";
    private static final String AVA_URL = "https://a.ppy.sh/";
    private static final String USERPAGE_URL = "https://osu.ppy.sh/u/";
    private static final String OSU_FILE_URL = "https://osu.ppy.sh/osu/";

    private static final String BASE_URL = "https://osu.ppy.sh/api/";

    private static final String Osu_File_URL = "https://old.ppy.sh/osu/";

    private final String OSU_API_KEY;

    public BanchoAPI() {
        this.OSU_API_KEY = Main.config.getString("OSU_APIKEY");
    }

    /**
     * 从.osu文件中匹配出BG文件名的表达式
     */
    public final static Pattern BGNAME_REGEX = Pattern.compile("(\\w+)\\.(jpg|png|jpeg)");
    private final static List<String> BGTYPE_REGEX = Arrays.asList("jpg", "png", "jpeg");

    private HashMap<Integer, Document> map = new HashMap<>();

    public BanchoUser getUser(Integer mode, String userId) {
        String result = accessAPI("user", userId, "id", null, null, null, null, mode);
        BanchoUser userFromAPI = JSONUtil.parseObj(result).toBean(BanchoUser.class);
        if(userFromAPI.getUserName()==null){
            userFromAPI=null;
        }
        if (userFromAPI != null) {
            //请求API时加入mode的标记，并且修复Rank问题
            userFromAPI.setMode(mode);
            fixRank(userFromAPI);
            userFromAPI.setQueryDate(LocalDate.now());
        }
        return userFromAPI;
    }

    public List<? extends AbstractScore> getBP(Integer mode, String userId) {
        String result = accessAPI("bp", userId, "id", null, null, null, null, mode);
        //由于这里用到List，手动补上双括号
        result = "[" + result + "]";
        List<BanchoScore> list = JSONUtil.parseArray(result).toList(BanchoScore.class);
        for (BanchoScore s : list) {
            s.setMode(mode.byteValue());
        }
        return list;
    }

    public Beatmap getBeatmap(String bid) {
        String result = accessAPI("beatmap", null, null, bid, null, null, null, null);
        return JSONUtil.parseObj(result).toBean(Beatmap.class);
        //new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(result, Beatmap.class);
    }


    @Nullable
    public File getOsuFile(Integer bid, Integer sid) {
        File mapFolder = new File(Main.BeatMapsPath, String.valueOf(sid));
        if (!mapFolder.exists() || !mapFolder.isDirectory()) {
            mapFolder.mkdirs();
        }
        File map = new File(mapFolder, bid + ".osu");
        if (map.exists()) return map;
        //getMap
        if (downloadMap(bid, map)) {
            return map;
        } else {
            return null;
        }
    }

    private boolean downloadMap(Integer bid, File target) {
        boolean success = true;
        FileUtil.touch(target);
        try {
            HttpUtil.downloadFile(Osu_File_URL + "/" + bid, target, new StreamProgress() {
                @Override
                public void start() {
                    Main.logger.info("开始下载地图:{}", bid);
                }

                @Override
                public void progress(long total, long progressSize) {
                    Main.logger.info("下载进度:{}/{}", FileUtil.readableFileSize(progressSize), FileUtil.readableFileSize(total));
                }

                @Override
                public void finish() {
                    Main.logger.info("铺面:{}下载完成", bid);
                }
            });
        } catch (Throwable e) {
            Main.logger.error("铺面下载失败", e);
            return false;
        }
        return success;
    }

    /**
     * Gets avatar.
     *
     * @param uid the uid
     * @return the avatar
     */
    public BufferedImage getAvatar(int uid) {
        URL avaurl;
        BufferedImage ava;
        BufferedImage resizedAva;
        Main.logger.info("开始获取玩家" + uid + "的头像");
        try {
            avaurl = new URL(AVA_URL + uid + "?" + System.currentTimeMillis() / 1000 + ".png");
            ava = ImageIO.read(avaurl);

            ImageInputStream iis = ImageIO.createImageInputStream(avaurl.openConnection().getInputStream());
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            String format = readers.next().getFormatName();
            if ("gif".equals(format)) {
                BufferedImage img = new BufferedImage(ava.getWidth(), ava.getHeight(), BufferedImage.TYPE_INT_ARGB);
                img.createGraphics().drawImage(ava, 0, 0, null);
                ava = img;
            }


            if (ava != null) {
                //进行缩放
                if (ava.getHeight() > 128 || ava.getWidth() > 128) {
                    //获取原图比例，将较大的值除以128，然后把较小的值去除以这个f
                    int resizedHeight;
                    int resizedWidth;
                    if (ava.getHeight() > ava.getWidth()) {
                        float f = (float) ava.getHeight() / 128;
                        resizedHeight = 128;
                        resizedWidth = (int) (ava.getWidth() / f);
                    } else {
                        float f = (float) ava.getWidth() / 128;
                        resizedHeight = (int) (ava.getHeight() / f);
                        resizedWidth = 128;
                    }
                    resizedAva = new BufferedImage(resizedWidth, resizedHeight, ava.getType());
                    Graphics2D g = (Graphics2D) resizedAva.getGraphics();
                    g.drawImage(ava.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH), 0, 0, resizedWidth, resizedHeight, null);
                    g.dispose();
                    ava.flush();
                } else {
                    //如果不需要缩小，直接把引用转过来
                    resizedAva = ava;
                }
                return resizedAva;
            } else {
                return null;
            }

        } catch (IOException e) {
            //存在没设置头像的情况 不做提醒
            return null;
        }

    }
    private BufferedImage obtainMap(File mapFolder) {
        Optional<File> matchingFile = Arrays.stream(mapFolder.listFiles()).filter(file -> BGTYPE_REGEX.contains(FileTypeUtil.getType(file))).findFirst();
        if (matchingFile.isPresent()) {
            File bgFile = matchingFile.get();
            BufferedImage bg = null;
            try {
                bg = ImageIO.read(bgFile);
            } catch (IOException e) {
                return null;
            }
            BufferedImage resizedBG = resizeImg(bg, 1366, 768);
            return resizedBG;
        }
        return null;
    }


    /**
     * Gets rank.
     *
     * @param rScore the r score
     * @param start  the start
     * @param end    the end
     * @return the rank
     */
    public int getRank(long rScore, int start, int end) {
        long endValue = getScore(end);
        if (rScore < endValue || endValue == 0) {
            map.clear();
            return 0;
        }
        if (rScore == endValue) {
            map.clear();
            return end;
        }
        //第一次写二分法……不过大部分时间都花在算准确页数，和拿页面元素上了
        while (start <= end) {
            int middle = (start + end) / 2;
            long middleValue = getScore(middle);

            if (middleValue == 0) {
                map.clear();
                return 0;
            }
            if (rScore == middleValue) {
                // 等于中值直接返回
                //清空掉缓存
                map.clear();
                return middle;
            } else if (rScore > middleValue) {
                //rank和分数成反比，所以大于反而rank要在前半部分找
                end = middle - 1;
            } else {
                start = middle + 1;
            }
        }
        map.clear();
        return 0;
    }

    /**
     * Gets last active.
     *
     * @param uid the uid
     * @return the last active
     */
    public Date getLastActive(int uid) {
        int retry = 0;
        Document doc = null;
        while (retry < 5) {
            try {
                Main.logger.info("正在获取" + uid + "的上次活跃时间");
                doc = Jsoup.connect(USERPAGE_URL + uid).timeout((int) Math.pow(2, retry + 1) * 1000).get();
                break;
            } catch (IOException e) {
                Main.logger.error("出现IO异常：" + e.getMessage() + "，正在重试第" + (retry + 1) + "次");
                retry++;
            }
        }
        if (retry == 5) {
            Main.logger.error("玩家" + uid + "请求API获取数据，失败五次");
            return null;
        }
        Elements link = doc.select("time[class*=timeago]");
        if (link.size() == 0) {
            return null;
        }
        String a = link.get(1).text();
        a = a.substring(0, 19);
        try {
            //转换为北京时间
            return new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(a).getTime() + 8 * 3600 * 1000);
        } catch (ParseException e) {
            Main.logger.error("将时间转换为Date对象出错");
        }
        return null;
    }


    /**
     * 让图片肯定不会变形，但是会切掉东西的拉伸
     *
     * @param bg     the bg
     * @param weight the weight
     * @param height the height
     * @return the buffered image
     */
    public BufferedImage resizeImg(BufferedImage bg, Integer weight, Integer height) {

        BufferedImage resizedBG;
        //获取bp原分辨率，将宽拉到1366，然后算出高，减去768除以二然后上下各减掉这部分
        int resizedWeight = weight;
        int resizedHeight = (int) Math.ceil((float) bg.getHeight() / bg.getWidth() * weight);
        int heightDiff = ((resizedHeight - height) / 2);
        int widthDiff = 0;
        //如果算出重画之后的高<768(遇到金盏花这种特别宽的)
        if (resizedHeight < height) {
            resizedWeight = (int) Math.ceil((float) bg.getWidth() / bg.getHeight() * height);
            resizedHeight = height;
            heightDiff = 0;
            widthDiff = ((resizedWeight - weight) / 2);
        }
        //把BG横向拉到1366;
        //忘记在这里处理了
        BufferedImage resizedBGTmp = new BufferedImage(resizedWeight, resizedHeight, bg.getType());
        Graphics2D g = resizedBGTmp.createGraphics();
        g.drawImage(bg.getScaledInstance(resizedWeight, resizedHeight, Image.SCALE_SMOOTH), 0, 0, resizedWeight, resizedHeight, null);
        g.dispose();

        //切割图片
        resizedBG = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < weight; x++) {
            //这里之前用了原bg拉伸之前的分辨率，难怪报错
            for (int y = 0; y < height; y++) {
                resizedBG.setRGB(x, y, resizedBGTmp.getRGB(x + widthDiff, y + heightDiff));
            }
        }
        //刷新掉bg以及临时bg的缓冲，将其作废
        bg.flush();
        resizedBGTmp.flush();
        return resizedBG;
    }

    private long getScore(int rank) {
        Document doc = null;
        int retry = 0;
        Main.logger.info("正在抓取#" + rank + "的玩家的分数");
        //一定要把除出来的值强转
        //math.round好像不太对，应该是ceil
        int p = (int) Math.ceil((float) rank / 50);
        //获取当前rank在当前页的第几个
        int num = (rank - 1) % 50;
        //避免在同一页内的连续查询，将上次查询的doc和p缓存起来
        if (map.get(p) == null) {
            while (retry < 5) {
                try {
                    doc = Jsoup.connect("https://osu.ppy.sh/rankings/osu/score?page=" + p).timeout((int) Math.pow(2, retry + 1) * 1000).get();
                    break;
                } catch (IOException e) {
                    Main.logger.error("出现IO异常：" + e.getMessage() + "，正在重试第" + (retry + 1) + "次");
                    retry++;
                }

            }
            if (retry == 5) {
                Main.logger.error("查询分数失败五次");
                return 0;
            }
            map.put(p, doc);
        } else {
            doc = map.get(p);
        }
        String score = doc.select("td[class*=focused]").get(num).child(0).attr("title");
        return Long.parseLong(score.replace(",", ""));

    }

    private static void fixRank(BanchoUser userFromAPI) {
        userFromAPI.setCount_rank_ss(userFromAPI.getCount_rank_ss() + userFromAPI.getCount_rank_ssh());
        userFromAPI.setCount_rank_s(userFromAPI.getCount_rank_s() + userFromAPI.getCount_rank_sh());
    }

    private static String accessAPI(String apiType, String uid, String uidType, String bid, String hash, Integer rank, String mid, Integer mode) {
        String URL;
        String failLog;
        String output = null;
        final String key = Main.config.getString("OSU_APIKEY");
        switch (apiType) {
            case "user":
                URL = getUserURL + "?k=" + key + "&type=" + uidType + "&m=" + mode + "&u=" + URLUtil.encode(uid, StandardCharsets.UTF_8);
                failLog = "玩家" + uid + "请求API：get_user失败五次";
                break;
            case "bp":
                URL = getBPURL + "?k=" + key + "&m=" + mode + "&type=" + uidType + "&limit=100&u=" + URLUtil.encode(uid, StandardCharsets.UTF_8);
                failLog = "玩家" + uid + "请求API：get_user_best失败五次";
                break;
            case "beatmap":
                URL = getMapURL + "?k=" + key + "&b=" + bid;
                failLog = "谱面" + bid + "请求API：get_beatmaps失败五次";
                break;
            case "beatmaps":
                URL = getMapURL + "?k=" + key + "&s=" + bid;
                failLog = "谱面" + bid + "请求API：get_beatmaps失败五次";
                break;
            case "beatmapHash":
                URL = getMapURL + "?k=" + key + "&h=" + hash;
                failLog = "谱面" + bid + "请求API：get_beatmaps失败五次";
                break;
            case "recent":
                URL = getRecentURL + "?k=" + key + "&m=" + mode + "&type=" + uidType + "&limit=1&u=" + URLUtil.encode(uid, StandardCharsets.UTF_8);
                failLog = "玩家" + uid + "请求API：get_recent失败五次";
                break;
            case "recents":
                URL = getRecentURL + "?k=" + key + "&m=" + mode + "&type=" + uidType + "&limit=100&u=" + URLUtil.encode(uid, StandardCharsets.UTF_8);
                failLog = "玩家" + uid + "请求API：get_recent失败五次";
                break;
            case "first":
                URL = getScoreURL + "?k=" + key + "&m=" + mode + "&limit=" + rank + "&b=" + bid;
                failLog = "谱面" + bid + "请求API：get_scores失败五次";
                break;
            case "score":
                URL = getScoreURL + "?k=" + key + "&m=" + mode + "&type=" + uidType + "&u=" + uid + "&b=" + bid;
                failLog = "谱面" + bid + "请求API：get_scores失败五次";
                break;
            case "match":
                URL = getMatchURL + "?k=" + key + "&mp=" + mid;
                failLog = "谱面" + bid + "请求API：get_scores失败五次";
                break;
            case "replay":
                URL = getReplayURL + "?k=" + key + "&s=" + mid;
                failLog = "谱面" + bid + "请求API：get_replay失败五次";
                break;
            default:
                Main.logger.info("apiType错误");
                return null;

        }

        int retry = 0;
        while (retry < 5) {
            HttpRequest request = new HttpRequest(UrlBuilder.of(URL));
            //设置请求头
            request.setMethod(Method.GET);
            request.header("Accept", "application/json");
            request.setConnectionTimeout((int) Math.pow(2, retry + 1) * 1000);
            request.setReadTimeout((int) Math.pow(2, retry + 1) * 1000);
            HttpResponse response = null;
            try {
                response = request.execute();
            }catch (Throwable t){
                Main.logger.info("HTTP GET请求失败: " + t.getLocalizedMessage() + "，正在重试第" + (retry + 1) + "次");
                retry++;
                continue;
            }
            if (response.getStatus() != 200) {
                Main.logger.info("HTTP GET请求失败: " + response.getStatus() + "，正在重试第" + (retry + 1) + "次");
                retry++;
                continue;
            }

            //读取返回结果
            String temp = response.body();
            //去掉两侧的中括号
            output = temp.substring(1, temp.length() - 1);
            response.close();
            break;
        }
        if (retry == 5) {
            Main.logger.error(failLog);
            return null;
        }
        return output;
    }

    public BanchoScore getUserRecent(Integer bancho_id, Integer mode) {
        Map<String, Object> paramMap = MapUtil.builder("", new Object())
                .put("k", OSU_API_KEY)
                .put("u", bancho_id)
                .put("mode", mode)
                .put("limit", 1)
                .put("type", "id").build();
        JSONObject response = JSONUtil.parseArray(HttpUtil.get(BASE_URL + "get_user_recent", paramMap)).getJSONObject(0);;
        if (response == null || !response.containsKey("beatmap_id")) {
            return null;
        }
        BanchoScore score = response.toBean(BanchoScore.class);
        score.setMode(mode.byteValue());
        score.setUserId(bancho_id);
        score.setAcc(ScoreUtil.genAccDouble(score, mode).floatValue());
        if (score.getRank().equals("F")) score.setPass(1);
        else score.setPass(1);
        return score;
    }

    public Beatmap getBeatmap(Integer map_id, @Nullable Integer mode) {
        Map<String, Object> paramMap = MapUtil.builder("", new Object())
                .put("k", OSU_API_KEY)
                .put("b", map_id)
                .put("limit", 1).build();
        if (mode != null && !mode.equals(1)) {
            paramMap.put("m", mode);
            paramMap.put("a", 1);
        }
        JSONObject response = JSONUtil.parseArray(HttpUtil.get(BASE_URL + "get_beatmaps", paramMap)).getJSONObject(0);
        if (response == null || !response.containsKey("approved")) {
            return null;
        }
        return response.toBean(Beatmap.class);
    }

}

package cn.day.kbcplugin.osubot.utils;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.api.BanchoAPI;
import cn.day.kbcplugin.osubot.enums.CompressLevelEnum;
import cn.day.kbcplugin.osubot.pojo.common.AbstractBeatmap;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.pojo.osu.OppaiResult;
import cn.hutool.core.io.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

import static cn.day.kbcplugin.osubot.enums.CompressLevelEnum.USHORT_555_RGB_PNG;


/**
 * 绘图工具类。
 *
 * @author QHS
 */

public class ImgUtil {

    private final BanchoAPI banchoApi;
    private final ScoreUtil scoreUtil;

    public ImgUtil(BanchoAPI banchoApi, ScoreUtil scoreUtil) {
        this.banchoApi = banchoApi;
        this.scoreUtil = scoreUtil;
        try {
            // 加载自定义字体
            Main.logger.info("加载自义定字体");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(Main.rootPath, "Gayatri.ttf"));
            // 注册自定义字体
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            Main.logger.warn("加载字体失败:使用默认字体");
        }
    }

    public BufferedImage get(String name) {
        byte[] data = FileUtil.readBytes(new File(Main.imgsPath, name));
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            return ImageIO.read(in);
        } catch (IOException e) {
            Main.logger.error("[获取图片失败]:" + name);
            return null;
        }
    }

    /**
     * 绘制Stat图片（已改造完成）
     * 为线程安全，将当前时间毫秒数加入文件名并返回(被废弃：已经采用base64编码)
     *
     * @param userFromAPI 最新用户信息
     * @param userInDB    作为对比的信息
     * @param role        用户组
     * @param day         对比的天数
     * @param approximate 是否是接近的数据
     * @param scoreRank   scoreRank
     * @param mode        模式，只支持0/1/2/3
     * @return Base64字串 string
     */
//    public String drawUserInfo(AbstractUserInfo userFromAPI, Userinfo userInDB, String role, int day, boolean approximate, int scoreRank, Integer mode) {
//        BufferedImage ava = banchoApi.getAvatar(userFromAPI.getUserId());
//        BufferedImage bg = null;
//        BufferedImage layout = getCopyImage(get("layout.png"));
//        BufferedImage scoreRankBG = getCopyImage(get("scorerank.png"));
//        //TODO issue
//        byte[] data = FileUtil.readBytes(new File(Main.imgsPath, userFromAPI.getUserId() + ".png"));
//        if (data != null) {
//            try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
//                bg = ImageIO.read(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (bg != null) {
//            bg = getCopyImage(bg);
//        } else {
//            try {
//                bg = getCopyImage(get(role + ".png"));
//            } catch (NullPointerException ignore) {
//                //这个异常是在出现了新用户组，但是没有准备这个用户组的背景时候出现
//                //2018-2-27 09:34:46 调整逻辑，没准备背景时候使用默认
//                bg = getCopyImage(get("creep.png"));
//            }
//        }
//
//        Graphics2D g2 = (Graphics2D) bg.getGraphics();
//        //绘制布局
//        g2.drawImage(layout, 0, 0, null);
//        //模式的图标
//        g2.drawImage(get("mode-" + mode + ".png"), 365, 80, null);
//        //用户组对应的背景
////        g2.drawImage(roleBg, 0, 0, null);
//        try {
//            //绘制头像
//            g2.drawImage(ava, 160, 22, null);
//        } catch (NullPointerException ignore) {
//            //没有头像。不做处理
//        }
//
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        //将score rank比用户名先画
//
//        if (scoreRank > 0) {
//            //把score rank用到的bg画到bg上
//            g2.drawImage(scoreRankBG, 653, 7, null);
//            Integer x;
//            if (scoreRank < 100) {
//                x = 722;
//            } else {
//                x = 697;
//            }
//            drawTextToImage(g2, "#FFFFFF", "Gayatri", 50, "#" + scoreRank, x, 58);
//        }
//
//        //绘制用户名
//        drawTextToImage(g2, "#000000", "Aller light", 48, userFromAPI.getUserName(), 349, 60);
//        //绘制Rank
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 48, "#" + userFromAPI.getPP(), 415, 114);
//        //绘制PP
//        drawTextToImage(g2, "#555555", "Futura Std Medium", 36, String.valueOf(userFromAPI.getPP()), 469, 157);
//        //绘制RankedScore
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 30,
//                new DecimalFormat("###,###").format(userFromAPI.getRankScore()), 370, 206);
//        //绘制acc
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 30,
//                new DecimalFormat("##0.00").format(userFromAPI.getAcc()) + "%", 357, 255);
//        //绘制pc
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 30,
//                new DecimalFormat("###,###").format(userFromAPI.getPlayCount()), 344, 302);
//        //绘制tth
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 30,
//                new DecimalFormat("###,###").format(userFromAPI.get() + userFromAPI.Count100() + userFromAPI.Count300()),
//                333, 350);
//        //绘制Level
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 30,
//                (int) Math.floor(userFromAPI.getLevel()) + " (" + (int) ((userFromAPI.getLevel() - Math.floor(userFromAPI.getLevel())) * 100) + "%)",
//                320, 398);
//        //绘制SS计数
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 24, Integer.toString(userFromAPI.getCountRankSs()), 343, 445);
//        //绘制S计数
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 24, Integer.toString(userFromAPI.getCountRankS()), 496, 445);
//        //绘制A计数
//        drawTextToImage(g2, "#222222", "Futura Std Medium", 24, Integer.toString(userFromAPI.getCountRankA()), 666, 445);
//        //绘制当时请求的时间
//        drawTextToImage(g2, "#BC2C00", "Ubuntu Medium", 18, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
////        new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
//                , 802, 452);
//        //---------------------------以上绘制在线部分完成--------------------------------
//        //试图查询数据库中指定日期的user
//        //这里应该是不需要防null判断的
//        if (day > 0) {
//                /*
//                不带参数：day=1，调用dbUtil拿当天凌晨（数据库中数值是昨天）的数据进行对比
//                带day = 0:进入本方法，不读数据库，不进行对比
//                day>1，例如day=2，21号进入本方法，查的是19号结束时候的成绩
//                */
//
//            //临时关闭平滑
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//            //只有day>1才会出现文字
//            if (approximate) {
//                //如果取到的是模糊数据
//                drawTextToImage(g2, "#666666", "宋体", 15, "请求的日期没有数据", 718, 138);
//                //算出天数差别
//                drawTextToImage(g2, "#666666", "宋体", 15, "『对比于" +
//                        ChronoUnit.DAYS.between(userInDB.getQueryDate(), LocalDate.now())
////                            Long.valueOf(((Calendar.getInstance().getTime().getTime() - .getTime()) / 1000 / 60 / 60 / 24)).toString()
//                        + "天前』", 725, 155);
//            } else {
//                //如果取到的是精确数据
//                drawTextToImage(g2, "#666666", "宋体", 15, "『对比于" + day + "天前』", 725, 155);
//            }
//
//
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            //这样确保了userInDB不是空的
//            //绘制Rank变化
//            if (userInDB.getRank() > userFromAPI.getRank()) {
//                //如果查询的rank比凌晨中的小
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + (userInDB.getRank() - userFromAPI.getRank()), 633, 109);
//            } else if (userInDB.getRank() < userFromAPI.getRank()) {
//                //如果掉了rank
//                drawTextToImage(g2, "#4466FF", "苹方", 24,
//                        "↓" + (userFromAPI.getRank() - userInDB.getRank()), 633, 109);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + 0, 633, 109);
//            }
//            //绘制PP变化
//            if (userInDB.getPP() > userFromAPI.getPP()) {
//                //如果查询的pp比凌晨中的小
//                drawTextToImage(g2, "#4466FF", "苹方", 24,
//                        "↓" + new DecimalFormat("##0.00").format(userInDB.getPP() - userFromAPI.getPP()), 633, 153);
//            } else if (userInDB.getPP() < userFromAPI.getPP()) {
//                //刷了PP
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + new DecimalFormat("##0.00").format(userFromAPI.getPP() - userInDB.getPP()), 633, 153);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + 0, 633, 153);
//            }
//
//            //绘制RankedScore变化
//            if (userInDB.getRankScore() < userFromAPI.getRankScore()) {
//                //因为RankedScore不会变少，所以不写蓝色部分
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + new DecimalFormat("###,###").format(userFromAPI.getRankScore() - userInDB.getRankScore()), 650, 203);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + 0, 650, 203);
//            }
//            //绘制ACC变化
//            //在这里把精度砍掉
//            if (Float.parseFloat(new DecimalFormat("##0.00").format(userInDB.getAcc())) > Float.parseFloat(new DecimalFormat("##0.00").format(userFromAPI.getAcc()))) {
//                //如果acc降低了
//                drawTextToImage(g2, "#4466FF", "苹方", 24,
//                        "↓" + new DecimalFormat("##0.00").format(userInDB.getAcc() - userFromAPI.getAcc()) + "%", 636, 251);
//            } else if (Float.parseFloat(new DecimalFormat("##0.00").format(userInDB.getAcc())) < Float.parseFloat(new DecimalFormat("##0.00").format(userFromAPI.getAcc()))) {
//                //提高
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + new DecimalFormat("##0.00").format(userFromAPI.getAcc() - userInDB.getAcc()) + "%", 636, 251);
//
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + new DecimalFormat("##0.00").format(0.00) + "%", 636, 251);
//            }
//
//            //绘制pc变化
//            if (userInDB.getPlayCount() < userFromAPI.getPlayCount()) {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + new DecimalFormat("###,###").format(userFromAPI.getPlayCount() - userInDB.getPlayCount()), 622, 299);
//
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + 0, 622, 299);
//
//            }
//
//            //绘制tth变化,此处开始可以省去颜色设置
//            if (userInDB.getCount50() + userInDB.Count100() + userInDB.Count300()
//                    < userFromAPI.getCount50() + userFromAPI.Count100() + userFromAPI.Count300()) {
//                //同理不写蓝色部分
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + new DecimalFormat("###,###").format(userFromAPI.getCount50() + userFromAPI.Count100() + userFromAPI.Count300() - (userInDB.getCount50() + userInDB.Count100() + userInDB.Count300())), 609, 347);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + 0, 609, 347);
//            }
//            //绘制level变化
//            if (Float.parseFloat(new DecimalFormat("##0.00").format(userInDB.getLevel())) < Float.parseFloat(new DecimalFormat("##0.00").format(userFromAPI.getLevel()))) {
//                //同理不写蓝色部分
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + (int) ((userFromAPI.getLevel() - userInDB.getLevel()) * 100) + "%", 597, 394);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 24,
//                        "↑" + 0 + "%", 597, 394);
//            }
//            //绘制SS count 变化
//            //这里需要改变字体大小
//            if (userInDB.getCountRankSs() > userFromAPI.getCountRankSs()) {
//                //如果查询的SS比凌晨的少
//                drawTextToImage(g2, "#4466FF", "苹方", 18,
//                        "↓" + (userInDB.getCountRankSs() - userFromAPI.getCountRankSs()), 414, 444);
//            } else if (userInDB.getCountRankSs() < userFromAPI.getCountRankSs()) {
//                //如果SS变多了
//                drawTextToImage(g2, "#FF6060", "苹方", 18,
//                        "↑" + (userFromAPI.getCountRankSs() - userInDB.getCountRankSs()), 414, 444);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 18,
//                        "↑" + 0, 414, 444);
//            }
//            //s
//            if (userInDB.getCountRankS() > userFromAPI.getCountRankS()) {
//                //如果查询的S比凌晨的少
//                drawTextToImage(g2, "#4466FF", "苹方", 18,
//                        "↓" + (userInDB.getCountRankS() - userFromAPI.getCountRankS()), 568, 444);
//            } else if (userInDB.getCountRankS() < userFromAPI.getCountRankS()) {
//                //如果S变多了
//                drawTextToImage(g2, "#FF6060", "苹方", 18,
//                        "↑" + (userFromAPI.getCountRankS() - userInDB.getCountRankS()), 568, 444);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 18,
//                        "↑" + 0, 568, 444);
//            }
//            //a
//            if (userInDB.getCountRankA() > userFromAPI.getCountRankA()) {
//                //如果查询的S比凌晨的少
//                drawTextToImage(g2, "#4466FF", "苹方", 18,
//                        "↓" + (userInDB.getCountRankA() - userFromAPI.getCountRankA()), 738, 444);
//            } else if (userInDB.getCountRankA() < userFromAPI.getCountRankA()) {
//                //如果S变多了
//                drawTextToImage(g2, "#FF6060", "苹方", 18,
//                        "↑" + (userFromAPI.getCountRankA() - userInDB.getCountRankA()), 738, 444);
//            } else {
//                drawTextToImage(g2, "#FF6060", "苹方", 18,
//                        "↑" + 0, 738, 444);
//            }
//        }
//        g2.dispose();
//        return drawImage(bg, 不压缩);
//    }

    /**
     * 绘制结算界面
     *
     * @param userName the username
     * @param score    the score
     * @param beatmap  the beatmap
     * @param mode     the mode
     * @return the string
     */
    public String drawResult(String userName, AbstractScore score, AbstractBeatmap beatmap, int mode) {
        String accS = new DecimalFormat("###.00").format((double)score.Acc());
        Map<String, String> mods = ScoreUtil.convertModToHashMap(score.mods());
        //这个none是为了BP节省代码，在这里移除掉
        mods.remove("None");
        //离线计算PP
        OppaiResult oppaiResult = null;
        try {
            oppaiResult = scoreUtil.calcPP_rosu(score, beatmap);
        } catch (Exception e) {
            //如果acc过低或者不是std
            Main.logger.error("rsou-pp出错", e);
        }
        BufferedImage bg = obtainBg(Main.sayobotApi.getBG(beatmap));
        if (bg == null) {
            //TODO usingDefaultBG();
            return null;
        }

        //2017-11-3 17:51:47这里有莫名的空指针，比较迷，在webPageManager.getBG里加一个判断为空则抛出空指针看看
        Graphics2D g2 = (Graphics2D) bg.getGraphics();
        //画上各个元素，这里Images按文件名排序
        //顶端banner(下方也暗化了20%，JAVA自带算法容易导致某些图片生成透明图片)
        g2.drawImage(get("bpBanner.png"), 0, 0, null);
        //Rank
        g2.drawImage(get("ranking-" + score.Grade() + ".png").getScaledInstance(get("ranking-" + score.Grade() + ".png").getWidth(), get("ranking-" + score.Grade() + ".png").getHeight(), Image.SCALE_SMOOTH), 1131 - 245, 341 - 242, null);

        //FC
        if (score.isPerfect()) {
            g2.drawImage(get("ranking-perfect.png"), 296 - 30, 675 - 55, null);
        }
        //分数 图片扩大到1.27倍
        //分数是否上e，每个数字的位置都不一样
        if (score.Score() > 99999999) {
            char[] Score = String.valueOf(score.Score()).toCharArray();
            for (int i = 0; i < Score.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + Score[i] + ".png"), 55 * i + 128 - 21, 173 - 55, null);
            }
        } else {
            char[] Score = String.valueOf(score.Score()).toCharArray();
            for (int i = 0; i < 8; i++) {
                if (Score.length < 8) {
                    //如果分数不到8位，左边用0补全
                    //获取Score的长度和8的差距，然后把i小于等于这个差距的时候画的数字改成0
                    if (i < 8 - Score.length) {
                        g2.drawImage(get("score-0.png"), 55 * i + 141 - 6, 173 - 55, null);
                    } else {
                        //第一次应该拿的是数组里第0个字符
                        g2.drawImage(get("score-" + Score[i - 8 + Score.length] + ".png"), 55 * i + 141 - 6, 173 - 55, null);
                    }
                } else {
                    //直接绘制
                    g2.drawImage(get("score-" + Score[i] + ".png"), 55 * i + 141 - 6, 173 - 55, null);
                }
            }
        }


        //combo
        char[] Combo = String.valueOf(score.maxCombo()).toCharArray();
        for (int i = 0; i < Combo.length; i++) {
            //第二个参数是数字之间的距离+第一个数字离最左边的距离
            g2.drawImage(get("score-" + Combo[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 30 - 7, 576 - 55 + 10, null);
        }
        //画上结尾的x
        g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * Combo.length + 30 - 7, 576 - 55 + 10, null);


        char[] count300 = String.valueOf(score.Count300()).toCharArray();
        char[] countgeki = String.valueOf(score.CountGeki()).toCharArray();
        char[] count100 = String.valueOf(score.Count100()).toCharArray();
        char[] countkatu = String.valueOf(score.CountKatu()).toCharArray();
        char[] count50 = String.valueOf(score.Count50()).toCharArray();
        char[] count0 = String.valueOf(score.CountMiss()).toCharArray();
        {
            g2.drawImage(get("hit300.png").getScaledInstance(73, 73, Image.SCALE_SMOOTH), 30 - 4, 245 - 27, null);
            for (int i = 0; i < count300.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + count300[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 134 - 7, 238 - 7, null);
            }
            //画上结尾的x
            g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * count300.length + 134 - 7, 238 - 7, null);

            //激
            g2.drawImage(get("hit300.png").getScaledInstance(73, 73, Image.SCALE_SMOOTH), 350 - 4, 245 - 27, null);
            for (int i = 0; i < countgeki.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + countgeki[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 455 - 8, 238 - 7, null);
            }
            //画上结尾的x
            g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * countgeki.length + 455 - 8, 238 - 7, null);
            //100
            g2.drawImage(get("hit100.png").getScaledInstance(50, 30, Image.SCALE_SMOOTH), 44 - 5, 346 - 8, null);
            for (int i = 0; i < count100.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + count100[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 134 - 7, 374 - 55, null);
            }
            //画上结尾的x
            g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * count100.length + 134 - 7, 374 - 55, null);

            //喝
            g2.drawImage(get("hit100.png").getScaledInstance(50, 30, Image.SCALE_SMOOTH), 364 - 5, 346 - 8, null);
            for (int i = 0; i < countkatu.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + countkatu[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 455 - 8, 374 - 55, null);
            }
            //画上结尾的x
            g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * countkatu.length + 455 - 8, 374 - 55, null);

            //50
            g2.drawImage(get("hit50.png").getScaledInstance(35, 30, Image.SCALE_SMOOTH), 51 - 5, 455 - 21, null);
            for (int i = 0; i < count50.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + count50[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 134 - 7, 470 - 55, null);
            }
            //画上结尾的x
            g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * count50.length + 134 - 7, 470 - 55, null);

            //x
            g2.drawImage(get("hit0.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH), 376 - 4, 437 - 5, null);
            for (int i = 0; i < count0.length; i++) {
                //第二个参数是数字之间的距离+第一个数字离最左边的距离
                g2.drawImage(get("score-" + count0[i] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * i + 455 - 8, 470 - 55, null);
            }
            //画上结尾的x
            g2.drawImage(get("score-x.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * count0.length + 455 - 8, 470 - 55, null);

            if (oppaiResult != null) {
//                //进度条
//                if (score.getRank().equals("F")) {
//                    int progress = 300 * (score.getCount50() + score.Count100() + score.Count300() + score.CountMiss())
//                            / (oppaiResult.getNumCircles() + oppaiResult.getNumSliders() + oppaiResult.getNumSpinners());
//
//                    //设置直线断点平滑
//                    g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//                    g2.setColor(Color.decode("#99cc31"));
//                    g2.drawLine(262, 615, 262 + progress - 2, 615);
//                    g2.setColor(Color.decode("#fe0000"));
//                    g2.drawLine(262 + progress - 2, 615, 262 + progress, 753);
//                }
                //底端PP面板，在oppai计算结果不是null的时候
                //600,700 570 700
                g2.drawImage(get("ppBanner.png"), 570, 705, null);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(Color.decode("#ff66a9"));
                g2.setFont(new Font("Gayatri", Font.PLAIN, 60));
                //临时修正，BP命令总PP使用官网爬到的
                if (score.PP() != null) {
                    if (score.PP() > 1000) {
                        // 630,748 582 753
                        g2.drawString(String.valueOf(Math.round(score.PP())), 582, 753);
                    } else {
                        if (String.valueOf(Math.round(score.PP())).contains("1")) {
                            //650 748 607 753
                            g2.drawString(String.valueOf(Math.round(score.PP())), 607, 753);
                        } else {
                            //650 748 592 753
                            g2.drawString(String.valueOf(Math.round(score.PP())), 592, 753);
                        }
                    }
                } else {
//                    2019-7-24
//                    【中军】skystaR<rize@pending.moe>  13:31:01
//                    这个插件的制作者都没想到能有四位数pp
//                    【中军】skystaR<rize@pending.moe>  13:31:05
//                    @活泼花猫
                    if (oppaiResult.getPp() > 1000) {
                        g2.drawString(String.valueOf(Math.round(oppaiResult.getPp())), 582, 753);
                    } else {
                        if (String.valueOf(Math.round(oppaiResult.getPp())).contains("1")) {
                            g2.drawString(String.valueOf(Math.round(oppaiResult.getPp())), 607, 753);
                        } else {
                            g2.drawString(String.valueOf(Math.round(oppaiResult.getPp())), 592, 753);
                        }
                    }

                }
                g2.setFont(new Font("Gayatri", Font.PLAIN, 48));
                g2.drawString(String.valueOf(Math.round(oppaiResult.getAimPp())), 834, 758);//834 753 834 758
                g2.drawString(String.valueOf(Math.round(oppaiResult.getSpeedPp())), 932, 758);//925 753 932 758
                g2.drawString(String.valueOf(Math.round(oppaiResult.getAccPp())), 1030, 758);//1015 753 1030 758
                g2.drawString(String.valueOf(Math.round(oppaiResult.getMaxPP())), 1120, 758);//1102 753 1120 758
            }
        }
        //TODO 其他模式，但是找不到素材了.jpg
        //acc
        if (score.Acc() == 100.0f) {
            //从最左边的数字开始，先画出100
            g2.drawImage(get("score-1.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * 0 + 317 - 8, 576 - 55 + 10, null);
            g2.drawImage(get("score-0.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * 1 + 317 - 8, 576 - 55 + 10, null);
            g2.drawImage(get("score-0.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * 2 + 317 - 8, 576 - 55 + 10, null);
            //打点
            g2.drawImage(get("score-dot.png").getScaledInstance(20, 45, Image.SCALE_SMOOTH), 37 * 1 + 407 - 8, 576 - 55 + 10, null);
            //从点的右边（+27像素）开始画两个0
            g2.drawImage(get("score-0.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 27 * 1 + 37 * 1 + 407 - 8, 576 - 55 + 10, null);
            g2.drawImage(get("score-0.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 27 * 1 + 37 * 2 + 407 - 8, 576 - 55 + 10, null);
            g2.drawImage(get("score-percent.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 27 * 1 + 407 - 8 + 37 * 3, 576 - 55 + 10, null);
        } else {
            //将ACC转化为整数部分、小数点和小数部分
            char[] accArray = accS.toCharArray();
            g2.drawImage(get("score-" + accArray[0] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * 0 + 317 - 8 + 15, 576 - 55 + 10, null);
            g2.drawImage(get("score-" + accArray[1] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 37 * 1 + 317 - 8 + 15, 576 - 55 + 10, null);
            //打点
            g2.drawImage(get("score-dot.png").getScaledInstance(20, 45, Image.SCALE_SMOOTH), 407 - 8, 576 - 55 + 15, null);

            g2.drawImage(get("score-" + accArray[3] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 27 * 1 + 407 - 8, 576 - 55 + 10, null);
            g2.drawImage(get("score-" + accArray[4] + ".png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 27 * 1 + 407 - 8 + 37 * 1, 576 - 55 + 10, null);
            g2.drawImage(get("score-percent.png").getScaledInstance(40, 51, Image.SCALE_SMOOTH), 27 * 1 + 407 - 8 + 37 * 2, 576 - 55 + 10, null);
        }
        if (!mods.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, String> entry : mods.entrySet()) {
                //第一个mod画在1237，第二个画在1237+30,第三个1237-30(没有实现)
                g2.drawImage(get("selection-mod-" + entry.getValue() + ".png"), 1237 - (50 * i), 375, null);
                i++;
            }
        }
        //写字
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //指定颜色
        g2.setPaint(Color.decode("#FFFFFF"));
        //指定字体
        g2.setFont(new Font("Aller light", Font.PLAIN, 29));
        //指定坐标
        g2.drawString(beatmap.getArtist() + " - " + beatmap.getTitle() + " [" + beatmap.getVersion() + "]", 7, 26);
        g2.setFont(new Font("Aller", Font.PLAIN, 21));
        g2.drawString("Beatmap by " + beatmap.getCreator(), 7, 52);
        g2.drawString("Played by " + userName + " on " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.of("UTC-8")).format(score.date().toInstant().plusSeconds(86400)) + ".", 7, 74);


        g2.dispose();

        return drawImage(bg, USHORT_555_RGB_PNG);
    }


    /**
     * 简单的复制一份图片……
     */
    private BufferedImage getCopyImage(BufferedImage bi) {
//        return bi.getSubimage(0, 0, bi.getWidth(), bi.getHeight());

        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        //这他妈不就是自己重写了getSubimage方法吗……为什么getSubimage返回的是原图，这个就能复制一份woc
        //不采用这套方案（会抹掉透明通道
//        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.SCALE_SMOOTH);
//        Graphics g = b.getGraphics();
//        g.drawImage(source, 0, 0, null);
//        g.dispose();
//        return b;
    }

    /**
     * 向图片上绘制字符串的方法……当时抽出来复用，但是方法名没取好
     * 2018-1-24 17:05:11去除配置文件的设定，反正以后要改也不可能去除旧命令。
     */
    private void drawTextToImage(Graphics2D g2, String color, String font,
                                 Integer size, String text, Integer x, Integer y) {
        //指定颜色
        g2.setPaint(Color.decode(color));
        //指定字体
        g2.setFont(new Font(font, Font.PLAIN, size));
        //指定坐标
        g2.drawString(text, x, y);

    }

    /**
     * 将图片转换为Base64字符串……
     *
     * @param img the img
     * @return the string
     */
    public String drawImage(BufferedImage img, CompressLevelEnum level) {
        BufferedImage result = img;
        switch (level) {
            case 不压缩:
                //什么也不做
                break;
            case JPG:
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    ImageIO.write(result, "jpg", out);
                    byte[] imgBytes = out.toByteArray();
                    return Base64.getEncoder().encodeToString(imgBytes);
                } catch (IOException e) {
                    Main.logger.error(e.getMessage());
                    return null;
                }
            case USHORT_555_RGB_PNG:
                result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_555_RGB);
                Graphics2D g3 = result.createGraphics();
                g3.clearRect(0, 0, img.getWidth(), img.getHeight());
                g3.drawImage(img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
                g3.dispose();
                break;
            default:
                return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(result, "png", out);
            byte[] imgBytes = out.toByteArray();
            return Base64.getEncoder().encodeToString(imgBytes);
        } catch (IOException e) {
            Main.logger.error(e.getMessage());
            return null;
        }
    }

    private BufferedImage obtainBg(File bgFile) {
        try {
            BufferedImage bg = ImageIO.read(bgFile);
            return Main.banchoApi.resizeImg(bg, 1366, 768);
        } catch (IOException e) {
            Main.logger.error("读取图片失败", e);
        }
        return null;
    }
}

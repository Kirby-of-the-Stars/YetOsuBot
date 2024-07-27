package cn.day.kbcplugin.osubot.api;

//小夜永远的神
public class SayobotApi {
    private static final String BASE_URL = "https://api.sayobot.cn";
    /*
        Integer getSid();
    Integer getBid();
    String getArtist();
    String getTitle();
    Double getBpm();
    Integer getMaxCombo();
    String getVersion();
    String getCreator();
     */
//    @Nullable
//    public SayoMapInfo_old getMapInfo(Integer bid) {
//        Map<String, Object> paramMap = MapUtil.builder(new HashMap<String, Object>())
//                .put("K", bid)
//                .put("T", 1).build();
//        JSONObject response = JSONUtil.parseObj(HttpUtil.get(BASE_URL + "/v2/beatmapinfo", paramMap));
//        if (response.getInt("status") != null && response.getInt("status").equals(0)) {
//            List<SayoMapInfo_old> map_list = response.getJSONObject("data").getBeanList("bid_data", SayoMapInfo_old.class);
//            SayoMapSetInfo_old mapSetInfo = response.get("data", SayoMapSetInfo_old.class);
//            for (SayoMapInfo_old map : map_list) {
//                if (Objects.equals(map.getBid(), bid)){
//                    map.setSid(mapSetInfo.getSid());
//                    map.setArtist(mapSetInfo.getArtist());
//                    map.setTitle(mapSetInfo.getTitle());
//                    map.setCreator(mapSetInfo.getCreator());
//                    return map;
//                }
//            }
//            Main.logger.warn("无法找到地图{}的信息", bid);
//        } else {
//            Main.logger.warn("获取地图{}信息失败", bid);
//            Main.logger.warn("响应结果:{}", response.toJSONString(0));
//        }
//        return null;
//    }
//
//    @Nullable
//    public File getBG(AbstractBeatmap info){
//        File mapFolder = new File(Main.BeatMapsPath,String.valueOf(info.getSid()));
//        if(!mapFolder.exists() || !mapFolder.isDirectory()){
//            mapFolder.mkdirs();
//        }
//        File bg = new File(mapFolder,info.getBgName());
//        if(bg.exists()){
//            return bg;
//        }
//        if(downloadBG(info.getSid(), info.getBgName(), bg)) return bg;
//        else return null;
//    }
//
//    public boolean downloadBG(Integer sid,String bg_name,File target){
//        try{
//            FileUtil.touch(target);
//            long size = HttpUtil.downloadFile("https://dl.sayobot.cn/beatmaps/files/" + sid + '/' + bg_name, target);
//        }catch (Throwable t){
//            Main.logger.warn("下载{}的bg失败,bg_name:{}",sid,bg_name);
//            Main.logger.warn("exception:",t);
//            return false;
//        }
//        return true;
//    }
//
//    public AbstractBeatmap getMap(int bid) {
//        SayoDBMap map = Main.beatMapDao.selectById(bid);
//        if(map!=null) return map;
//        Main.logger.info("从API当中获取{}的数据,并尝试进入数据库当中",bid);
//        SayoMapSetInfo_old beatmapSet = getMapSetInfo(bid);
//        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
//        int count = beatmapSet.getBids_amount();
//        for (int i = 0; i < count; i++) {
//            SayoDBMap beatmap = new SayoDBMap();
//            BeanUtil.copyProperties(beatmapSet,beatmap,copyOptions);
//            SayoMapInfo_old info = beatmapSet.bid_data.get(i);
//            BeanUtil.copyProperties(info,beatmap,copyOptions);
//            beatmap.bpm = beatmapSet.getBpm();
//            beatmap.sid = beatmapSet.getSid();
//            if(beatmap.getBid().equals(bid)){
//                map = beatmap;
//            }
//            if(BeatmapStatus.isOnline(beatmapSet.getApproved())){
//                Main.beatMapDao.insert(beatmap);
//            }
//        }
//        return map;
//    }
//
//    public SayoMapSetInfo_old getMapSetInfo(int bid){
//                Map<String, Object> paramMap = MapUtil.builder(new HashMap<String, Object>())
//                .put("K", bid)
//                .put("T", 1).build();
//        JSONObject response = JSONUtil.parseObj(HttpUtil.get(BASE_URL + "/v2/beatmapinfo", paramMap));
//        return response.getJSONObject("data").toBean(SayoMapSetInfo_old.class);
//    }


}

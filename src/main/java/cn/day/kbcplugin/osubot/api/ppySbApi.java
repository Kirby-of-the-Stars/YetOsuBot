package cn.day.kbcplugin.osubot.api;

import cn.day.kbcplugin.osubot.pojo.bancho.BanchoScore;
import cn.day.kbcplugin.osubot.pojo.common.AbstractScore;
import cn.day.kbcplugin.osubot.pojo.ppysb.SbPlayer;
import cn.day.kbcplugin.osubot.pojo.ppysb.SbPlayerStats;
import cn.day.kbcplugin.osubot.pojo.ppysb.SbScore;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ppySbApi {

    private static final String BASE_URL = "https://api.ppy.sb";


    @Nullable
    public BanchoScore getRecent(Integer ppy_sb_id, Integer mode) {
        String api_url = "/v1/get_player_scores";
        Map<String, Object> parmerMap = MapUtil.builder("key", new Object())
                .put("scope", "recent")
                .put("id", ppy_sb_id)
                .put("mode", mode.toString())
                .put("limit", 5)
                .put("include_loved", true)
                .put("include_failed", true).build();
        JSONObject response = JSONUtil.parseObj(HttpUtil.get(BASE_URL+api_url,parmerMap));
        if(response.isEmpty()|| !response.getStr("status").equals("success")){
            return null;
        }
        JSONObject scoreJson = response.getJSONArray("scores").getJSONObject(0);
        JSONObject beatmapJson = scoreJson.getJSONObject("beatmap");
        BanchoScore score = new BanchoScore();
        score.setBeatmapId(beatmapJson.getInt("id").toString());
        score.setBeatmapName(beatmapJson.getStr("title"));
        score.setScore(scoreJson.getLong("score"));
        score.setPp(scoreJson.getFloat("pp"));
        score.setAcc(scoreJson.getFloat("acc"));
        score.setMaxCombo(scoreJson.getInt("max_combo"));
        score.setEnabledMods(scoreJson.getInt("mods"));
        score.setCount300(scoreJson.getInt("n300"));
        score.setCount100(scoreJson.getInt("n100"));
        score.setCount50(scoreJson.getInt("n50"));
        score.setCountGeki(scoreJson.getInt("ngeki"));
        score.setCountKatu(scoreJson.getInt("nkatu"));
        score.setCountMiss(scoreJson.getInt("nmiss"));
        score.setRank(scoreJson.getStr("grade"));
        score.setPerfect(scoreJson.getInt("perfect"));
        score.setDate(scoreJson.getDate("play_time"));
        return score;
    }
    @Nullable
    public SbPlayer getUserInfo(Integer ppy_sb_id){
        Map<String, Object> paramMap = MapUtil.builder("", new Object())
                .put("scope", "all")
                .put("id", ppy_sb_id)
                .build();
        JSONObject response = JSONUtil.parseObj(HttpUtil.get(BASE_URL+"/v1/get_player_info",paramMap));
        if(!response.containsKey("status") || !response.getStr("status").equals("success")){
            return null;
        }
        JSONObject player = response.getJSONObject("player");
        SbPlayer bean = player.getJSONObject("info").toBean(SbPlayer.class);
        bean.stats = player.getJSONObject("stats").getJSONObject(String.valueOf(bean.getPreferred_mode())).toBean(SbPlayerStats.class);
        return bean;
    }
    @Nullable
    public List<? extends AbstractScore> getBestScores(Integer osuId, int limit, Integer mode){
        String api_url = "/v1/get_player_scores";
        Map<String, Object> parmerMap = MapUtil.builder("key", new Object())
                .put("scope", "best")
                .put("id", osuId)
                .put("mode", mode.toString())
                .put("limit", limit)
                .put("include_loved", false)
                .put("include_failed", false).build();
        JSONObject response = JSONUtil.parseObj(HttpUtil.get(BASE_URL+api_url,parmerMap));
        if(response.isEmpty()|| !response.getStr("status").equals("success")){
            return null;
        }
        return response.getBeanList("scores", SbScore.class);
    }

}

package cn.day.kbcplugin.osubot.dao;

import cn.day.kbcplugin.osubot.db.BaseDao;
import cn.day.kbcplugin.osubot.pojo.SayoDBMap;

import javax.sql.DataSource;

public class BeatMapDao extends BaseDao<SayoDBMap> {
    public BeatMapDao(DataSource ds) {
        super(ds);
    }
}

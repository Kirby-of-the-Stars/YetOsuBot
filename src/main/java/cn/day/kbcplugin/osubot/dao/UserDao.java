package cn.day.kbcplugin.osubot.dao;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.db.BaseDao;
import cn.day.kbcplugin.osubot.pojo.Account;
import cn.day.kbcplugin.osubot.pojo.PluginUser;
import cn.hutool.db.Entity;
import cn.hutool.db.Session;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDao extends BaseDao<PluginUser> {

    public UserDao(DataSource ds) {
        super(ds);
    }

    public boolean registerUser(PluginUser user) {
        try {
            getSession().insert(Entity.create("user").parseBean(user, true, false));
        } catch (SQLException e) {
            Main.logger.error("SQL Exception {}", e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public PluginUser getUser(String kookId) {
        try {
            Entity entity = Entity.create("main.user")
                    .set("kook_id", kookId);
            Entity result = db.get(entity);
            if (result == null) {
                return null;
            }
            return result.toBean(PluginUser.class);
        } catch (SQLException e) {
            Main.logger.error("SQL Exception {}", e.getLocalizedMessage());
            return null;
        }
    }

    private Session getSession() {
        return Session.create(ds);
    }

    public boolean isExist(String kookId) {
        boolean res = false;
        try {
            res = db.count(Entity.create(tableName).set("kook_id", kookId)) == 1;
        } catch (SQLException e) {
            Main.logger.error("SQL Exception", e);
        }
        return res;
    }

    public boolean updateAccount(Account account){
        PluginUser pluginUser = selectById(account.getKookId());
        pluginUser.setOsuId(account.getOsuId());
        pluginUser.setServerId(account.getServerId());
        return updateById(pluginUser)==1;
    }


}

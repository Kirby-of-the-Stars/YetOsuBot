package cn.day.kbcplugin.osubot.dao;

import cn.day.kbcplugin.osubot.db.BaseDao;
import cn.day.kbcplugin.osubot.pojo.Account;
import cn.hutool.db.Entity;

import javax.sql.DataSource;
import java.util.List;

public class AccountDao extends BaseDao<Account> {
    public AccountDao(DataSource ds) {
        super(ds);
    }

    public boolean isBound(int osu_id){
        Account account = selectById(osu_id);
        return account!=null;
    }

    public List<Account> getUserAccounts(int kook_id){
        Entity where = Entity.create(tableName).set("kook_id", kook_id);
        return select(where);
    }

    public boolean bindAccount(Account account){
        return insert(account)==1;
    }

}

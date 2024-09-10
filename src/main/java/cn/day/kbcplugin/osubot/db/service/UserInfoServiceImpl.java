package cn.day.kbcplugin.osubot.db.service;

import cn.day.kbcplugin.osubot.db.dao.UserInfoMapper;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.entity.UserInfo;
import com.mybatisflex.core.query.QueryChain;

import java.util.List;

public class UserInfoServiceImpl {

    private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    public UserInfo queryOsuUserInfo(String kookId, ServerEnum server) {
        return QueryChain.of(userInfoMapper)
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getKookId).eq(kookId)
                .and(UserInfo::getServer).eq(server)
                .one();
    }

    public List<UserInfo> queryAllProfiles(String kookId) {
        return QueryChain.of(userInfoMapper)
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getKookId).eq(kookId)
                .list();
    }

    public UserInfoMapper getMapper() {
        return userInfoMapper;
    }

}

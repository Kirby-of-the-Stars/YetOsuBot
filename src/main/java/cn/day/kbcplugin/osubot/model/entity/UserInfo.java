package cn.day.kbcplugin.osubot.model.entity;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import cn.day.kbcplugin.osubot.model.api.base.IUserInfo;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("user_infos")
public class UserInfo {
    @Id(keyType = KeyType.None)
    private String osuId;
    @Id(keyType = KeyType.None)
    private ServerEnum server;
    @Id(keyType = KeyType.None)
    private OsuModeEnum mode;
    private String kookId;
    private String userName;
    private LocalDate updateTime;

    public UserInfo(IUserInfo userInfo, String kookId, ServerEnum server, OsuModeEnum mode) {
        this.osuId = userInfo.getUserId();
        this.userName = userInfo.getUserName();
        this.kookId = kookId;
        this.server = server;
        this.updateTime = LocalDate.now();
        this.mode = mode;
    }
}

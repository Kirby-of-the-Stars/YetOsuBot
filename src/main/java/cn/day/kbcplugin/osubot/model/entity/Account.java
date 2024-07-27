package cn.day.kbcplugin.osubot.model.entity;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import cn.day.kbcplugin.osubot.enums.ServerEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("accounts")
public class Account {
    @Id(keyType = KeyType.None)
    private String kookId;
    private OsuModeEnum preferredMode;
    private ServerEnum preferredServer;

    //TODO other userInfo?
}

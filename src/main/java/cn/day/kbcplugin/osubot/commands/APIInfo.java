package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.model.card.APIInfoCard;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.LogFactory;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

/**
 * 查看当前信息
 */
@Command(name = "api")
@Prefix("/")
@Description("查看API信息,用法:/api")
public class APIInfo {

    private static final Log logger = LogFactory.getLog("[API INFO Command]");

    @Execute
    public void info(@Context Message message) {
        try {
            message.reply(APIInfoCard.build());
        } catch (Exception e) {
            logger.warn("kook又卡消息了:{}", e.getLocalizedMessage(), e);
            message.reply("KOOK跟机器人爆了,发不了消息");
        }
    }
}

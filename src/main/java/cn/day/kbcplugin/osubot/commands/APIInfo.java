package cn.day.kbcplugin.osubot.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

/**
 * 查看当前信息
 */
@Command(name = "api")
@Prefix("/")
@Description("查看API信息")
public class APIInfo {
    @Execute
    public void info(@Context Message message){

    }
}

package cn.day.kbcplugin.osubot.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.kookbc.impl.command.litecommands.annotations.prefix.Prefix;

@Command(name = "profile",aliases = "pf")
@Prefix("/")
public class Profile {

    @Execute
    public void infoMe(@Context CommandSender commandSender, @Context Message message) {
        if (commandSender instanceof User sender) {
            //TODO profile
        }
    }
}

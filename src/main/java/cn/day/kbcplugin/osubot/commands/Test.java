package cn.day.kbcplugin.osubot.commands;

import cn.day.kbcplugin.osubot.Main;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class Test implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        Main.logger.info("test");
    }
}

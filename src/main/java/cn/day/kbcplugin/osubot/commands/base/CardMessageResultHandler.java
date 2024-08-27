package cn.day.kbcplugin.osubot.commands.base;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import snw.jkook.command.CommandSender;
import snw.jkook.command.ConsoleCommandSender;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.card.MultipleCardComponent;

public class CardMessageResultHandler implements ResultHandler<CommandSender, MultipleCardComponent> {
    @Override
    public void handle(Invocation<CommandSender> invocation, MultipleCardComponent result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Message message = invocation.context().get(Message.class).orElse(null);
        if (sender instanceof User) {
            if (message != null) {
                message.reply(result);
            }
        } else if (sender instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) sender).getLogger().info("The execution result of command {}: {}", invocation.name(), result);
        } else {
            throw new IllegalStateException("Unknown sender type: " + sender.getClass().getName());
        }
    }
}

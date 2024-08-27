package cn.day.kbcplugin.osubot.commands.base;

import cn.day.kbcplugin.osubot.enums.ServerEnum;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import snw.jkook.command.CommandSender;

public class ServerArgumentResolver extends ArgumentResolver<CommandSender, ServerEnum> {
    @Override
    protected ParseResult<ServerEnum> parse(Invocation<CommandSender> invocation, Argument<ServerEnum> argument, String s) {
        ServerEnum server = ServerEnum.fromName(s);
        if(server == null) {
            return ParseResult.failure("请输入正确的服务器类型");
        }
        return ParseResult.success(server);
    }
    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<ServerEnum> argument, SuggestionContext context) {
        return SuggestionResult.of(ServerEnum.toNameList());
    }
}

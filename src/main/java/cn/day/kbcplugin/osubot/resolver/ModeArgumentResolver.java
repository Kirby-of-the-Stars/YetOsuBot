package cn.day.kbcplugin.osubot.resolver;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import snw.jkook.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ModeArgumentResolver extends ArgumentResolver<CommandSender, OsuModeEnum> {

    @Override
    protected ParseResult<OsuModeEnum> parse(Invocation<CommandSender> invocation, Argument<OsuModeEnum> argument, String s) {
        OsuModeEnum mode = OsuModeEnum.fromName(s);
        if(mode == null) {
            return ParseResult.failure("错误的模式");
        }
        return ParseResult.success(mode);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<OsuModeEnum> argument, SuggestionContext context) {
        List<String> list = new ArrayList<>(OsuModeEnum.toNameList());
        list.add("可使用的模式: ");
        return SuggestionResult.of(list);
    }
}

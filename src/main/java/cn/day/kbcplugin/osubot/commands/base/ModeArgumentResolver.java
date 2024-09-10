package cn.day.kbcplugin.osubot.commands.base;

import cn.day.kbcplugin.osubot.enums.OsuModeEnum;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.dromara.hutool.core.text.StrUtil;
import snw.jkook.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ModeArgumentResolver extends ArgumentResolver<CommandSender, OsuModeEnum> {

    @Override
    protected ParseResult<OsuModeEnum> parse(Invocation<CommandSender> invocation, Argument<OsuModeEnum> argument, String s) {
        OsuModeEnum mode;
        if (StrUtil.isNumeric(s)) {
            try {
                mode = OsuModeEnum.get(Integer.parseInt(s));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return ParseResult.failure("请输入正确的模式");
            }
            return ParseResult.success(mode);
        }
        mode = OsuModeEnum.fromName(s);
        if (mode == null) {
            return ParseResult.failure("请输入正确的模式");
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

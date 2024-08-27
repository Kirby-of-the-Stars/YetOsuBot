package cn.day.kbcplugin.osubot.commands.base;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import snw.jkook.command.CommandSender;

public class IntegerParser implements Parser<CommandSender, Integer> {
    @Override
    public ParseResult<Integer> parse(Invocation<CommandSender> invocation, Argument<Integer> argument, RawInput rawInput) {
        try {
            int integer = Integer.parseInt(rawInput.next());
            if (integer < 0) {
                return ParseResult.failure("请输入大于0的数字");
            }
            return ParseResult.success(integer);
        } catch (NumberFormatException e) {
            return ParseResult.failure("请输入正确的数字");
        }
    }

    @Override
    public Range getRange(Argument<Integer> integerArgument) {
        return Range.ONE;
    }
}

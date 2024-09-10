package cn.day.kbcplugin.osubot.commands.base;

import cn.day.kbcplugin.osubot.db.dao.AccountMapper;
import cn.day.kbcplugin.osubot.model.entity.Account;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import snw.jkook.command.CommandSender;
import snw.jkook.entity.User;

public class AccountContextProvider implements ContextProvider<CommandSender, Account> {

    private final AccountMapper accountMapper;

    public AccountContextProvider(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public ContextResult<Account> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof User sender) {
            String kookId = sender.getId();
            if (kookId == null) {
                return ContextResult.error("测试: 无法查询到kook账号信息");
            }
            Account account = accountMapper.selectOneById(kookId);
            if (account == null) {
                return ContextResult.error("当前尚未绑定任何账号,请先使用 /bind 绑定");
            }
            return ContextResult.ok(() -> account);
        } else {
            return ContextResult.error("只有用户才能执行该命令");
        }
    }
}

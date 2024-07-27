package cn.day.kbcplugin.osubot.commands;

import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class AccountList implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
//        if(message==null) return;
//        PluginUser pluginUser = Main.userDao.getUser(sender.getId());
//        if(pluginUser==null){
//            message.reply("请先进行绑定");
//            return;
//        }
//        List<Account> accounts = Main.accountDao.getUserAccounts(Integer.parseInt(sender.getId()));
//        Account targetAccount = null;
//        int size = (Math.min(accounts.size(), 5));
//        if(arguments.length > 0){
//            String _num = (String) arguments[0];
//            if(!_num.startsWith("#")){
//                message.reply("请指定正确的编号,例如#1");
//                return;
//            }
//            int num = Integer.parseInt(_num.substring(1));
//            if(num>size){
//                message.reply("超出范围");
//                return;
//            }
//            targetAccount = accounts.get(num-1);
//            if(!Main.userDao.updateAccount(targetAccount)){
//                message.reply("设置默认账号失败");
//                return;
//            }
//        }
//        //TODO get account avatar
//        CardBuilder builder = new CardBuilder();
//        try {
//            for (int i = 0; i < size; i++) {
//                builder.setTheme(Theme.INFO);
//                builder.setSize(Size.LG);
//                List<BaseElement> colums = new ArrayList<>(3);
//                Account account = accounts.get(i);
//                String nameText = String.format("**%s**\n%s","用户名",account.getUserName());
//                if(targetAccount!=null){
//                    if(targetAccount.getOsuId()==account.getOsuId()){
//                        nameText = String.format("**%s**\n%s","用户名(默认用户)",account.getUserName());
//                    }
//                }else {
//                    if(pluginUser.getOsuId()==account.getOsuId()){
//                        nameText = String.format("**%s**\n%s","用户名(默认用户)",account.getUserName());
//                    }
//                }
//                MarkdownElement name = new MarkdownElement(nameText);
//                colums.add(name);
//                MarkdownElement id = new MarkdownElement(
//                        String.format("**%s**\n%s","osu_id",account.getOsuId())
//                );colums.add(id);
//                MarkdownElement server = new MarkdownElement(
//                        String.format("**%s**\n%s","绑定的服务器",account.getServerName())
//                );colums.add(server);
//                builder.addModule(new SectionModule(new Paragraph(3,colums)));
//                if(i!=(size)-1){
//                    builder.newCard();
//                }
//            }
//        }catch (Exception e){
//            message.reply("插件内部错误:"+e.getLocalizedMessage());
//            Main.logger.error("AccountList出错:",e);
//            return;
//        }
//        message.reply(builder.build());
    }
}

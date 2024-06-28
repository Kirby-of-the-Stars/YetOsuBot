package cn.day.kbcplugin.osubot.commands;

import snw.jkook.command.JKookCommand;
import snw.jkook.plugin.Plugin;

public class CommandInit {

    public static void registerCommand(Plugin plugin){
        //注册bind
        new JKookCommand("bind","/")
                .setDescription("绑定一个osu用户到当前kook用户上。\n 用法: /bind osuid [server] [mode],server可选，目前支持bancho和ppysb，其他则默认bancho,模式默认std")
                .executesUser(new BindUser())
                .register(plugin);
        //注册bpme
        new JKookCommand("bpme","/")
                .setDescription("打印一个自己的bp。 \n 用法: /bpme #number")
                .executesUser(new Bpme())
                .register(plugin);
        //注册recent
        new JKookCommand("recent","/")
                .setDescription("打印一个自己的最近24h内的成就。 \n 用法: /recent [server] [mode] ,指定服务器 和模式，单参数的时候默认认为是模式")
                .addAlias("pr")
                .executesUser(new getRecent())
                .register(plugin);
        //测试输出
//        new JKookCommand("test","/")
//                .setDescription("测试输出")
//                .executesUser(new Test())
//                .register(plugin);
//
        new JKookCommand("myosu","/")
                .setDescription("查看所有被绑定的账号,用法 /myosu 或 /osu #[num] num:表示设置其为默认账号")
                .addAlias("osu")
                .executesUser(new AccountList())
                .register(plugin);
    }
}

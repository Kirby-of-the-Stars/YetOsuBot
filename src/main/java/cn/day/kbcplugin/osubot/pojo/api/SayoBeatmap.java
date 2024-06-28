package cn.day.kbcplugin.osubot.pojo.api;

public class SayoBeatmap {
    public int sid;
    public float AR;
    public float CS;
    public float HP;
    public float OD;
    public float aim;
    public String audio;
    public String bg;
    public int bid;
    public int circles;
    //在这个时间内（ms）可以打出300分数
    public int hit300window;
    @Deprecated
    //背景图的MD5(已废弃)
    public String img;

    public int length;
    public int maxcombo;
    public int mode;
    public int passcount;
    public int playcount;
    public float pp;
    public float pp_acc;
    public float pp_aim;
    public float pp_speed;
    public int sliders;
    //speed难度
    public float speed;
    public int spinners;
    public float star;
    //aim难度曲线
    public String strain_aim;
    //speed难度曲线
    public String strain_speed;
    //难度名
    public String version;
    public String artist;
    public String title;
    public String creator;
    
}

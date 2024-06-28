package cn.day.kbcplugin.osubot.pojo;

import cn.day.kbcplugin.osubot.annotion.TableField;
import cn.day.kbcplugin.osubot.annotion.TableId;
import cn.day.kbcplugin.osubot.annotion.TableName;
import cn.day.kbcplugin.osubot.pojo.common.AbstractBeatmap;
import cn.hutool.core.util.StrUtil;

@TableName("beatmaps")
public class SayoDBMap implements AbstractBeatmap {
    public String artist;
    public String artistU;
    public String title;
    public String titleU;
    //难度名
    public String version;
    public int bpm;
    public String creator;
    public int creator_id;
    public int sid;
    @TableId("bid")
    public int bid;
    public double AR;
    public double CS;
    public double HP;
    public double OD;
    public double aim;
    public String audio;
    public String bg;
    public int circles;
    //在这个时间内（ms）可以打出300分数
    public int hit300window;
    @TableField(exist = false)
    @Deprecated
    //背景图的MD5(已废弃)
    public String img;

    public int length;
    public int maxcombo;
    public int mode;
    public int playcount;
    public double pp;
    public double pp_acc;
    public double pp_aim;
    public double pp_speed;
    public int sliders;
    //speed难度
    public float speed;
    public int spinners;
    public double star;
    //aim难度曲线
    public String strain_aim;
    //speed难度曲线
    public String strain_speed;

    @Override
    public Integer getSid() {
        return this.sid;
    }

    @Override
    public Integer getBid() {
        return this.bid;
    }

    @Override
    public String getArtist() {
        return StrUtil.isBlankIfStr(this.artistU)?this.artist:this.artistU;
    }

    @Override
    public String getTitle() {
        return StrUtil.isBlankIfStr(this.titleU)?this.title:this.titleU;
    }

    @Override
    public Integer getMaxCombo() {
        return this.maxcombo;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getCreator() {
        return this.creator;
    }

    @Override
    public String getBgName() {
        return this.bg;
    }
}

package cn.day.kbcplugin.osubot.model.api;

import com.sun.jna.Structure;
import lombok.Getter;
import lombok.Setter;

@Getter
@Structure.FieldOrder({"pp","pp_acc","pp_aim","pp_speed","pp_fc","max_pp","map_star","debug_text"})
public class PPResult extends Structure {
    public double pp;
    /// The accuracy portion of the final pp.
    public double pp_acc;
    /// The aim portion of the final pp.
    public double pp_aim;
    /// The speed portion of the final pp.
    public double pp_speed;
    /// pp if fc
    public double pp_fc;
    /// Max pp
    public double max_pp;
    /// map star
    public double map_star;

    public String debug_text;

    @Setter
    private OsuMap rawMap;

    public PPResult() {

    }
    public PPResult(double pp, double pp_acc, double pp_aim, double pp_speed,double pp_fc ,double max_pp, double map_star, String debug_text) {
        this.pp = pp;
        this.pp_acc = pp_acc;
        this.pp_aim = pp_aim;
        this.pp_speed = pp_speed;
        this.pp_fc = pp_fc;
        this.max_pp = max_pp;
        this.map_star = map_star;
        this.debug_text = debug_text;
    }

    @Override
    public String toString() {
        return "PPResult{" +
                "pp=" + pp +
                ", pp_acc=" + pp_acc +
                ", pp_aim=" + pp_aim +
                ", pp_speed=" + pp_speed +
                ", pp_fc=" + pp_fc +
                ", max_pp=" + max_pp +
                ", map_star=" + map_star +
                ", debug_text='" + debug_text + '\'' +
                '}';
    }
}

package cn.day.kbcplugin.osubot.pojo.osu;

public class STDMods {
    /*
     *     const NF: u32 = 1u32;
     *     const EZ: u32 = 2u32;
     *     const TD: u32 = 4u32;
     *     const HD: u32 = 8u32;
     *     const HR: u32 = 16u32;
     *     const DT: u32 = 64u32;
     *     const RX: u32 = 128u32;
     *     const HT: u32 = 256u32;
     *     const FL: u32 = 1_024u32;
     *     const SO: u32 = 4_096u32;
     */
    private long result = 0;
    public long ModsChain() {
        if (result < 0) {
            throw new IllegalArgumentException("mods should bigger then 0");
        } else {
            return result;
        }
    }

    private STDMods(){}

    public static STDMods Builder(){
        return new STDMods();
    }

    public STDMods NF(){
        this.result+=1;
        return this;
    }
    public STDMods EZ(){
        this.result+=2;
        return this;
    }
    public STDMods TD(){
        this.result+=4;
        return this;
    }
    public STDMods HD(){
        this.result+=8;
        return this;
    }
    public STDMods HR(){
        this.result+=16;
        return this;
    }
    public STDMods DT(){
        this.result+=64;
        return this;
    }
    public STDMods RX(){
        this.result+=128;
        return this;
    }
    public STDMods HT(){
        this.result+=256;
        return this;
    }
    public STDMods FL(){
        this.result+=1024;
        return this;
    }
    public STDMods SO(){
        this.result+=4096;
        return this;
    }

    public static long None(){
        return 0;
    }

    public STDMods auto(Byte mode){
        result+=mode.longValue();
        return this;
    }
    public STDMods auto(long mode){
        result+=mode;
        return this;
    }

}

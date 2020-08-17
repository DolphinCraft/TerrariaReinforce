package pw.illusion.reinforce.config;

import cc.sfclub.util.common.JsonConfig;
import pw.illusion.reinforce.Reinforce;

import java.util.ArrayList;
import java.util.List;

public class Config extends JsonConfig {
    public static Config inst;
    public String loreHeader = "H=Reinforce==";
    public String loreFooter = "F=Reinforce==";
    public boolean enableFail = false;
    public String ecoExpr = "";
    public List<Modifier> modifiers = new ArrayList<>();
    public Lang lang = new Lang();

    public static class Lang {
        public String loreModTitle = "Modifier: %s";
    }

    public Config() {
        super(Reinforce.getPlugin(Reinforce.class).getDataFolder().toString());
    }
}

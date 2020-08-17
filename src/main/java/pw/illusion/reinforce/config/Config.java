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
    public String ecoExpr = "%mat+%ench+%rand";
    public int randMin = 100;
    public boolean firstRun = true;
    public int randMax = 1000;
    public List<Modifier> modifiers = new ArrayList<>();
    public List<Price> materialOffsets = new ArrayList<>();
    public List<Price> enchantmentOffsets = new ArrayList<>();
    public Lang lang = new Lang();

    public static class Lang {
        public String loreModTitle = "Modifier: %s";
        public String ensure_with_price = "&aAre you sure?You will spend %s coins on your item: &b%s";
        public String unrecognized_item = "&aWe don't support this item.";
        public String succeed = "&aSucceed! Your Modifier is %s";
        public String money_not_enough = "&cYou don't have enough money to do it :(";
        public String dont_move_your_sword_away = "&4&lHey!&r&lCHEATER,DON'T MOVE YOUR ORIGINALLY ITEM AWAY!";
        public String failed = "&cFAILED!Nothing happened but your money.&eTry again?";
    }

    public Config() {
        super(Reinforce.getPlugin(Reinforce.class).getDataFolder().toString());
    }
}

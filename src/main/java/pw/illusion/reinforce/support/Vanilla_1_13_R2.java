package pw.illusion.reinforce.support;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pw.illusion.reinforce.api.PriceJudge;
import pw.illusion.reinforce.api.TypeJudge;
import pw.illusion.reinforce.util.ArmorType;

public class Vanilla_1_13_R2 implements TypeJudge, PriceJudge {
    @Override
    public ArmorType typeOf(ItemStack itemStack) {
        Material mat = itemStack.getType();
        if (mat.name().endsWith("CHESTPLATE")
                || mat.name().endsWith("HELMET")
                || mat.name().endsWith("BOOTS")
                || mat.name().endsWith("LEGGINGS")) {
            return ArmorType.ARMOR;
        }
        if (mat == Material.BOW) {
            return ArmorType.RANGED;
        }
        if (Material.getMaterial("TRIDENT") != null && mat == Material.TRIDENT) { //1.13 support
            return ArmorType.TRIDENT;
        }
        if (mat.name().endsWith("SWORD") || mat.name().endsWith("AXE")) {
            return ArmorType.MELEE;
        }
        return ArmorType.UNRECOGNIZED;
    }

    @Override
    public double priceOf(ItemStack itemStack) {
        return 0;
    }

    @Override
    public String name() {
        return "1.13.2 and below";
    }
}

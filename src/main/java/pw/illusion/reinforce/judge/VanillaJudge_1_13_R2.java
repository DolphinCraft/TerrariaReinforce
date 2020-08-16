package pw.illusion.reinforce.judge;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pw.illusion.reinforce.api.TypeJudge;
import pw.illusion.reinforce.util.ArmorType;

public class VanillaJudge_1_13_R2 implements TypeJudge {
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
        if (Material.getMaterial("TRIDENT") != null) { //1.13 support
            return ArmorType.RANGED;
        }
        if (mat.name().endsWith("SWORD") || mat.name().endsWith("AXE")) {
            return ArmorType.MELEE;
        }
        return ArmorType.UNRECOGNIZED;
    }

    @Override
    public String name() {
        return "1.13.2 and below";
    }
}

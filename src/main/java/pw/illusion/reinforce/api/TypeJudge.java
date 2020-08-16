package pw.illusion.reinforce.api;

import org.bukkit.inventory.ItemStack;
import pw.illusion.reinforce.util.ArmorType;

public interface TypeJudge {
    ArmorType typeOf(ItemStack itemStack);

    String name();
}

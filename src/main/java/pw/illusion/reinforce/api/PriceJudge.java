package pw.illusion.reinforce.api;

import org.bukkit.inventory.ItemStack;

public interface PriceJudge {
    double priceOf(ItemStack itemStack);

    String name();
}

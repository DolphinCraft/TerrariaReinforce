package pw.illusion.reinforce;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.illusion.reinforce.config.Config;

import java.util.ArrayList;

public class DiscountCard {
    private static final String ITEM_IDENTIFIER = ChatColor.GRAY + "trf_discountcard";

    public static ItemStack allocate() {
        Material type;
        ItemStack itemStack = new ItemStack(Material.END_CRYSTAL);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(Config.inst.lang.DiscountCardItemName);
        ArrayList<String> list = new ArrayList<>();
        list.add(Config.inst.lang.DiscountCardItemLore);
        list.add(ITEM_IDENTIFIER);
        im.setLore(list);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public static boolean isDiscountCard(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            return itemStack.getItemMeta().getLore().contains(ITEM_IDENTIFIER);
        }
        return false;
    }

    public static boolean canPlayerDiscount(Player player) {
        for (ItemStack itemStack : player.getInventory()) {
            if (isDiscountCard(itemStack)) {
                return true;
            }
        }
        return false;
    }
}

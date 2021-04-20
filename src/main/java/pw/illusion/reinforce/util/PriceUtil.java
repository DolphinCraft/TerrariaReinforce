package pw.illusion.reinforce.util;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.illusion.reinforce.Reinforce;
import pw.illusion.reinforce.config.Config;
import pw.illusion.reinforce.config.Modifier;
import pw.illusion.reinforce.config.Price;

import java.util.*;

public class PriceUtil {
    private static final Random rng = new Random();

    @SneakyThrows
    public static double calcPrice(ItemStack item, boolean discount) {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("TerrariaReinforce")) return 0.00;
        double baseValueEnch = 0D;
        double baseValueOrig = 0D;
        double baseValueMat = 0D;
        int rn = 0;
        if (Config.inst.ecoExpr.contains("%mat")) { //meaningless but im boring XD
            for (Price materialOffset : Config.inst.materialOffsets) {
                if (item.getType().name().matches(materialOffset.regex)) {
                    baseValueMat = materialOffset.price + baseValueMat;
                    if (Config.inst.judgeOnceForMaterial) {
                        break;
                    }
                }
            }
        }
        if (Config.inst.ecoExpr.contains("%ench")) {
            for (Price enchOffset : Config.inst.enchantmentOffsets) {
                for (String s : asStrList(item.getEnchantments())) {
                    if (s.matches(enchOffset.regex)) {
                        baseValueEnch = enchOffset.price + baseValueEnch;
                    }
                }
            }
        }
        if (Config.inst.ecoExpr.contains("%rand")) {
            rn = rng.nextInt(Config.inst.randMax) + Config.inst.randMin;
        }
        if (Config.inst.ecoExpr.contains("%orig")) {
            Modifier modifier = modifierOf(item);
            if (modifier != null) baseValueOrig = modifier.price + baseValueOrig;
        }
        return (double) Reinforce.getInst().getScriptEngine().eval(
                Config.inst.ecoExpr
                        .replaceAll("%rand", rn + "")
                        .replaceAll("%mat", baseValueMat + "")
                        .replaceAll("%ench", baseValueEnch + "")
                        .replaceAll("%orig", baseValueOrig + "")
        ) * (discount ? Config.inst.discountPercent : 1);
    }

    private static Modifier modifierOf(ItemStack itemStack) {
        if (!Reinforce.getInst().isValidItem(itemStack)) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        String l_modName = null;
        //Item has lore because Reinforce.isValidItem .
        Iterator<String> iter = itemMeta.getLore().iterator();
        while (iter.hasNext()) {
            String el = iter.next();
            if (el.equals(Config.inst.loreHeader)) {
                //the next is modifier: XX
                l_modName = iter.next();
                l_modName = l_modName.replaceAll(Config.inst.lang.loreModTitle, "");
                break;
            }
        }
        if (l_modName == null) {
            return null;
        }
        for (Modifier modifier : Config.inst.modifiers) {
            if (modifier.displayName.equals(l_modName)) {
                return modifier;
            }
        }
        return null;
    }

    private static List<String> asStrList(Map<Enchantment, Integer> enchs) {
        List<String> l = new ArrayList<>();
        enchs.forEach((k, v) -> l.add(k.getKey().getKey() + " " + v));
        return l;
    }
}

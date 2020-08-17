package pw.illusion.reinforce.util;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import pw.illusion.reinforce.Reinforce;
import pw.illusion.reinforce.config.Config;
import pw.illusion.reinforce.config.Price;

import java.util.Random;

public class PriceUtil {
    private static final Random rng = new Random();

    @SneakyThrows
    public static double calcPrice(ItemStack item) {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("TerrariaReinforce")) return 0.00;
        double baseValueEnch = 0D;
        double baseValueMat = 0D;
        int rn = 0;
        if (Config.inst.ecoExpr.contains("%mat")) { //meaningless but im boring XD
            for (Price materialOffset : Config.inst.materialOffsets) {
                if (item.getType().name().matches(materialOffset.regex)) {
                    baseValueMat = materialOffset.price + baseValueMat;
                    break;
                }
            }
        }
        if (Config.inst.ecoExpr.contains("%ench")) {
            for (Price enchOffset : Config.inst.enchantmentOffsets) {
                if (item.getType().name().matches(enchOffset.regex)) {
                    baseValueEnch = enchOffset.price + baseValueEnch;
                    break;
                }
            }
        }
        if (Config.inst.ecoExpr.contains("%rand")) {
            rn = rng.nextInt(Config.inst.randMax) + Config.inst.randMin;
        }
        return (double) Reinforce.getInst().getScriptEngine().eval(
                Config.inst.ecoExpr
                        .replaceAll("%rand", rn + "")
                        .replaceAll("%mat", baseValueMat + "")
                        .replaceAll("%ench", baseValueEnch + "")
        );
    }
}

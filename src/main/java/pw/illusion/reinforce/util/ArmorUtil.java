package pw.illusion.reinforce.util;

import org.bukkit.inventory.ItemStack;
import pw.illusion.reinforce.Log;
import pw.illusion.reinforce.Reinforce;
import pw.illusion.reinforce.api.TypeJudge;

public class ArmorUtil {
    public static ArmorType typeOf(ItemStack itemStack) {
        for (TypeJudge typeJudge : Reinforce.getInst().typeJudgeList) {
            ArmorType type = typeJudge.typeOf(itemStack);
            if (type != ArmorType.UNRECOGNIZED) {
                Log.debug("Item: " + itemStack.toString());
                Log.debug("Judge: " + typeJudge.name());
                Log.debug("Result: " + type);
                return type;
            }
        }
        return ArmorType.UNRECOGNIZED;
    }
}

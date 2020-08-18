package pw.illusion.reinforce;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class Log {
    private static final String FORMAT = ChatColor.GREEN.toString() + ChatColor.BOLD + "Terraria" + ChatColor.AQUA + "Reinforce " + ChatColor.RESET + ChatColor.WHITE + ">> %";

    public static void info(String mesg) {
        Bukkit.getServer().getConsoleSender().sendMessage(FORMAT.replaceAll("%", mesg));
    }

    public static void warn(String mesg) {
        Bukkit.getServer().getConsoleSender().sendMessage(FORMAT.replaceAll("%", ChatColor.RED + mesg));

    }

    public static void debug(String mesg) {
        if (!Reinforce.debug) return;
        Bukkit.getServer().getConsoleSender().sendMessage(FORMAT.replaceAll("%", "[D] " + ChatColor.UNDERLINE + mesg.replaceAll(org.bukkit.ChatColor.COLOR_CHAR + "", "&")));
    }
}

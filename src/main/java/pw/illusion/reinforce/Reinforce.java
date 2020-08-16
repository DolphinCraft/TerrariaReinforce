package pw.illusion.reinforce;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pw.illusion.reinforce.api.TypeJudge;
import pw.illusion.reinforce.config.Config;
import pw.illusion.reinforce.judge.VanillaJudge_1_13_R2;

import java.util.ArrayList;
import java.util.List;

public final class Reinforce extends JavaPlugin {
    public static boolean debug = false;
    public List<TypeJudge> typeJudgeList = new ArrayList<>();

    public static Reinforce getInst() {
        return Reinforce.getPlugin(Reinforce.class);
    }

    @Override
    public void onEnable() {
        Log.info("Initializing Configuration..");
        Config.inst = (Config) new Config().saveDefaultOrLoad();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadDefaultJudges() {
        if (getServer().getVersion().contains("1.13")) {
            registerTypeJudge(new VanillaJudge_1_13_R2());
        }
        if (typeJudgeList.isEmpty()) {
            Log.warn("No TypeJudge Registered!!");
            Log.warn("THIS MAY MEANS YOUR MINECRAFT VERSION IS UNSUPPORTED! (" + getServer().getBukkitVersion() + ")");
        }
    }

    public void registerTypeJudge(TypeJudge judge) {
        typeJudgeList.add(judge);
        Log.info("[!] New TypeJudge Registered: " + ChatColor.AQUA + judge.name());
    }
}

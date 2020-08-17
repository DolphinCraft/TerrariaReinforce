package pw.illusion.reinforce;

import cc.sfclub.util.bukkit.inv.InvBird;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import pw.illusion.reinforce.api.TypeJudge;
import pw.illusion.reinforce.config.Config;
import pw.illusion.reinforce.config.Modifier;
import pw.illusion.reinforce.judge.VanillaJudge_1_13_R2;
import pw.illusion.reinforce.util.ArmorType;
import pw.illusion.reinforce.util.ArmorUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public final class Reinforce extends JavaPlugin {
    public static boolean debug = false;
    public List<TypeJudge> typeJudgeList = new ArrayList<>();
    private static final Gson gson = new Gson();
    private static Random random;
    @Getter
    private ScriptEngine scriptEngine;
    private InvBird invBird;

    public static Reinforce getInst() {
        return Reinforce.getPlugin(Reinforce.class);
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        random = new Random();//tried to keep rng refresh...

        Log.info("Initializing Configuration..");
        File modsDir = new File(getDataFolder(), "mods"); //scan singleton modifier.json
        if (!getDataFolder().mkdir() || !modsDir.mkdir()) {
            Log.warn("CANNOT CREATE FOLDER!! PLEASE CHECK PERMISSION!!");
            return;
        }
        Config.inst = (Config) new Config().saveDefaultOrLoad();
        /* Checking Configuration*/
        if (Config.inst.loreHeader == null
                || Config.inst.loreHeader.isEmpty()
                || Config.inst.loreFooter == null
                || Config.inst.loreFooter.isEmpty()) {
            Log.warn("LoreHeader or LoreFooter can't be null!");
            this.setEnabled(false);
            return;
        }
        if (Config.inst.loreFooter.equals(Config.inst.loreHeader)) {
            Log.warn("LoreHeader is equals to LoreFooter");
            this.setEnabled(false);
            return;
        }
        for (File file : modsDir.listFiles(file -> file.getName().endsWith(".json"))) {
            Log.debug("Loading File: " + file.getName());
            Modifier mod = gson.fromJson(new BufferedReader(new FileReader(file)), Modifier.class);
            if (Modifier.isValid(mod)) {
                Log.info("Loading " + ChatColor.AQUA + mod.displayName + " (from " + file.getName() + ")");
            }
        }
        /*Formatting colors*/
        Config.inst.loreFooter = ChatColor.translateAlternateColorCodes('&', Config.inst.loreFooter);
        Config.inst.loreHeader = ChatColor.translateAlternateColorCodes('&', Config.inst.loreHeader);
        Config.inst.modifiers.forEach(e -> {
            e.displayName = ChatColor.translateAlternateColorCodes('&', e.displayName);
            e.lores.forEach(s -> s = ChatColor.translateAlternateColorCodes('&', s));
        });
        Log.info("Initializing ScriptEngine..");
        ScriptEngineManager scm = new ScriptEngineManager();
        scriptEngine = scm.getEngineByName("JavaScript");
        Log.info("Initializing Judges..");
        loadDefaultJudges();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Clear item modifiers
     *
     * @param item item to be clean
     * @return cleaned item
     */
    public ItemStack resetModifier(ItemStack item) {
        if (!isValidItem(item)) return item;
        ItemMeta itemMeta = item.getItemMeta();
        if (!itemMeta.hasDisplayName()) return item;
        //clear display name
        for (Modifier mod : Config.inst.modifiers) {
            if (itemMeta.getDisplayName().startsWith(mod.displayName)) {
                itemMeta.setDisplayName(itemMeta.getDisplayName().replaceAll(mod.displayName + " ", ""));
                break;
            }
        }
        //clear lore
        if (itemMeta.hasLore()) {
            Iterator<String> iter = itemMeta.getLore().iterator();
            boolean flag = false;
            while (iter.hasNext()) {
                String ele = iter.next();
                if (ele.equals(Config.inst.loreHeader)) {
                    flag = true;
                }
                if (flag) iter.remove();
                if (ele.equals(Config.inst.loreFooter)) break;
            }

        }
        ItemStack itemStack = item.clone();
        itemStack.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Give buff to item
     * Do nothing(return original item) when selectRandomModifier return null or unknown armortype.
     *
     * @param item items that ready to be modified
     * @return modified item or original item.
     */
    @SuppressWarnings("unused")
    public ItemStack randModifier(ItemStack item) {
        if (ArmorUtil.typeOf(item) == ArmorType.UNRECOGNIZED) return item;
        ItemStack result = item;
        Optional<Modifier> mod = selectRandomModifier(item);
        if (mod.isPresent()) { //sorry but i have to use this or use atomic for ifPresent.....
            result = applyModifier(resetModifier(item), mod.get());
        }
        return result;
    }

    /**
     * apply mod to item
     * it doesn't clear modifier for item.
     *
     * @param itemStack item
     * @return modded item
     */
    public ItemStack applyModifier(ItemStack itemStack, Modifier mod) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> newLores = new ArrayList<>();
        newLores.add(Config.inst.loreHeader);
        newLores.add(Config.inst.lang.loreModTitle.replaceAll("%s", mod.displayName));
        newLores.addAll(mod.lores);
        newLores.add(Config.inst.loreFooter);
        if (itemMeta.hasLore()) {
            itemMeta.getLore().addAll(newLores);
        } else {
            itemMeta.setLore(newLores);
        }
        ItemStack item = itemStack.clone();
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Roll a random modifier.
     *
     * @return modifier
     */
    private Optional<Modifier> selectRandomModifier(ItemStack item) {
        for (Modifier modifier : Config.inst.modifiers) {
            if (modifier.armorType != ArmorUtil.typeOf(item)) continue;
            int rn = random.nextInt(100) + 1; //rn (0~99)+1 == 1~100
            if (modifier.probability < rn) {
                //hit
                return Optional.of(modifier);
            }
        }
        //didn't hit any modifier
        if (Config.inst.enableFail) {
            return Optional.empty();
        }
        int counter = 0;
        while (true) {
            counter++;
            int rn = random.nextInt(Config.inst.modifiers.size());
            Modifier mod = Config.inst.modifiers.get(rn);
            if (counter > 20) {
                Log.warn("RandomModifier.Encourage executed 21 times!!");
                Log.warn("OMG WHY THIS PLAYER SO UNLUCKY?");
                Log.warn("OR YOUR CONFIGURATION HAVE NOTHING COMPENSATABLE,UNKNOWN ARMORTYPE?");
                Log.warn("RETURN " + mod.displayName);
            }
            if (mod.armorType != ArmorUtil.typeOf(item)) continue;
            if (mod.compensatable) {
                return Optional.of(mod);
            }
        }
    }

    /**
     * Check if item modified by TR
     *
     * @return isModified
     */
    public boolean isValidItem(ItemStack itemStack) {
        return (itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().contains(Config.inst.loreHeader) && itemStack.getItemMeta().getLore().contains(Config.inst.loreFooter));
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

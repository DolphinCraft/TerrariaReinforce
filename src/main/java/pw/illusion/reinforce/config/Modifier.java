package pw.illusion.reinforce.config;

import pw.illusion.reinforce.util.ArmorType;

import java.util.ArrayList;
import java.util.List;

public class Modifier {
    public String displayName;
    public double probability;
    public ArmorType armorType;
    public List<String> lores = new ArrayList<>();
}

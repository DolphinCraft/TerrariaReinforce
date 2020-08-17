package pw.illusion.reinforce.hook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;

@AllArgsConstructor
public class VaultHook {
    @Getter
    private final Economy econ;
}

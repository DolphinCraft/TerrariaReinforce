package pw.illusion.reinforce;

public class VaultNotFoundE extends Exception {
    @Override
    public String getMessage() {
        return "Vault is missing";
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[]{
                new StackTraceElement("https://github.com/illusMC/TerrariaReinforce/wiki/FAQ#vaultnotfounde-vault-is-missing", "", "Where you can seek for help", 0)
        };
    }
}

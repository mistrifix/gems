package us.outlast.gems;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.outlast.gems.command.AdminCommand;
import us.outlast.gems.command.CommonCommand;
import us.outlast.gems.data.PluginConfiguration;
import us.outlast.gems.listener.AuthListener;

public class Outlast extends JavaPlugin {

    private static Outlast instance;

    public void onLoad() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("gems").setExecutor(new CommonCommand());
        this.getCommand("agems").setExecutor(new AdminCommand());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AuthListener(), this);

        PluginConfiguration.getInstance().loadConfiguration();

    }

    @Override
    public void onDisable() {
    }

    public static Outlast getInstance() {
        return (instance != null ? instance : new Outlast());
    }
}

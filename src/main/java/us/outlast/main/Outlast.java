package us.outlast.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.outlast.main.command.AdminCommand;
import us.outlast.main.command.CommonCommand;
import us.outlast.main.data.PluginConfiguration;
import us.outlast.main.listener.AuthListener;

public final class Outlast extends JavaPlugin {

    private static Outlast instance;

    public void onLoad() {
        instance = this;
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
    }

    @Override
    public void onEnable() {
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

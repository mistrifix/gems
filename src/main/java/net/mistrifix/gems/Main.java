package net.mistrifix.gems;

import net.mistrifix.gems.model.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.mistrifix.gems.command.AdminCommand;
import net.mistrifix.gems.command.CommonCommand;
import net.mistrifix.gems.data.PluginConfiguration;
import net.mistrifix.gems.listener.AuthListener;

public class Main extends JavaPlugin {

    private static Main instance;

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
        UserManager.getUsers().forEach(UserManager::insert);
    }

    public static Main getInstance() {
        return instance;
    }
}

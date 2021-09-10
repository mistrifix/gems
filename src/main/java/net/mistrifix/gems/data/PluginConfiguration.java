package net.mistrifix.gems.data;

import org.bukkit.configuration.file.FileConfiguration;
import net.mistrifix.gems.Main;

public class PluginConfiguration {

    private static PluginConfiguration instance;

    public int startingGems;

    public PluginConfiguration() {
        instance = this;
    }

    public void loadConfiguration() {
        FileConfiguration config = Main.getInstance().getConfig();
        startingGems = config.getInt("startingGems");
    }

    public static PluginConfiguration getInstance() {
        return (instance != null ? instance : new PluginConfiguration());
    }
}

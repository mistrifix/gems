package us.outlast.gems.data;

import org.bukkit.configuration.file.FileConfiguration;
import us.outlast.gems.Outlast;

public class PluginConfiguration {

    private static PluginConfiguration instance;

    public int startingGems;

    public PluginConfiguration() {
        instance = this;
    }

    public void loadConfiguration() {
        FileConfiguration config = Outlast.getInstance().getConfig();
        startingGems = config.getInt("startingGems");
    }

    public static PluginConfiguration getInstance() {
        return (instance != null ? instance : new PluginConfiguration());
    }
}

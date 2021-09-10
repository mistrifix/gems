package net.mistrifix.gems.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.mistrifix.gems.model.UserManager;

public class AuthListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        UserManager.getUser(event.getPlayer().getUniqueId()).ifPresent(user -> UserManager.load(user.getUniqueId()));
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        UserManager.getUser(event.getPlayer().getUniqueId()).ifPresent(user -> {
            if(user.wasChanged()) UserManager.insert(user);
        });
    }
}

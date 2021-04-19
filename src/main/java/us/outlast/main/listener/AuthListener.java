package us.outlast.main.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.outlast.main.model.UserManager;

public class AuthListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        UserManager.getUser(event.getPlayer().getUniqueId()).ifPresent(user -> UserManager.load(user.getUniqueId()));
    }

    public void onQuit(final PlayerQuitEvent event) {
        UserManager.getUser(event.getPlayer().getUniqueId()).ifPresent(user -> {
            if(user.wasChanged()) UserManager.insert(user);
        });
    }
}

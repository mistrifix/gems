package us.outlast.gems.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.outlast.gems.model.UserManager;

public class CommonCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player player = (Player) sender;
        UserManager.getUser(player.getUniqueId()).ifPresent(user -> user.sendMessage("&7Your balance: &3" + user.getBalance().intValue() + " gems&7."));
        return true;
    }
}

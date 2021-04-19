package us.outlast.gems.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.outlast.gems.model.User;
import us.outlast.gems.model.UserManager;
import us.outlast.gems.util.ChatUtils;

import java.math.BigInteger;
import java.util.Objects;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can perform this command");
            return false;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("outlast.gems.admin")) {
            player.sendMessage(ChatUtils.colored("&cYou cannot execute this command."));
            return false;
        }
        UserManager.getUser(player.getUniqueId()).ifPresent(user -> {
            if(args.length == 0) {
                user.sendMessage("&c/agems set <player> <amount>");
                user.sendMessage("&c/agems give <player> <amount>");
                user.sendMessage("&c/agems take <player> <amount>");
                user.sendMessage("&c/agems <player>");
                user.sendMessage("&c/agems forcesave &7- saves player data forcefully");
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("forcesave")) {
                    long now = System.currentTimeMillis();
                    for(User allUsers : UserManager.getUsers()) {
                        try {

                            UserManager.insert(allUsers);
                            user.sendMessage("&7Successfully saved data of &2" + UserManager.getUsers().size() + (UserManager.getUsers().size() > 1 ? " users" : " user") + " &7(&e" + (System.currentTimeMillis() - now) + "ms&7).");
                            return;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            user.sendMessage("&cReloading attempt failed. Check the console logs to get more info.");
                        }
                    }
                }
            }
            if(args.length == 1) {
                if(Bukkit.getPlayer(args[0]) == null || !Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline() || !Objects.requireNonNull(Bukkit.getPlayer(args[0])).getName().equalsIgnoreCase(args[0])) {
                    user.sendMessage("&6" + args[0] + " &cnot found.");
                    return;
                }
                if(!player.hasPermission("outlast.gems.viewothers")) {
                    player.sendMessage(ChatUtils.colored("&cYou cannot execute this command."));
                    return;
                }
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                UserManager.getUser(targetPlayer.getUniqueId()).ifPresent(targetUser ->
                        user.sendMessage("&e" + targetPlayer.getName() + "'s &7balance: &3" + targetUser.getBalance().intValue() + " gems&7."));
            }
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("set")) {
                    if(Bukkit.getPlayer(args[1]) == null || !Objects.requireNonNull(Bukkit.getPlayer(args[1])).isOnline() || !Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName().equalsIgnoreCase(args[1])) {
                        user.sendMessage("&6" + args[1] + " &cnot found.");
                        return;
                    }
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    UserManager.getUser(targetPlayer.getUniqueId()).ifPresent(targetUser -> {
                        if (isInt(args[2])) {
                            targetUser.setFunds(new BigInteger(args[2]));
                            UserManager.insert(targetUser);

                            user.sendMessage("&7You changed &2" + targetPlayer.getName() + "'s &7balance to &e" + targetUser.getBalance().intValue() + " gems&7.");
                            targetUser.sendMessage("&7Your new balance: &3" + targetUser.getBalance().intValue() + " gems&7.");
                        } else {
                            user.sendMessage("&eInvalid argument.");
                        }
                    });
                }
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {
                    if(Bukkit.getPlayer(args[1]) == null || !Objects.requireNonNull(Bukkit.getPlayer(args[1])).isOnline() || !Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName().equalsIgnoreCase(args[1])) {
                        user.sendMessage("&6" + args[1] + " &cnot found.");
                        return;
                    }
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    UserManager.getUser(targetPlayer.getUniqueId()).ifPresent(targetUser -> {
                        if (isInt(args[2])) {
                            targetUser.addFunds(new BigInteger(args[2]));
                            UserManager.insert(targetUser);

                            user.sendMessage("&7You changed &2" + targetPlayer.getName() + "'s &7balance to &e" + targetUser.getBalance().intValue() + " gems&7.");
                            targetUser.sendMessage("&7Your new balance: &3" + targetUser.getBalance().intValue() + " gems&7.");
                        } else {
                            user.sendMessage("&eInvalid argument.");
                        }
                    });
                }
                if(args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                    if(Bukkit.getPlayer(args[1]) == null || !Objects.requireNonNull(Bukkit.getPlayer(args[1])).isOnline() || !Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName().equalsIgnoreCase(args[1])) {
                        user.sendMessage("&6" + args[1] + " &cnot found.");
                        return;
                    }
                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    UserManager.getUser(targetPlayer.getUniqueId()).ifPresent(targetUser -> {
                        if (isInt(args[2])) {
                            if(targetUser.getBalance().intValue() - Integer.parseInt(args[2]) < 0) {
                                user.sendMessage("&cUser's balance cannot be negative.");
                                return;
                            }
                            targetUser.subtractFunds(new BigInteger(args[2]));
                            UserManager.insert(targetUser);

                            user.sendMessage("&7You changed &3" + targetPlayer.getName() + "'s &7balance to &e" + targetUser.getBalance().intValue() + " gems&7.");
                            targetUser.sendMessage("&7Your new balance: &3" + targetUser.getBalance().intValue() + " gems&7.");
                        } else {
                            user.sendMessage("&eInvalid argument.");
                        }
                    });
                }
            }
        });
        return false;
    }

    private boolean isInt(String param) {
        try {
            Integer.valueOf(param);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}

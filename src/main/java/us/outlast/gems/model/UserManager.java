package us.outlast.gems.model;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import us.outlast.gems.Outlast;

import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UserManager {

    private static final Map<UUID, User> cache = new HashMap<>();

    public static void addUser(final @NotNull User user) {
        cache.putIfAbsent(user.getUniqueId(), user);
    }

    public static void removeUser(final @NotNull User user) {
        cache.remove(user.getUniqueId(), user);
    }

    public static Set<User> getUsers() {
        return new HashSet<>(cache.values());
    }

    public static Optional<User> getUser(final @NotNull UUID uuid) {
        Optional<User> user = Optional.ofNullable(cache.get(uuid));
        if (user.isPresent()) {
            return user;
        }
        final User newUser = new User(uuid);
        addUser(newUser);
        insert(newUser);

        return Optional.ofNullable(cache.get(uuid));
    }

    public static void insert(final @NotNull User user) {
        File datafile = new File(Outlast.getInstance().getDataFolder() + File.separator + "users" + File.separator + user.getUniqueId() + ".yml");
        if(datafile.isDirectory()) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(datafile);
        config.set("uuid", user.getUniqueId().toString());
        config.set("gems", user.getBalance().intValue());
        try {
            config.save(datafile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static @NotNull CompletableFuture<User> select(final @NotNull UUID uuid) {
        final CompletableFuture<User> future = new CompletableFuture<>();
        File datafile = new File(Outlast.getInstance().getDataFolder() + File.separator + "users" + File.separator + uuid.toString() + ".yml");
        if(datafile.isDirectory()) {
            future.complete(null);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(datafile);

        final User newUser = new User(UUID.fromString(Objects.requireNonNull(config.getString("uuid"))));
        newUser.setFunds(BigInteger.valueOf(config.getInt("gems")));

        future.complete(newUser);
        return future;
    }

    public static void load(@NotNull final UUID uuid) {
        select(uuid).thenCompose(user -> {
            if (user != null) {
                cache.put(uuid, user);
                return CompletableFuture.completedFuture(null);
            } else {
                User newUser = new User(uuid);
                cache.put(uuid, newUser);
                insert(newUser);
            }
            return null;
        });
    }
}

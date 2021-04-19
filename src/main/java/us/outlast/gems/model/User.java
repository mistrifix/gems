package us.outlast.gems.model;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import us.outlast.gems.data.PluginConfiguration;
import us.outlast.gems.util.ChatUtils;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final @NotNull UUID uuid;
    private BigInteger balance;

    private boolean changed = false;

    public User(@NotNull UUID uuid) {
        this.uuid = uuid;
        if(PluginConfiguration.getInstance().startingGems > 0) {
            this.balance = BigInteger.valueOf(PluginConfiguration.getInstance().startingGems);
        }
        this.balance = BigInteger.ZERO;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public BigInteger getBalance() {
        return this.balance;
    }

    public boolean canAfford(BigInteger cost) {
        if (cost.signum() <= 0) {
            return false;
        }
        final BigInteger remainingFunds = getBalance().subtract(cost);
        return remainingFunds.signum() >= 0;
    }


    public void executePayment(BigInteger cost) {
        if (canAfford(cost)) {
            subtractFunds(cost);
            markChanged();
        }
    }

    public void addFunds(BigInteger funds) {
        if (funds.signum() < 0) {
            return;
        }
        this.balance = getBalance().add(funds);
        markChanged();
    }

    public void setFunds(final BigInteger funds) {
        if (funds.signum() >= 0) {
            this.balance = funds;
            markChanged();
        }
    }

    public void subtractFunds(BigInteger funds) {
        if (funds.signum() <= 0) {
            return;
        }
        this.balance = getBalance().subtract(funds);
        markChanged();
    }

    public void sendMessage(final String message) {
        Objects.requireNonNull(Bukkit.getPlayer(this.uuid)).sendMessage(ChatUtils.colored(message));
    }

    public void markChanged() {
        this.changed = true;
    }

    public boolean wasChanged() {
        boolean changedState = this.changed;

        if (changedState) {
            this.changed = false;
        }
        return changedState;
    }
}
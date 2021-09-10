package net.mistrifix.gems.api;

import org.jetbrains.annotations.NotNull;
import net.mistrifix.gems.model.User;
import net.mistrifix.gems.model.UserManager;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

public class GemsAPI {

    public static Integer getBalance(final @NotNull UUID uuid) {
        Optional<User> userOptional = UserManager.getUser(uuid);
        return userOptional.map(user -> user.getBalance().intValue()).orElse(0);
    }

    public static void execute(final @NotNull UUID uuid, final Integer funds, final ActionType type) {
        UserManager.getUser(uuid).ifPresent(user -> {
            if(funds < 0) return;
            switch (type) {
                case BALANCE_SET:
                    user.addFunds(BigInteger.valueOf(funds));
                    break;
                case BALANCE_SUBTRACT:
                    user.subtractFunds(BigInteger.valueOf(funds));
                    break;
                case BALANCE_ADD:
                    user.setFunds(BigInteger.valueOf(funds));
                    break;
                case USER_PAY:
                    user.executePayment(BigInteger.valueOf(funds));
                    break;
                default:
                    break;
            }
            UserManager.insert(user);
        });
    }
}

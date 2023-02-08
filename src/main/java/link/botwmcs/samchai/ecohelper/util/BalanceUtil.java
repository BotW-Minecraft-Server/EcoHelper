package link.botwmcs.samchai.ecohelper.util;

import committee.nova.lighteco.capabilities.impl.Account;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class BalanceUtil {
    public static BigDecimal getBalance(Player player) {
        AtomicReference<BigDecimal> balance = new AtomicReference<>(new BigDecimal("0"));
        player.getCapability(Account.ACCOUNT).ifPresent(a -> {
            balance.set(a.getBalance());
        });
        return balance.get();
    }

    public static void setBalance(Player player, BigDecimal value) {
        player.getCapability(Account.ACCOUNT).ifPresent(a -> {
            int balance = value.intValue();
            if (balance <= EcoHelperConfig.CONFIG.min_balance.get()) {
                balance = EcoHelperConfig.CONFIG.min_balance.get();
            } else if (balance >= EcoHelperConfig.CONFIG.max_balance.get()) {
                balance = EcoHelperConfig.CONFIG.max_balance.get();
            }
            a.setBalance(new BigDecimal(balance));
        });
    }

    public static void addBalance(Player player, BigDecimal value) {
        player.getCapability(Account.ACCOUNT).ifPresent(a -> {
            int balance = a.getBalance().add(value).intValue();
            if (balance <= EcoHelperConfig.CONFIG.min_balance.get()) {
                balance = EcoHelperConfig.CONFIG.min_balance.get();
            } else if (balance >= EcoHelperConfig.CONFIG.max_balance.get()) {
                balance = EcoHelperConfig.CONFIG.max_balance.get();
            }
            a.setBalance(new BigDecimal(balance));
        });
    }

    public static void removeBalance(Player player, BigDecimal value) {
        player.getCapability(Account.ACCOUNT).ifPresent(a -> {
            int balance = a.getBalance().subtract(value).intValue();
            if (balance <= EcoHelperConfig.CONFIG.min_balance.get()) {
                balance = EcoHelperConfig.CONFIG.min_balance.get();
            } else if (balance >= EcoHelperConfig.CONFIG.max_balance.get()) {
                balance = EcoHelperConfig.CONFIG.max_balance.get();
            }
            a.setBalance(new BigDecimal(balance));
        });
    }

    public static boolean transferBalance(Player from, Player to, BigDecimal value) {
        if (getBalance(from).compareTo(value) < 0) {
            return false;
        }
        if (getBalance(to).add(value).intValue() >= EcoHelperConfig.CONFIG.max_balance.get()) {
            int overheadBalance = getBalance(to).add(value).intValue() - EcoHelperConfig.CONFIG.max_balance.get();
            removeBalance(from, value.subtract(new BigDecimal(overheadBalance)));
            setBalance(to, new BigDecimal(EcoHelperConfig.CONFIG.max_balance.get()));
        }
        removeBalance(from, value);
        addBalance(to, value);
        return true;
    }
}

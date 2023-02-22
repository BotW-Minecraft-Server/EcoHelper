package link.botwmcs.samchai.ecohelper.impl;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.impl.cmi.SqlExecutor;
import link.botwmcs.samchai.ecohelper.impl.essentials.YamlReader;
import net.minecraft.server.level.ServerPlayer;

public class BukkitImpl {
    public static void setCmiBalanceToEH(ServerPlayer player) {
        SqlExecutor.syncCmiBalance(player);
    }

    public static void setEssBalanceToEH(ServerPlayer player) {
        YamlReader reader = new YamlReader(player);
        String balance = reader.getValueByKey("money", "0");
        EcoHelper.LOGGER.info("Balance: " + balance);
    }
}

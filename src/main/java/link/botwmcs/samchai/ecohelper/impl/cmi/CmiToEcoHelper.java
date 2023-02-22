package link.botwmcs.samchai.ecohelper.impl.cmi;

import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import net.minecraft.server.level.ServerPlayer;

public class CmiToEcoHelper {
    public static boolean setPlayerBalance(ServerPlayer player, double balance) {
        EcoUtils.EcoActionResult result = BalanceUtil.setBalance(player, balance);
        return result != EcoUtils.EcoActionResult.SUCCESS;
    }
}

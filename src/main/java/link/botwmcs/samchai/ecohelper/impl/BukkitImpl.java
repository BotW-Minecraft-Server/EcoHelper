package link.botwmcs.samchai.ecohelper.impl;

import link.botwmcs.samchai.ecohelper.util.CommandUtil;
import net.minecraft.server.level.ServerPlayer;

public class BukkitImpl {
    public static String getPlayerBukkitEco(ServerPlayer serverPlayer, String command) {
        return CommandUtil.runCommandOnPlayerPos(serverPlayer.getLevel(), serverPlayer, command);
    }
}

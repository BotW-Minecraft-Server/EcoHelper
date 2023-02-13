package link.botwmcs.samchai.ecohelper.util;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Set;

public class PlayerUtil {
    public static boolean isJoiningWorldForTheFirstTime(Player player, boolean mustHaveEmptyInventory) {
        String firstjointag = EcoHelper.MODID + ".firstJoin";

        Set<String> tags = player.getTags();
        if (tags.contains(firstjointag)) {
            return false;
        }

        player.addTag(firstjointag);

        if (mustHaveEmptyInventory) {
            Inventory inv = player.getInventory();
            boolean isempty = true;
            for (int i = 0; i < 36; i++) {
                if (!inv.getItem(i).isEmpty()) {
                    isempty = false;
                    break;
                }
            }

            if (!isempty) {
                return false;
            }
        }

        Level world = player.getCommandSenderWorld();
        ServerLevel ServerLevel = (ServerLevel)world;
        BlockPos wspos = ServerLevel.getSharedSpawnPos();
        BlockPos ppos = player.blockPosition();
        BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());

        return cpos.closerThan(wspos, 50);
    }

    public static boolean isJoiningWorldForTheFirstTime(Player player) {
        return isJoiningWorldForTheFirstTime(player, false);
    }
}

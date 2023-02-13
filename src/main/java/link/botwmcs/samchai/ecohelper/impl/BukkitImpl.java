package link.botwmcs.samchai.ecohelper.impl;

import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.util.CommandUtil;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.CSS;
import java.util.Objects;

public class BukkitImpl {
    public static String getPlayerBukkitEco(ServerPlayer serverPlayer, String command) {
        return CommandUtil.runCommandOnPlayerPos(serverPlayer.getLevel(), serverPlayer, command);
    }
}

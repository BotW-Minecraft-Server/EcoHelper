package link.botwmcs.samchai.ecohelper.util;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class CommandUtil {
    // Thanks https://github.com/zly2006 for this code
    // FUCK BUKKIT, I CANNOT CATCH THE COMMAND OUTPUT
    // TODO: Will try develop a bukkit plugin for this bullshit.
    public static String runCommand(ServerLevel serverLevel, Vec3 vec3, String command) {
        StringBuilder output = new StringBuilder();
        CommandSource source = new CommandSource() {
            @Override
            public void sendMessage(Component pComponent, @NotNull UUID pSenderUUID) {
                output.append(pComponent.getString());
            }

            @Override
            public boolean acceptsSuccess() {
                return true;
            }

            @Override
            public boolean acceptsFailure() {
                return true;
            }

            @Override
            public boolean shouldInformAdmins() {
                return false;
            }
        };
        serverLevel.getServer().getCommands().performCommand(
            new CommandSourceStack(
                source,
                vec3,
                Vec2.ZERO,
                serverLevel,
                2,
                "dev",
                Component.nullToEmpty("dev"),
                serverLevel.getServer(),
                null
            ),
            command
        );
        return output.toString();
    }

    public static String runCommand(ServerLevel serverLevel, BlockPos nearPos, String command) {
        return runCommand(serverLevel, Vec3.atBottomCenterOf(nearPos), command);
    }

    public static String runCommandOnPlayerPos(ServerLevel serverLevel, ServerPlayer serverPlayer, String command) {
        BlockPos playerPos = serverPlayer.getOnPos();
        return runCommand(serverLevel, playerPos, command);
    }
}

package link.botwmcs.samchai.ecohelper.util;

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
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CommandUtil {
    public static String rawCommandBlock(ServerLevel serverLevel, Vec3 vec3, String command) {
        BaseCommandBlock commandBlock = new BaseCommandBlock() {
            @Override
            public @NotNull ServerLevel getLevel() {
                return serverLevel;
            }

            @Override
            public void onUpdated() {

            }

            @Override
            public @NotNull Vec3 getPosition() {
                return vec3;
            }

            @Override
            public @NotNull CommandSourceStack createCommandSourceStack() {
                return new CommandSourceStack(this, vec3, Vec2.ZERO, serverLevel, 2, "CommandBlock", Component.nullToEmpty("CommandBlock"), serverLevel.getServer(), null);
            }

            public void setCommand(@NotNull String command) {
                super.setCommand(command);
            }


        };
        commandBlock.setCommand(command);
        commandBlock.performCommand(serverLevel);
        return commandBlock.getLastOutput().getString();
    }


//    public static String getRawCommandOutput(ServerLevel serverLevel, @Nullable Vec3 vec, String command) {
//        BaseCommandBlock bcb = new BaseCommandBlock() {
//            @Override
//            public @NotNull ServerLevel getLevel() {
//                return serverLevel;
//            }
//
//            @Override
//            public void onUpdated() { }
//
//            @Override
//            public @NotNull Vec3 getPosition() {
//                return Objects.requireNonNullElseGet(vec, () -> new Vec3(0, 0, 0));
//            }
//
//            @Override
//            public @NotNull CommandSourceStack createCommandSourceStack() {
//                return new CommandSourceStack(this, getPosition(), Vec2.ZERO, serverLevel, 2, "dev", Component.nullToEmpty("dev"), serverLevel.getServer(), null);
//            }
//
//            @Override
//            public boolean performCommand(Level pLevel) {
//                if (!pLevel.isClientSide) {
//                    if ("Searge".equalsIgnoreCase(this.getCommand())) {
//                        this.setLastOutput(Component.nullToEmpty("#itzlipofutzli"));
//                        this.setSuccessCount(1);
//                    } else {
//                        this.setSuccessCount(0);
//                        MinecraftServer minecraftserver = this.getLevel().getServer();
//                        if (!StringUtil.isNullOrEmpty(this.getCommand())) {
//                            this.setLastOutput(null);
//                            CommandSourceStack commandsourcestack = this.createCommandSourceStack().withCallback((p_45417_, p_45418_, p_45419_) -> {
//                                if (p_45418_) {
//                                    this.setSuccessCount(this.getSuccessCount()+1);
//                                }
//
//                            });
//                            minecraftserver.getCommands().performCommand(commandsourcestack, this.getCommand());
////                            try {
////                                this.setLastOutput(null);
////                                CommandSourceStack commandsourcestack = this.createCommandSourceStack().withCallback((p_45417_, p_45418_, p_45419_) -> {
////                                    if (p_45418_) {
////                                        this.setSuccessCount(this.getSuccessCount()+1);
////                                    }
////
////                                });
////                                minecraftserver.getCommands().performCommand(commandsourcestack, this.getCommand());
////                            } catch (Throwable throwable) {
////                                CrashReport crashreport = CrashReport.forThrowable(throwable, "Executing command block");
////                                CrashReportCategory crashreportcategory = crashreport.addCategory("Command to be executed");
////                                crashreportcategory.setDetail("Command", this::getCommand);
////                                crashreportcategory.setDetail("Name", () -> this.getName().getString());
////                                throw new ReportedException(crashreport);
////                            }
//                        }
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        };
//
//        bcb.setCommand(command);
//        bcb.setTrackOutput(true);
//        bcb.performCommand(serverLevel);
//
//        return bcb.getLastOutput().getString();
//    }
//
    public static String runCommand(ServerLevel serverLevel, BlockPos nearPos, String command) {
        return rawCommandBlock(serverLevel, Vec3.atBottomCenterOf(nearPos), command);
    }

    public static String runCommandOnPlayerPos(ServerLevel serverLevel, ServerPlayer serverPlayer, String command) {
        BlockPos playerPos = serverPlayer.getOnPos();
        return runCommand(serverLevel, playerPos, command);
    }
}
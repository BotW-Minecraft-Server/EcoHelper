package link.botwmcs.samchai.ecohelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class EcoCommand {

    public EcoCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Register a boolean argument to check that is a player or not
        Predicate<CommandSourceStack> isPlayer = commandSourceStack -> {
            try {
                commandSourceStack.getPlayerOrException();
                return true;
            } catch (CommandSyntaxException e) {
                return false;
            }
        };
        // Check if config is enabled
        Predicate<CommandSourceStack> configToggle = commandSourceStack -> {
            if (EcoHelperConfig.CONFIG.enabled.get()) {
                return true;
            } else {
                return false;
            }
        };
        // Check if config GUI is enabled
        Predicate<CommandSourceStack> configToggleGUI = commandSourceStack -> {
            if (EcoHelperConfig.CONFIG.enable_gui.get()) {
                return true;
            } else {
                return false;
            }
        };

        LiteralCommandNode<CommandSourceStack> register = dispatcher.register(Commands.literal("eco")
                        .requires(isPlayer)
                        .requires(configToggle)
                        .then(Commands.literal("admin")
                                .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.literal("get")
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                    double balance = BalanceUtil.getBalance(player);
                                                    context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.get", player.getName(), balance), true);
                                                    return 1;
                                                })
                                        )
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("balance", DoubleArgumentType.doubleArg())
                                                        .executes(context -> {
                                                            ServerPlayer serverPlayer = EntityArgument.getPlayer(context, "target");
                                                            double balance = DoubleArgumentType.getDouble(context, "balance");
                                                            EcoUtils.EcoActionResult result = BalanceUtil.setBalance(serverPlayer, balance);
                                                            if (result != EcoUtils.EcoActionResult.SUCCESS) {
                                                                context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.set.fail", serverPlayer.getName(), result.toString()));
                                                            } else {
                                                                context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.set.success", serverPlayer.getName(), balance), true);

                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("balance", DoubleArgumentType.doubleArg(0))
                                                        .executes(context -> {
                                                            ServerPlayer serverPlayer = EntityArgument.getPlayer(context, "target");
                                                            double balance = DoubleArgumentType.getDouble(context, "balance");
                                                            EcoUtils.EcoActionResult result = BalanceUtil.addBalance(serverPlayer, balance);
                                                            if (result != EcoUtils.EcoActionResult.SUCCESS) {
                                                                context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.add.fail", serverPlayer.getName(), result.toString()));
                                                            } else {
                                                                context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.add.success", serverPlayer.getName(), balance), true);
                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("balance", DoubleArgumentType.doubleArg(0))
                                                        .executes(context -> {
                                                            ServerPlayer serverPlayer = EntityArgument.getPlayer(context, "target");
                                                            double balance = DoubleArgumentType.getDouble(context, "balance");
                                                            EcoUtils.EcoActionResult result = BalanceUtil.removeBalance(serverPlayer, balance);
                                                            if (result != EcoUtils.EcoActionResult.SUCCESS) {
                                                                context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.remove.fail", serverPlayer.getName(), result.toString()));
                                                            } else {
                                                                context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.remove.success", serverPlayer.getName(), balance), true);
                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("exchange")
                                .requires(configToggleGUI.negate()))
                        .then(Commands.literal("pay")
                                .requires(configToggleGUI.negate())
                                .then(Commands.argument("target", EntityArgument.player())
                                                .then(Commands.argument("balance", DoubleArgumentType.doubleArg(0))
                                                                .executes(context -> {
                                                                    ServerPlayer from = context.getSource().getPlayerOrException();
                                                                    ServerPlayer to = EntityArgument.getPlayer(context, "target");
                                                                    double transfer = DoubleArgumentType.getDouble(context, "balance");
                                                                    double balance = BalanceUtil.getBalance(from);
                                                                    EcoUtils.EcoActionResult result = BalanceUtil.transferBalance(from, to, transfer);
                                                                    if (result != EcoUtils.EcoActionResult.SUCCESS) {
                                                                        context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.pay.fail", result.toString()));
                                                                    } else {
                                                                        context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.pay.success", to.getName(), transfer, balance), true);
                                                                    }
                                                                    return 1;
                                                                })
                                                )
                                ))
                        .then(Commands.literal("open")
                                .requires(configToggleGUI)
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    return 1;
                                })
                        )
        );
    }
}

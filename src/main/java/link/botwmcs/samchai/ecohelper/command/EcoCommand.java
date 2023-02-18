package link.botwmcs.samchai.ecohelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

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
        double minBalance = EcoHelperConfig.CONFIG.min_balance.get();
        double maxBalance = EcoHelperConfig.CONFIG.max_balance.get();

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
                                                .then(Commands.argument("balance", DoubleArgumentType.doubleArg(minBalance, maxBalance))
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
                                .requires(configToggleGUI.negate())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            int amount = IntegerArgumentType.getInteger(context, "amount");
                                            boolean result = BalanceUtil.exchangeToBalance(player, amount);
                                            if (result) {
                                                context.getSource().sendSuccess(Component.nullToEmpty("yes"), true);
                                            } else {
                                                context.getSource().sendFailure(Component.nullToEmpty("no"));
                                            }

                                            return 1;
                                        })
                                )
                                .then(Commands.literal("all")
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            boolean result = BalanceUtil.exchangeToBalance(player);
                                            if (result) {
                                                context.getSource().sendSuccess(Component.nullToEmpty("yes"), true);
                                            } else {
                                                context.getSource().sendFailure(Component.nullToEmpty("no"));
                                            }

                                            return 1;
                                        })
                                )
                        )
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
                                )
                        )
                        .then(Commands.literal("worth")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    ItemStack itemOnHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                                    double worth = BalanceUtil.getTradableItemWorth(player.getLevel(), itemOnHand);
                                    if (worth == 0) {
                                        context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.worth.fail"));
                                    } else {
                                        context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.worth.success", itemOnHand.getHoverName(), worth), true);
                                    }
                                    return 1;
                                })
                        )
                        .then(Commands.literal("wallet")
                                .requires(configToggleGUI)
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    return 1;
                                })
                        )

        );
    }
}

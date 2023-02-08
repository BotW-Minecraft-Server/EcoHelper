package link.botwmcs.samchai.ecohelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import committee.nova.lighteco.capabilities.impl.Account;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.math.BigDecimal;
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

        dispatcher.register(Commands.literal("eco")
                .requires(isPlayer)
                .requires(configToggle)
                        .then(Commands.literal("admin")
                                .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.literal("get")
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                    BigDecimal balance = BalanceUtil.getBalance(player);
                                                    context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.get", player.getName(), balance), true);
                                                    return 1;
                                                })
                                        )
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("balance", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                            BigDecimal balance = new BigDecimal(IntegerArgumentType.getInteger(context, "balance"));
                                                            BalanceUtil.setBalance(player, balance);
                                                            context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.set", player.getName(), balance), true);
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("balance", IntegerArgumentType.integer(0))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                            BigDecimal balance = new BigDecimal(IntegerArgumentType.getInteger(context, "balance"));
                                                            BalanceUtil.addBalance(player, balance);
                                                            context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.add", player.getName(), balance), true);
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("balance", IntegerArgumentType.integer(0))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                            BigDecimal balance = new BigDecimal(IntegerArgumentType.getInteger(context, "balance"));
                                                            BalanceUtil.removeBalance(player, balance);
                                                            context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.remove", player.getName(), balance), true);
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("gui")
                                .requires(configToggleGUI))
                        .then(Commands.literal("exchange"))
                        .then(Commands.literal("pay")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("balance", IntegerArgumentType.integer(0))
                                                .executes(context -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(context, "target");
                                                    ServerPlayer self = context.getSource().getPlayerOrException();
                                                    BigDecimal balance = new BigDecimal(IntegerArgumentType.getInteger(context, "balance"));
                                                    BigDecimal selfBalance = BalanceUtil.getBalance(self);
                                                    if (target == self) {
                                                        context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.pay.fail.self"));
                                                        return 1;
                                                    }
                                                    if (balance.intValue() == 0) {
                                                        context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.pay.fail.zero"));
                                                        return 1;
                                                    }
                                                    // Execute transfer method
                                                    boolean result = BalanceUtil.transferBalance(context.getSource().getPlayerOrException(), target, balance);
                                                    if (!result) {
                                                        context.getSource().sendFailure(new TranslatableComponent("commands.ecohelper.pay.fail", target.getName()));
                                                    } else {
                                                        context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.pay.success", target.getName(), balance, selfBalance), true);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )

                        .executes(context -> {
                            if (EcoHelperConfig.CONFIG.enable_gui.get()) {
                                ServerPlayer player = context.getSource().getPlayerOrException();
//                                player.openMenu();
                            } else {
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                BigDecimal balance = BalanceUtil.getBalance(player);
                                context.getSource().sendSuccess(new TranslatableComponent("commands.ecohelper.get.myself", balance), true);
                            }
                            return 1;

                        })
        );
    }
}

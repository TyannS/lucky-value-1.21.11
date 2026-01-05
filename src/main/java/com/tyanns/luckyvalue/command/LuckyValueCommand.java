package com.tyanns.luckyvalue.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.tyanns.luckyvalue.network.LuckyValueS2CPayload;
import com.tyanns.luckyvalue.server.LuckyValueManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class LuckyValueCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> literalCommandNode = dispatcher.register(CommandManager.literal("lucky").
                requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
                .then(
                        CommandManager.literal("set")
                                .then(
                                        CommandManager.argument("target", EntityArgumentType.players())
                                                .then(
                                                        CommandManager.argument("value", IntegerArgumentType.integer(-100, 100))
                                                                .executes(
                                                                        context -> {
                                                                            final Collection<? extends ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "target");
                                                                            final int value = IntegerArgumentType.getInteger(context, "value");
                                                                            for (ServerPlayerEntity serverPlayerEntity : targets) {
                                                                                LuckyValueManager.setLuckyValue(serverPlayerEntity.getUuid(), value);
                                                                                LuckyValueS2CPayload payload = new LuckyValueS2CPayload(LuckyValueManager.getLuckyValue(serverPlayerEntity.getUuid()));
                                                                                ServerPlayNetworking.send(serverPlayerEntity, payload);
                                                                            }
                                                                            context.getSource().sendFeedback(() -> Text.literal("已将%s名玩家的幸运值设置为%s".formatted(targets.size(), value)), false);
                                                                            return targets.size();
                                                                        })
                                                )
                                )
                ).then(
                        CommandManager.literal("get")
                                .then(
                                        CommandManager.argument("target", EntityArgumentType.players())
                                                .executes(
                                                        context -> {
                                                            final Collection<? extends ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "target");
                                                            StringBuilder str = new StringBuilder("幸运值查询如下:\n");
                                                            for (ServerPlayerEntity serverPlayerEntity : targets) {
                                                                str.append(serverPlayerEntity.getGameProfile().name()).append(": ").append(LuckyValueManager.getLuckyValue(serverPlayerEntity.getUuid())).append("\n");
                                                            }
                                                            str.deleteCharAt(str.length() - 1);
                                                            context.getSource().sendFeedback(() -> Text.literal(str.toString()), false);
                                                            return targets.size();
                                                        }
                                                )
                                )
                ));
    }
}

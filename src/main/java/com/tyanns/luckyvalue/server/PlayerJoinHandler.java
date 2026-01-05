package com.tyanns.luckyvalue.server;

import com.tyanns.luckyvalue.network.LuckyValueS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerJoinHandler {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            LuckyValueS2CPayload payload = new LuckyValueS2CPayload(LuckyValueManager.getLuckyValue(player.getUuid()));
            ServerPlayNetworking.send(player, payload);
        });
    }
}

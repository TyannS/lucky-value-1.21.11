package com.tyanns.luckyvalue.server;

import com.tyanns.luckyvalue.network.LuckyValueS2CPayload;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class DayChangeHandler {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerWorld overworld = server.getOverworld();
            if (overworld == null) return;

            long time = overworld.getTimeOfDay();

            if (time % 24000 == 1) {
                for (UUID uuid : LuckyValueManager.getLuckyMap().keySet()) {
                    int value = java.util.concurrent.ThreadLocalRandom.current().nextInt(-100, 101);
                    LuckyValueManager.setLuckyValue(uuid, value);
                }
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    LuckyValueS2CPayload payload = new LuckyValueS2CPayload(LuckyValueManager.getLuckyValue(player.getUuid()));
                    ServerPlayNetworking.send(player, payload);
                }
            }
        });
    }
}

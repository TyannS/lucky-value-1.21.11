package com.tyanns.luckyvalue.client;

import com.tyanns.luckyvalue.network.LuckyValueS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;

public class LuckyValueClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(LuckyValueS2CPayload.ID, (payload, context) -> {
            ClientWorld world = context.client().world;
            if (world == null) {
                return;
            }
            ClientData.lucky_value = payload.lucky_value();
        });
    }
}

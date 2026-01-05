package com.tyanns.luckyvalue.network;

import com.tyanns.luckyvalue.LuckyValue;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LuckyValueS2CPayload(int lucky_value) implements CustomPayload {
    public static final Identifier LUCKY_VALUE_PAYLOAD_ID = Identifier.of(LuckyValue.MOD_ID, "lucky_value");
    public static final CustomPayload.Id<LuckyValueS2CPayload> ID = new CustomPayload.Id<>(LUCKY_VALUE_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, LuckyValueS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, LuckyValueS2CPayload::lucky_value, LuckyValueS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

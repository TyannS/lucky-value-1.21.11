package com.tyanns.luckyvalue.mixin;

import com.tyanns.luckyvalue.client.ClientData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderLuckyValue(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null)
            return;
        TextRenderer renderer = client.textRenderer;
        String textStr = "幸运值: " + ClientData.lucky_value;
        Text text = Text.literal(textStr);

        int margin = 8;
        int screenHeight = client.getWindow().getScaledHeight();
        int textHeight = renderer.fontHeight;

        int x = margin;
        int y = screenHeight - textHeight - margin;
        context.drawTextWithShadow(renderer, text, x, y, getLuckyColor(ClientData.lucky_value));
    }

    //幸运值决定HUD颜色
    @Unique
    private static int getLuckyColor(int value) {
        if (value < 0) {
            return 0xFFFF5555;
        } else if (value <= 50) {
            return 0xFF55FF55;
        } else if (value <= 90) {
            return 0xFF55FFFF;
        } else {
            return 0xFFFFD700;
        }
    }
}

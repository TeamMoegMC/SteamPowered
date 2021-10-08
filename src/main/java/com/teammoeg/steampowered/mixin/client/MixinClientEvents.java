package com.teammoeg.steampowered.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.content.curiosities.armor.CopperBacktankArmorLayer;
import com.simibubi.create.events.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientEvents.class)
public class MixinClientEvents {
    /**
     * @author yuesha-yc
     * @reason fix overlay when the hotbar event is canceled
     */
    @SubscribeEvent
    @Overwrite(remap = false)
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        MatrixStack ms = event.getMatrixStack();
        IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        int light = 15728880;
        int overlay = OverlayTexture.NO_OVERLAY;
        float pt = event.getPartialTicks();
        if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
            CopperBacktankArmorLayer.renderRemainingAirOverlay(ms, buffers, light, overlay, pt);
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES) {
            ClientEvents.onRenderHotbar(ms, buffers, light, overlay, pt);
        }
    }
}

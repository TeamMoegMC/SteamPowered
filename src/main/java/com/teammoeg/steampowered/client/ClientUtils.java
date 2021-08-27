package com.teammoeg.steampowered.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    public static World getClientWorld() {
        return mc().level;
    }
}

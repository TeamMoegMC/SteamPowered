/*
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Steam Powered.
 *
 * Steam Powered is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Steam Powered is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Steam Powered. If not, see <https://www.gnu.org/licenses/>.
 */

package com.teammoeg.steampowered;

import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.NonNullLazyValue;
import com.teammoeg.steampowered.client.SteamPoweredClient;
import com.teammoeg.steampowered.network.PacketHandler;
import com.teammoeg.steampowered.registrate.SPBlocks;
import com.teammoeg.steampowered.registrate.SPItems;
import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("steampowered")
public class SteamPowered {

    public static final String MODID = "steampowered";

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static final ItemGroup itemGroup = new ItemGroup(MODID) {
        @Override
        @Nonnull
        public ItemStack makeIcon() {
            return new ItemStack(SPBlocks.STEEL_FLYWHEEL.get());
        }
    };

    public static final NonNullLazyValue<CreateRegistrate> registrate = CreateRegistrate.lazy(MODID);

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public SteamPowered() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> SteamPoweredClient.addClientListeners(MinecraftForge.EVENT_BUS, FMLJavaModLoadingContext.get().getModEventBus()));

        FluidRegistry.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        SPBlocks.register();
        SPTiles.register();
        SPItems.register();
        SPTags.init();
        BlockStressValues.registerProvider(MODID,new SPStress());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SPConfig.SERVER_CONFIG);
        PacketHandler.register();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }
}

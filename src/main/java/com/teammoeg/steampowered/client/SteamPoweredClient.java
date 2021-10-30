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

package com.teammoeg.steampowered.client;

import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.block.SPBlockPartials;
import com.teammoeg.steampowered.ponder.SPPonderIndex;
import com.teammoeg.steampowered.registrate.SPBlocks;
import com.teammoeg.steampowered.registrate.SPItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SteamPoweredClient {
    public static void addClientListeners(IEventBus forgeEventBus, IEventBus modEventBus) {
        SPBlockPartials.clientInit();
        modEventBus.addListener(SteamPoweredClient::clientInit);
        modEventBus.addListener(SteamPoweredClient::setupRenderType);
    }

    public static void clientInit(FMLClientSetupEvent event) {
        SPPonderIndex.register();
    }

    public static void setupRenderType(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(FluidRegistry.steam.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(FluidRegistry.steamFlowing.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(SPBlocks.DYNAMO.get(), RenderType.cutoutMipped());
        });
    }
}

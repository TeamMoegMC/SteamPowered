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

package com.teammoeg.steampowered.registrate;

import com.teammoeg.steampowered.SteamPowered;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SPTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SteamPowered.MODID);

    public static final RegistryObject<CreativeModeTab> SP_BASE_TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.steampowered.base"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(SPBlocks.STEEL_FLYWHEEL::asStack)
                    .displayItems((a, b) -> {

                        b.accept(SPBlocks.BRONZE_BOILER);
                        b.accept(SPBlocks.BRONZE_BURNER);
                        b.accept(SPBlocks.BRONZE_STEAM_ENGINE);
                        b.accept(SPBlocks.BRONZE_FLYWHEEL);
                        b.accept(SPBlocks.BRONZE_COGWHEEL);
                        b.accept(SPBlocks.BRONZE_LARGE_COGWHEEL);

                        b.accept(SPBlocks.CAST_IRON_BOILER);
                        b.accept(SPBlocks.CAST_IRON_BURNER);
                        b.accept(SPBlocks.CAST_IRON_STEAM_ENGINE);
                        b.accept(SPBlocks.CAST_IRON_FLYWHEEL);
                        b.accept(SPBlocks.CAST_IRON_COGWHEEL);
                        b.accept(SPBlocks.CAST_IRON_LARGE_COGWHEEL);

                        b.accept(SPBlocks.STEEL_BOILER);
                        b.accept(SPBlocks.STEEL_BURNER);
                        b.accept(SPBlocks.STEEL_STEAM_ENGINE);
                        b.accept(SPBlocks.STEEL_FLYWHEEL);
                        b.accept(SPBlocks.STEEL_COGWHEEL);
                        b.accept(SPBlocks.STEEL_LARGE_COGWHEEL);

                        b.accept(SPBlocks.DYNAMO);

                        b.accept(SPItems.BRONZE_SHEET);
                        b.accept(SPItems.PRESSURIZED_GAS_CONTAINER);
                        b.accept(SPItems.PRESSURIZED_STEAM_CONTAINER);
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}

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

import com.teammoeg.steampowered.item.GasContainerItem;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SteamPowered.MODID);

    public static RegistryObject<Item> pressurizedGasContainer = ITEMS.register("pressurized_gas_container", () -> new GasContainerItem(Fluids.EMPTY, (new Item.Properties()).stacksTo(16).tab(SteamPowered.itemGroup)));
    public static RegistryObject<Item> pressurizedSteamContainer = ITEMS.register("pressurized_steam_container", () -> new GasContainerItem(FluidRegistry.steam, new Item.Properties().stacksTo(1).tab(SteamPowered.itemGroup).craftRemainder(ItemRegistry.pressurizedGasContainer.get())));
}

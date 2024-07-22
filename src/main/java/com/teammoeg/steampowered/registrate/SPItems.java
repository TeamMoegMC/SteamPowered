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

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.teammoeg.steampowered.SteamPowered.REGISTRATE;

public class SPItems {

    static {
        REGISTRATE.setCreativeTab(SPTabs.SP_BASE_TAB);
    }

    public static final ItemEntry<Item> BRONZE_SHEET =
            REGISTRATE.item("bronze_sheet", Item::new)
                    .register();

    public static final ItemEntry<Item> PRESSURIZED_GAS_CONTAINER =
            REGISTRATE.item("pressurized_gas_container", Item::new)
                    .register();

    public static final ItemEntry<Item> PRESSURIZED_STEAM_CONTAINER =
            REGISTRATE.item("pressurized_steam_container", Item::new)
                    .register();

    public static void register() {
    }
}

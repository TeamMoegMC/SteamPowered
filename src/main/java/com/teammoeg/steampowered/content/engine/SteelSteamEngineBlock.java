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

package com.teammoeg.steampowered.content.engine;

import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SteelSteamEngineBlock extends SteamEngineBlock implements ITE<SteelSteamEngineTileEntity> {
    public SteelSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.STEEL_STEAM_ENGINE.create();
    }

    @Override
    public Class<SteelSteamEngineTileEntity> getTileEntityClass() {
        return SteelSteamEngineTileEntity.class;
    }
}

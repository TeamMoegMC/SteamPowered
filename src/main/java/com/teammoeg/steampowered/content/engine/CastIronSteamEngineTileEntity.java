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

import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.registrate.SPBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastIronSteamEngineTileEntity extends SteamEngineTileEntity {
    public CastIronSteamEngineTileEntity(BlockEntityType<? extends SteamEngineTileEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Block getFlywheel() {
        return SPBlocks.CAST_IRON_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return SPConfig.COMMON.castIronFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return SPConfig.COMMON.castIronFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return SPConfig.COMMON.castIronFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return SPConfig.COMMON.castIronFlywheelSteamStorage.get();
    }
}

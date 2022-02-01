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

package com.teammoeg.steampowered.content.cogwheel;

import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import com.teammoeg.steampowered.registrate.SPTiles;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class MetalCogwheelBlock extends CogWheelBlock {
    protected MetalCogwheelBlock(boolean large, Properties properties) {
        super(large, properties);
    }

    public MetalCogwheelTileEntity createTileEntity(BlockState state, BlockGetter world) {
        return SPTiles.METAL_COGWHEEL.create();
    }

    public static MetalCogwheelBlock small(Properties properties) {
        return new MetalCogwheelBlock(false, properties);
    }

    public static MetalCogwheelBlock large(Properties properties) {
        return new MetalCogwheelBlock(true, properties);
    }
}

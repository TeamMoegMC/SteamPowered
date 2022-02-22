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

package com.teammoeg.steampowered.content.boiler;

import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.registrate.SPTiles;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CastIronBoilerBlock extends BoilerBlock implements ITE<CastIronBoilerTileEntity> {
    public CastIronBoilerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuConsume() {
		return SPConfig.COMMON.castIronBoilerHU.get();
	}

    @Override
    public Class<CastIronBoilerTileEntity> getTileEntityClass() {
        return CastIronBoilerTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends CastIronBoilerTileEntity> getTileEntityType() {
        return SPTiles.CAST_IRON_BOILER.get();
    }
}

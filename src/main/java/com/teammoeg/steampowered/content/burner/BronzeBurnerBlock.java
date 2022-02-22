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

package com.teammoeg.steampowered.content.burner;

import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.registrate.SPTiles;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;


public class BronzeBurnerBlock extends BurnerBlock implements ITE<BronzeBurnerTileEntity> {
    public BronzeBurnerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuProduce() {
		return SPConfig.COMMON.bronzeBurnerHU.get();
	}

	@Override
	public double getEfficiency() {
		return SPConfig.COMMON.bronzeBurnerEfficiency.get();
	}

	@Override
	public Class<BronzeBurnerTileEntity> getTileEntityClass() {
		return BronzeBurnerTileEntity.class;
	}

	@Override
	public BlockEntityType<? extends BronzeBurnerTileEntity> getTileEntityType() {
		return SPTiles.BRONZE_BURNER.get();
	}
}

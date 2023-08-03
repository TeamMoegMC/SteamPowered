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

import com.simibubi.create.foundation.block.IBE;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.registrate.SPBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastIronBoilerBlock extends BoilerBlock implements IBE<CastIronBoilerBlockEntity> {
    public CastIronBoilerBlock(Properties properties) {
        super(properties);
    }

	@Override
	public int getHuConsume() {
		return SPConfig.COMMON.castIronBoilerHU.get();
	}

    @Override
    public Class<CastIronBoilerBlockEntity> getBlockEntityClass() {
        return CastIronBoilerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CastIronBoilerBlockEntity> getBlockEntityType() {
        return SPBlockEntities.CAST_IRON_BOILER.get();
    }
}

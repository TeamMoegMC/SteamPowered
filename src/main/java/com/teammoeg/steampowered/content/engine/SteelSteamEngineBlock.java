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

import java.util.List;

import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.item.ItemDescription.Palette;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.client.ClientUtils;
import com.teammoeg.steampowered.registrate.SPTiles;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t,
			ITooltipFlag f) {
    	if(Screen.hasShiftDown()) {
    		t.add(new TranslationTextComponent("tooltip.steampowered.engine.brief").withStyle(TextFormatting.GOLD));
    		if(ClientUtils.hasGoggles()) 
    		t.add(new TranslationTextComponent("tooltip.steampowered.engine.steamconsume",SPConfig.COMMON.steelFlywheelSteamConsumptionPerTick.get()).withStyle(TextFormatting.GOLD));
    	}else {
    		t.add(TooltipHelper.holdShift(Palette.Gray,false));
    	}
		super.appendHoverText(i,w,t,f);
	}
    @Override
    public Class<SteelSteamEngineTileEntity> getTileEntityClass() {
        return SteelSteamEngineTileEntity.class;
    }
}

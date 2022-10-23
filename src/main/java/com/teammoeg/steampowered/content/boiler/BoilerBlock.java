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

import java.util.List;
import com.simibubi.create.foundation.item.ItemDescription.Palette;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.teammoeg.steampowered.client.ClientUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public abstract class BoilerBlock extends Block {

	@Override
    public ActionResultType use(BlockState bs, World w, BlockPos bp, PlayerEntity pe, Hand h, BlockRayTraceResult br) {
		if (FluidUtil.interactWithFluidHandler(pe, h,w, bp,br.getDirection()))
			return ActionResultType.SUCCESS;
        return ActionResultType.PASS;
    }

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
		return true;
	}
	
	public BoilerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getAnalogOutputSignal(BlockState b, World w, BlockPos p) {
		TileEntity te = w.getBlockEntity(p);
		if (te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler = (BoilerTileEntity) te;
			return boiler.output.getFluidAmount() * 15 / boiler.output.getCapacity();
		}
		return super.getAnalogOutputSignal(b, w, p);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public abstract int getHuConsume();

	@Override
	public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t, ITooltipFlag f) {
		if (Screen.hasShiftDown()) {
			t.add(new TranslationTextComponent("tooltip.steampowered.boiler.brief").withStyle(TextFormatting.GOLD));
			if (ClientUtils.hasGoggles()) {
				t.add(new TranslationTextComponent("tooltip.steampowered.boiler.danger").withStyle(TextFormatting.RED));
				t.add(new TranslationTextComponent("tooltip.steampowered.boiler.huconsume", this.getHuConsume())
						.withStyle(TextFormatting.GOLD));
				t.add(new TranslationTextComponent("tooltip.steampowered.boiler.waterconsume",
						((int) Math.ceil(this.getHuConsume() / 120.0))).withStyle(TextFormatting.AQUA));
				t.add(new TranslationTextComponent("tooltip.steampowered.boiler.steamproduce", this.getHuConsume() / 10)
						.withStyle(TextFormatting.GOLD));
			}
		} else {
			t.add(TooltipHelper.holdShift(Palette.Gray, false));
		}
		if (Screen.hasControlDown()) {
			t.add(new TranslationTextComponent("tooltip.steampowered.boiler.redstone").withStyle(TextFormatting.RED));
		} else {
			t.add(Lang
					.translate("tooltip.holdForControls",
							Lang.translate("tooltip.keyCtrl").withStyle(TextFormatting.GRAY))
					.withStyle(TextFormatting.DARK_GRAY));
		}
		super.appendHoverText(i, w, t, f);
	}

	@Override
	public void stepOn(World w, BlockPos bp, Entity e) {
		TileEntity te = w.getBlockEntity(bp);
		if (te instanceof BoilerTileEntity && e instanceof LivingEntity) {
			if (((BoilerTileEntity) te).lastheat > 0 || (!((BoilerTileEntity) te).output.isEmpty())) {
				e.hurt(DamageSource.HOT_FLOOR, 2);
			}
		}
	}
}

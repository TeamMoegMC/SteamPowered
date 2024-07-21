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
import java.util.Random;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.teammoeg.steampowered.client.ClientUtils;
import com.teammoeg.steampowered.client.Particles;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class BoilerBlock extends Block implements LiquidBlockContainer {
	@Override
	public boolean canPlaceLiquid(BlockGetter w, BlockPos p, BlockState s, Fluid f) {
		BlockEntity te = w.getBlockEntity(p);
		if (te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler = (BoilerTileEntity) te;
			if (boiler.input.fill(new FluidStack(f, 1000), FluidAction.SIMULATE) == 1000)
				return true;
		}
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor w, BlockPos p, BlockState s, FluidState f) {
		BlockEntity te = w.getBlockEntity(p);
		if (te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler = (BoilerTileEntity) te;
			if (boiler.input.fill(new FluidStack(f.getType(), 1000), FluidAction.SIMULATE) == 1000) {
				boiler.input.fill(new FluidStack(f.getType(), 1000), FluidAction.EXECUTE);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
		return true;
	}

	public BoilerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getAnalogOutputSignal(BlockState b, Level w, BlockPos p) {
		BlockEntity te = w.getBlockEntity(p);
		if (te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler = (BoilerTileEntity) te;
			return boiler.output.getFluidAmount() * 15 / boiler.output.getCapacity();
		}
		return super.getAnalogOutputSignal(b, w, p);
	}


	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public abstract int getHuConsume();

	@Override
	public void appendHoverText(ItemStack i, BlockGetter w, List<Component> t, TooltipFlag f) {
		if (Screen.hasShiftDown()) {
			t.add(new TranslatableComponent("tooltip.steampowered.boiler.brief").withStyle(ChatFormatting.GOLD));
			if (ClientUtils.hasGoggles()) {
				t.add(new TranslatableComponent("tooltip.steampowered.boiler.danger").withStyle(ChatFormatting.RED));
				t.add(new TranslatableComponent("tooltip.steampowered.boiler.huconsume", this.getHuConsume())
						.withStyle(ChatFormatting.GOLD));
				t.add(new TranslatableComponent("tooltip.steampowered.boiler.waterconsume",
						((int) Math.ceil(this.getHuConsume() / 120.0))).withStyle(ChatFormatting.AQUA));
				t.add(new TranslatableComponent("tooltip.steampowered.boiler.steamproduce", this.getHuConsume() / 10)
						.withStyle(ChatFormatting.GOLD));
			}
		} else {
			t.add(TooltipHelper.holdShift(TooltipHelper.Palette.GRAY, false));
		}
		if (Screen.hasControlDown()) {
			t.add(new TranslatableComponent("tooltip.steampowered.boiler.redstone").withStyle(ChatFormatting.RED));
		} else {
			t.add(Lang
					.translate("tooltip.holdForControls",
							Lang.translate("tooltip.keyCtrl").style(ChatFormatting.GRAY))
					.style(ChatFormatting.DARK_GRAY).component());
		}
		super.appendHoverText(i, w, t, f);
	}


	public void stepOn(Level w, BlockPos bp, Entity e) {
		BlockEntity te = w.getBlockEntity(bp);
		if (te instanceof BoilerTileEntity && e instanceof LivingEntity) {
			if (((BoilerTileEntity) te).lastheat > 0 || (!((BoilerTileEntity) te).output.isEmpty())) {
				e.hurt(DamageSource.HOT_FLOOR, 2);
			}
		}
	}
}

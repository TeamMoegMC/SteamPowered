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

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import com.teammoeg.steampowered.client.ClientUtils;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;

public abstract class BoilerBlock extends Block {
	@Override
	public InteractionResult use(BlockState bs, Level w, BlockPos bp, Player pe, InteractionHand h, BlockHitResult br) {
		if (FluidUtil.interactWithFluidHandler(pe, h,w, bp,br.getDirection()))
			return InteractionResult.SUCCESS;
		return InteractionResult.PASS;
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
			t.add(Component.translatable("tooltip.steampowered.boiler.brief").withStyle(ChatFormatting.GOLD));
			if (ClientUtils.hasGoggles()) {
				t.add(Component.translatable("tooltip.steampowered.boiler.danger").withStyle(ChatFormatting.RED));
				t.add(Component.translatable("tooltip.steampowered.boiler.huconsume", this.getHuConsume())
						.withStyle(ChatFormatting.GOLD));
				t.add(Component.translatable("tooltip.steampowered.boiler.waterconsume",
						((int) Math.ceil(this.getHuConsume() / 120.0))).withStyle(ChatFormatting.AQUA));
				t.add(Component.translatable("tooltip.steampowered.boiler.steamproduce", this.getHuConsume() / 10)
						.withStyle(ChatFormatting.GOLD));
			}
		} else {
			t.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY, false));
		}
		if (Screen.hasControlDown()) {
			t.add(Component.translatable("tooltip.steampowered.boiler.redstone").withStyle(ChatFormatting.RED));
		} else {
			t.add(CreateLang
					.translate("tooltip.holdForControls",
							CreateLang.translate("tooltip.keyCtrl").style(ChatFormatting.GRAY))
					.style(ChatFormatting.DARK_GRAY).component());
		}
		super.appendHoverText(i, w, t, f);
	}


	public void stepOn(Level w, BlockPos bp, Entity e) {
		BlockEntity te = w.getBlockEntity(bp);
		if (te instanceof BoilerTileEntity && e instanceof LivingEntity) {
			if (((BoilerTileEntity) te).lastheat > 0 || (!((BoilerTileEntity) te).output.isEmpty())) {
				e.hurt(w.damageSources().hotFloor(), 2);
			}
		}
	}
}

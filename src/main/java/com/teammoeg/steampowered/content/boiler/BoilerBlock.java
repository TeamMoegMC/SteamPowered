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

import com.simibubi.create.foundation.item.ItemDescription.Palette;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.teammoeg.steampowered.client.ClientUtils;
import com.teammoeg.steampowered.client.Particles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public abstract class BoilerBlock extends Block implements ILiquidContainer {
	@Override
	public boolean canPlaceLiquid(IBlockReader w, BlockPos p, BlockState s, Fluid f) {
		TileEntity te = w.getBlockEntity(p);
		if (te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler = (BoilerTileEntity) te;
			if (boiler.input.fill(new FluidStack(f, 1000), FluidAction.SIMULATE) == 1000)
				return true;
		}
		return false;
	}

	@Override
	public boolean placeLiquid(IWorld w, BlockPos p, BlockState s, FluidState f) {
		TileEntity te = w.getBlockEntity(p);
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
	public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
		TileEntity te = p_180655_2_.getBlockEntity(p_180655_3_);
		if (te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler = (BoilerTileEntity) te;
			if (boiler.output.getFluidAmount()>=10000&&boiler.lastheat!=0) {//steam leaking
				double d0 = p_180655_3_.getX();
				double d1 = p_180655_3_.getY() + 1;
				double d2 = p_180655_3_.getZ();
				//if(p_180655_4_.nextDouble()<0.5D) {
					p_180655_2_.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.25F, 0.25F, false);
					int count=4;
					while(--count!=0)
					p_180655_2_.addParticle(Particles.STEAM.get(), d0+p_180655_4_.nextFloat(), d1, d2+p_180655_4_.nextFloat(), 0.0D, 0.0D, 0.0D);
				//}
			}
		}
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

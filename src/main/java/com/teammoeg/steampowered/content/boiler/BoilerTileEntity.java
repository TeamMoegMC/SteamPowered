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

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.client.Particles;
import com.teammoeg.steampowered.content.burner.IHeatReceiver;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public abstract class BoilerTileEntity extends TileEntity
		implements IHeatReceiver, ITickableTileEntity, IHaveGoggleInformation {
	FluidTank input = new FluidTank(10000, s -> s.getFluid() == Fluids.WATER);
	public FluidTank output = new FluidTank(10000);
	private IFluidHandler ft = new IFluidHandler() {
		@Override
		public int getTanks() {
			return 2;
		}

		@Override
		public FluidStack getFluidInTank(int tank) {
			switch (tank) {
			case 0:
				return input.getFluid();
			case 1:
				return output.getFluid();
			default:
				return null;
			}
		}

		@Override
		public int getTankCapacity(int tank) {
			return 10000;
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			if (tank == 0 && stack.getFluid() == Fluids.WATER)
				return true;
			return false;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			int filled=input.fill(resource, action);
			if(filled>0&&action==FluidAction.EXECUTE) {
				setChanged();
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
			}
			return filled;	
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			FluidStack drained=output.drain(resource, action);
			if(!drained.isEmpty()&&action==FluidAction.EXECUTE){
				setChanged();
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
			}
			return drained;
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			FluidStack drained=output.drain(maxDrain, action);
			if(!drained.isEmpty()&&action==FluidAction.EXECUTE){
				setChanged();
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
			}
			return drained;
		}
	};
	int heatreceived;
	int lastheat;
	private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> ft);

	public BoilerTileEntity(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}

	// Easy, easy
	public void readCustomNBT(CompoundNBT nbt) {
		input.readFromNBT(nbt.getCompound("in"));
		output.readFromNBT(nbt.getCompound("out"));
		heatreceived = nbt.getInt("hu");
		lastheat = nbt.getInt("lasthu");
	}

	// Easy, easy
	public void writeCustomNBT(CompoundNBT nbt) {
		nbt.put("in", input.writeToNBT(new CompoundNBT()));
		nbt.put("out", output.writeToNBT(new CompoundNBT()));
		nbt.putInt("hu", heatreceived);
		nbt.putInt("lasthu", lastheat);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		readCustomNBT(nbt);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		writeCustomNBT(nbt);
		return nbt;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.writeCustomNBT(nbt);
		return new SUpdateTileEntityPacket(this.getBlockPos(), 3, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.readCustomNBT(pkt.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		writeCustomNBT(nbt);
		return nbt;
	}

	@Override
	public void tick() {

		if (this.level == null)
			return;
		if (!this.level.isClientSide) {
			lastheat = heatreceived;
			if (heatreceived != 0) {
				int consume = Math.min(getHUPerTick(), heatreceived);
				heatreceived = 0;
				double waterconsume = (SPConfig.COMMON.steamPerWater.get() * 10);
				consume = Math.min((int) (this.input.drain((int) Math.ceil(consume / waterconsume), FluidAction.EXECUTE)
						.getAmount() * waterconsume), consume);
				this.output.fill(new FluidStack(FluidRegistry.steam.get().getFluid(), consume / 10),
						FluidAction.EXECUTE);
				if(consume>0) {
					this.setChanged();
					this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
				}
			}
			

		} else {
			if (output.getFluidAmount() >= output.getCapacity() && lastheat != 0) {// steam leaking
				particleInterval++;
				if (particleInterval >= 20) {
					particleInterval = 0;
					double d0 = this.worldPosition.getX();
					double d1 = this.worldPosition.getY() + 1;
					double d2 = this.worldPosition.getZ();
					// if(p_180655_4_.nextDouble()<0.5D) {
					
					this.level.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.25F,
							0.25F, false);
					
					for(int i=0;i<this.level.random.nextInt(3)+1;i++)
					this.level.addParticle(Particles.STEAM.get(), d0 + this.level.random.nextFloat(), d1,
							d2 + this.level.random.nextFloat(), 0.0D, 0.0D, 0.0D);
					// }
				}
			} else
				particleInterval = 0;
		}
	}

	private int particleInterval = 0;

	@Override
	public void commitHeat(float value) {
		heatreceived = (int) value;

	}

	public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, LazyOptional.of(() -> input));
		this.containedFluidTooltip(tooltip, isPlayerSneaking, LazyOptional.of(() -> output));
		return true;
	}

	protected abstract int getHUPerTick();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.holder.isPresent()) {
			this.refreshCapability();
		}
		return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? holder.cast() : super.getCapability(cap, side);
	}

	private void refreshCapability() {
		LazyOptional<IFluidHandler> oldCap = this.holder;
		this.holder = LazyOptional.of(() -> {
			return this.ft;
		});
		oldCap.invalidate();
	}
}

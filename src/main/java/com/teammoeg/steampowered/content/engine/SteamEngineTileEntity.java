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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.client.Particles;
import com.teammoeg.steampowered.content.boiler.BoilerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public abstract class SteamEngineTileEntity extends EngineTileEntity implements IHaveGoggleInformation {

	private FluidTank tank;
	private IFluidHandler handler=new IFluidHandler() {

		@Override
		public int getTanks() {
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(int itank) {
			return tank.getFluid();
		}

		@Override
		public int getTankCapacity(int itank) {
			return tank.getCapacity();
		}

		@Override
		public boolean isFluidValid(int itank, FluidStack stack) {
			return tank.isFluidValid(stack);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			int filled=tank.fill(resource, action);
			if(filled>0&&action==FluidAction.EXECUTE) {
				setChanged();
				level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
			}
			return filled;
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return FluidStack.EMPTY;
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return FluidStack.EMPTY;
		}
		
	};
	private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> handler);
	private int heatup = 0;

	public SteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
		super(type);
		this.refreshCapability();
		this.tank = new FluidTank(this.getSteamStorage(), fluidStack -> {
			ITag<Fluid> steamTag = FluidTags.getAllTags().getTag(new ResourceLocation("forge", "steam"));
			if (steamTag != null)
				return fluidStack.getFluid().is(steamTag);
			else
				return fluidStack.getFluid() == FluidRegistry.steam.get();
		});
	}

	@Override
	public void tick() {
		super.tick();
		if(level == null)return;
		if (!level.isClientSide) {
			BlockState state = this.level.getBlockState(this.worldPosition);
			if(this.poweredWheel==null)attachWheel();
			if (this.poweredWheel == null || this.poweredWheel.isRemoved()) {
				if(this.appliedSpeed!=0||this.appliedCapacity!=0) {
					this.appliedCapacity = 0;
					this.appliedSpeed = 0;
					this.refreshWheelSpeed();
				}
				heatup = 0;
				if(!tank.drain(this.getSteamConsumptionPerTick(), IFluidHandler.FluidAction.EXECUTE).isEmpty()) {
					this.setChanged();
					level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
				}
				if(state.getValue(SteamEngineBlock.LIT))
					this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, false));
			} else {
				Direction engineFacing = this.getBlockState().getValue(EngineBlock.FACING);
				BlockPos boilerPos = this.worldPosition.relative(engineFacing, -1);
				FluidTank tank=this.tank;
				if(this.tank.isEmpty()) {
					TileEntity te=this.getWorld().getBlockEntity(boilerPos);
					if(te instanceof BoilerTileEntity) {
						tank=((BoilerTileEntity)te).output;
					}
				}
				if(heatup==0&&tank.getFluidAmount()/this.getSteamConsumptionPerTick()<40) 
					return;
				
				if (!tank.isEmpty() && tank.drain(this.getSteamConsumptionPerTick(), IFluidHandler.FluidAction.EXECUTE)
						.getAmount() >= this.getSteamConsumptionPerTick()) {
					
					if (heatup >= 60) {
						float spd=this.getGeneratingSpeed();
						float cap=this.getGeneratingCapacity();
						if(this.tank!=tank)
							cap = MathHelper.ceil(cap*this.getSuckEfficiency());
						if(this.appliedSpeed!=spd||this.appliedCapacity!=cap) {
							this.appliedSpeed = spd;
							this.appliedCapacity = cap;
							this.refreshWheelSpeed();
						}
					} else {
						heatup++;
						this.setChanged();
						level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
						if(!state.getValue(SteamEngineBlock.LIT))
							this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, true));
					}
				} else {
					if (heatup > 0) {
						heatup--;
						this.setChanged();
						level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
					}
					if(this.appliedSpeed!=0||this.appliedCapacity!=0) {
						this.appliedCapacity = 0;
						this.appliedSpeed = 0;
						this.refreshWheelSpeed();
					}
					if(state.getValue(SteamEngineBlock.LIT))
						this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, false));
				}
			}
		}else{
			if (this.getBlockState().getValue(SteamEngineBlock.LIT)) {
				paticleInterval++;
	            double d0 = this.worldPosition.getX() + 0.5D;
	            double d1 = this.worldPosition.getY();
	            double d2 = this.worldPosition.getZ() + 0.5D;
	            if (paticleInterval>=40) {
	            	paticleInterval=0;
	                this.level.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.25f, 0.25F, false);
	                Direction direction = this.getBlockState().getValue(SteamEngineBlock.FACING);
		            Direction.Axis direction$axis = direction.getAxis();
		            double d4 = this.level.random.nextDouble() * 0.6D - 0.3D;
		            double d5 = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52D : d4;
		            double d6 = this.level.random.nextDouble() * 9.0D / 16.0D;
		            double d7 = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : d4;
		            this.level.addParticle(Particles.STEAM.get(), d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
	            }
	        }else paticleInterval=0;
		}
	}
	private int paticleInterval;
	public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
		if (!this.getBlockState().getValue(SteamEngineBlock.LIT)) {
			tooltip.add(componentSpacing.plainCopy()
					.append(new TranslationTextComponent("tooltip.steampowered.steam_engine.not_enough_steam")
							.withStyle(TextFormatting.RED)));
		} else if (heatup < 60) {
			tooltip.add(componentSpacing.plainCopy()
					.append(new TranslationTextComponent("tooltip.steampowered.steam_engine.heating")
							.withStyle(TextFormatting.YELLOW)));
		} else {
			tooltip.add(componentSpacing.plainCopy()
					.append(new TranslationTextComponent("tooltip.steampowered.steam_engine.running")
							.withStyle(TextFormatting.GREEN)));
		}
		return this.containedFluidTooltip(tooltip, isPlayerSneaking,
				getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
	}

	protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
		super.fromTag(state, compound, clientPacket);
		tank.readFromNBT(compound.getCompound("TankContent"));
		heatup = compound.getInt("heatup");
	}

	public void write(CompoundNBT compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.put("TankContent", tank.writeToNBT(new CompoundNBT()));
		compound.putInt("heatup", heatup);
		
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (!this.holder.isPresent()) {
			this.refreshCapability();
		}
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? holder.cast()
				: super.getCapability(capability, facing);
	}

	private void refreshCapability() {
		LazyOptional<IFluidHandler> oldCap = this.holder;
		this.holder = LazyOptional.of(() -> {
			return this.handler;
		});
		oldCap.invalidate();
	}

	public void attachWheel() {
		Direction engineFacing = this.getBlockState().getValue(EngineBlock.FACING);
		BlockPos wheelPos = this.worldPosition.relative(engineFacing, 2);
		BlockState wheelState = this.level.getBlockState(wheelPos);
		
		if (this.getFlywheel() == wheelState.getBlock()) {
			Direction wheelFacing = wheelState.getValue(FlywheelBlock.HORIZONTAL_FACING);
			if (wheelFacing.getAxis() == engineFacing.getClockWise().getAxis()) {
				if (!FlywheelBlock.isConnected(wheelState)
						|| FlywheelBlock.getConnection(wheelState) == engineFacing.getOpposite()) {
					TileEntity te = this.level.getBlockEntity(wheelPos);
					if (!te.isRemoved()) {
						if (te instanceof FlywheelTileEntity) {
							if (!FlywheelBlock.isConnected(wheelState)) {
								FlywheelBlock.setConnection(this.level, te.getBlockPos(), te.getBlockState(),
										engineFacing.getOpposite());
							}

							this.poweredWheel = (FlywheelTileEntity) te;
							this.refreshWheelSpeed();
							return;
						}

					}
				}
			}
		}
		if(poweredWheel!=null&&!poweredWheel.isRemoved()) {
			this.poweredWheel.setRotation(0, 0);
			this.poweredWheel =null;
		}
	}

	public abstract Block getFlywheel();

	public abstract float getGeneratingCapacity();

	public abstract float getGeneratingSpeed();

	public abstract int getSteamConsumptionPerTick();

	public abstract int getSteamStorage();
	
	public abstract double getSuckEfficiency();

	@Override
	public void lazyTick() {
		super.lazyTick();
		this.attachWheel();
	}
}

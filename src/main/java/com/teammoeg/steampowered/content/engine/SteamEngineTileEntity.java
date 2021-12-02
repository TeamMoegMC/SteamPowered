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

import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class SteamEngineTileEntity extends EngineTileEntity implements IHaveGoggleInformation {

    private FluidTank tank;
    private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);
    private int heatup=0;
    public SteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
        this.refreshCapability();
        this.tank = new FluidTank(this.getSteamStorage(), fluidStack -> {
            ITag<Fluid> steamTag = FluidTags.getAllTags().getTag(new ResourceLocation("forge", "steam"));
            if (steamTag != null) return fluidStack.getFluid().is(steamTag);
            else return fluidStack.getFluid() == FluidRegistry.steam.get();
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            if (!tank.isEmpty()) {
                if (tank.drain(this.getSteamConsumptionPerTick(), IFluidHandler.FluidAction.EXECUTE).getAmount() >= this.getSteamConsumptionPerTick()) {
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, true));
                    if(heatup>=20) {
	                    this.appliedCapacity = this.getGeneratingCapacity();
	                    this.appliedSpeed = this.getGeneratingSpeed();
	                    this.refreshWheelSpeed();
                    }else
                    	heatup++;
                }
            } else {
            	if(heatup>0)
            		heatup--;
                this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, false));
                this.appliedCapacity = 0;
                this.appliedSpeed = 0;
                this.refreshWheelSpeed();
            }
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
        if (tank.isEmpty() || tank.getFluidAmount() < this.getSteamConsumptionPerTick()) {
            tooltip.add(componentSpacing.plainCopy().append(new TranslationTextComponent("tooltip.steampowered.steam_engine.not_enough_steam").withStyle(TextFormatting.RED)));
        }else if(heatup<2S0) {
        	tooltip.add(componentSpacing.plainCopy().append(new TranslationTextComponent("tooltip.steampowered.steam_engine.heating").withStyle(TextFormatting.YELLOW)));
        } else {
            tooltip.add(componentSpacing.plainCopy().append(new TranslationTextComponent("tooltip.steampowered.steam_engine.running").withStyle(TextFormatting.GREEN)));
        }
        return this.containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
    }

    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        tank.readFromNBT(compound.getCompound("TankContent"));
        heatup=compound.getInt("heatup");
    }

    public void write(CompoundNBT compound, boolean clientPacket) {
        compound.put("TankContent", tank.writeToNBT(new CompoundNBT()));
        compound.putInt("heatup",heatup);
        super.write(compound, clientPacket);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!this.holder.isPresent()) {
            this.refreshCapability();
        }
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? holder.cast() : super.getCapability(capability, facing);
    }

    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = this.holder;
        this.holder = LazyOptional.of(() -> {
            return this.tank;
        });
        oldCap.invalidate();
    }

    public void attachWheel() {
        Direction engineFacing = (Direction) this.getBlockState().getValue(EngineBlock.FACING);
        BlockPos wheelPos = this.worldPosition.relative(engineFacing, 2);
        BlockState wheelState = this.level.getBlockState(wheelPos);
        if (this.getFlywheel() == wheelState.getBlock()) {
            Direction wheelFacing = (Direction) wheelState.getValue(FlywheelBlock.HORIZONTAL_FACING);
            if (wheelFacing.getAxis() == engineFacing.getClockWise().getAxis()) {
                if (!FlywheelBlock.isConnected(wheelState) || FlywheelBlock.getConnection(wheelState) == engineFacing.getOpposite()) {
                    TileEntity te = this.level.getBlockEntity(wheelPos);
                    if (!te.isRemoved()) {
                        if (te instanceof FlywheelTileEntity) {
                            if (!FlywheelBlock.isConnected(wheelState)) {
                                FlywheelBlock.setConnection(this.level, te.getBlockPos(), te.getBlockState(), engineFacing.getOpposite());
                            }

                            this.poweredWheel = (FlywheelTileEntity) te;
                            this.refreshWheelSpeed();
                        }

                    }
                }
            }
        }
    }

    public abstract Block getFlywheel();

    public abstract float getGeneratingCapacity();

    public abstract float getGeneratingSpeed();

    public abstract int getSteamConsumptionPerTick();

    public abstract int getSteamStorage();
}

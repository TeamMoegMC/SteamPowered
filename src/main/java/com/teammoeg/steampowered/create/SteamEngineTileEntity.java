package com.teammoeg.steampowered.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import com.simibubi.create.foundation.block.BlockStressValues;
import com.teammoeg.steampowered.FluidRegistry;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SteamEngineTileEntity extends EngineTileEntity {

    private static final float GENERATING_CAPACITY = 128F;
    private static final float GENERATING_SPEED = 16F;
    private static final int CONSUMING_STEAM_MB_PER_TICK = 160;
    private static final int STEAM_STORAGE_MAXIMUM = 160000;

    protected FluidTank tank = new FluidTank(STEAM_STORAGE_MAXIMUM, fluidStack -> {
        return fluidStack.getFluid() == FluidRegistry.steam.get();
    });

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public SteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
    }

    @Override
    public void lazyTick() {
        if (level != null && !level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            if (!tank.isEmpty() && tank.getFluidAmount() > CONSUMING_STEAM_MB_PER_TICK) {
                state.setValue(SteamEngineBlock.LIT, true);
                this.appliedCapacity = GENERATING_CAPACITY;
                this.appliedSpeed = GENERATING_SPEED;
                this.refreshWheelSpeed();
                tank.drain(CONSUMING_STEAM_MB_PER_TICK, IFluidHandler.FluidAction.EXECUTE);
            } else {
                state.setValue(SteamEngineBlock.LIT, false);
                this.appliedCapacity = 0;
                this.appliedSpeed = 0;
                this.refreshWheelSpeed();
            }
        }
        super.lazyTick();
    }

    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        tank.readFromNBT(compound.getCompound("TankContent"));
    }

    public void write(CompoundNBT compound, boolean clientPacket) {
        compound.put("TankContent", tank.writeToNBT(new CompoundNBT()));
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    public FluidTank getTank() {
        return tank;
    }
}

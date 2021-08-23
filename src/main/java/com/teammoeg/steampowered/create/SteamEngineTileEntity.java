package com.teammoeg.steampowered.create;

import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import net.minecraft.block.BlockState;
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

    protected FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 10, fluidStack -> {
        return fluidStack.getFluid() == Fluids.WATER;
    });

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public SteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
    }

    @Override
    public void lazyTick() {
        System.out.println("STEAM_ENGINE: " + tank.getFluidAmount());
        tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
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

package com.teammoeg.steampowered.tileentity;

import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.block.SteamEngineBlock;
import com.teammoeg.steampowered.network.PacketHandler;
import com.teammoeg.steampowered.network.TileSyncPacket;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SteamEngineTileEntity extends EngineTileEntity implements IHaveGoggleInformation {

    private static final float GENERATING_CAPACITY = 32F;
    private static final float GENERATING_SPEED = 32F;
    private static final int CONSUMING_STEAM_MB_PER_TICK = 30;
    private static final int STEAM_STORAGE_MAXIMUM = 100000;

    protected FluidTank tank = new FluidTank(STEAM_STORAGE_MAXIMUM, fluidStack -> {
        return fluidStack.getFluid() == FluidRegistry.steam.get();
    }) {
        protected void onContentsChanged() {
            syncFluidContent();
        }
    };

    public void syncFluidContent() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("tank", tank.writeToNBT(new CompoundNBT()));
        PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> {
            return this.level.getChunkAt(this.worldPosition);
        }), new TileSyncPacket(this, nbt));
    }

    public void receiveFromServer(CompoundNBT message) {
        if (message.contains("tank", 10)) {
            this.tank.readFromNBT(message.getCompound("tank"));
        }
    }

    public void receiveFromClient(CompoundNBT message) {

    }

    private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public SteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
        this.refreshCapability();
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            if (!tank.isEmpty()) {
                if (tank.getFluidAmount() < CONSUMING_STEAM_MB_PER_TICK) {
                    tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                } else {
                    tank.drain(CONSUMING_STEAM_MB_PER_TICK, IFluidHandler.FluidAction.EXECUTE);
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, true));
                    this.appliedCapacity = GENERATING_CAPACITY;
                    this.appliedSpeed = GENERATING_SPEED;
                    this.refreshWheelSpeed();
                }
            } else {
                this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, false));
                this.appliedCapacity = 0;
                this.appliedSpeed = 0;
                this.refreshWheelSpeed();
            }
        }
    }

    public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
        return this.containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
    }

    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        tank.readFromNBT(compound.getCompound("TankContent"));
    }

    public void write(CompoundNBT compound, boolean clientPacket) {
        compound.put("TankContent", tank.writeToNBT(new CompoundNBT()));
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
}

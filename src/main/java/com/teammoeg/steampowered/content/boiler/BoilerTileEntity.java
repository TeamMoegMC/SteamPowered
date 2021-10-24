package com.teammoeg.steampowered.content.boiler;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class BoilerTileEntity extends TileEntity implements IHeatReceiver, ITickableTileEntity, IHaveGoggleInformation {
    FluidTank input = new FluidTank(10000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }
    };
    FluidTank output = new FluidTank(10000) {

    };
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
            return input.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return output.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return output.drain(maxDrain, action);
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
    }

    // Easy, easy
    public void writeCustomNBT(CompoundNBT nbt) {
        nbt.put("in", input.writeToNBT(new CompoundNBT()));
        nbt.put("out", output.writeToNBT(new CompoundNBT()));
        nbt.putInt("hu", heatreceived);
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
    	lastheat=heatreceived;
        //debug
        if (this.level != null && !this.level.isClientSide) {
            int consume = Math.min(getHUPerTick(), heatreceived);
            heatreceived = 0;
            consume = Math.min(this.input.drain(consume / 120, FluidAction.EXECUTE).getAmount() * 120, consume);
            this.output.fill(new FluidStack(FluidRegistry.steam.get().getFluid(), consume / 10), FluidAction.EXECUTE);
            this.level.sendBlockUpdated(this.getBlockPos(), this.level.getBlockState(this.getBlockPos()), this.level.getBlockState(this.getBlockPos()), 3);
        }
    }

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

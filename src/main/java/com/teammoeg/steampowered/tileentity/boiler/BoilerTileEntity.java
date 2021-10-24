package com.teammoeg.steampowered.tileentity.boiler;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.network.ITileSync;
import com.teammoeg.steampowered.network.PacketHandler;
import com.teammoeg.steampowered.network.TileSyncPacket;
import com.teammoeg.steampowered.tileentity.burner.IHeatReceiver;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public abstract class BoilerTileEntity extends TileEntity implements IHeatReceiver, ITickableTileEntity, IHaveGoggleInformation, ITileSync {
    private FluidTank input = new FluidTank(10000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }

        @Override
        protected void onContentsChanged() {
            syncFluidContent();
        }
    };
    private FluidTank output = new FluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            syncFluidContent();
        }
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
    private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> ft);

    public BoilerTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        input.readFromNBT(nbt.getCompound("in"));
        output.readFromNBT(nbt.getCompound("out"));
        heatreceived = nbt.getInt("hu");
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT cnbt = super.serializeNBT();
        cnbt.put("in", input.writeToNBT(new CompoundNBT()));
        cnbt.put("out", output.writeToNBT(new CompoundNBT()));
        cnbt.putInt("hu", heatreceived);
        return cnbt;
    }


    @Override
    public void tick() {
        //debug
        if (this.level.isClientSide()) {
            System.out.println("client input amount: "+input.getFluidAmount());
            System.out.println("client output amount: "+output.getFluidAmount());
            System.out.println("client heat received: "+heatreceived);
        } else {
            System.out.println("server input amount: "+input.getFluidAmount());
            System.out.println("server output amount: "+output.getFluidAmount());
            System.out.println("server heat received: "+heatreceived);
        }
        //debug
        if (this.level != null && !this.level.isClientSide) {
            int consume = Math.min(getHUPerTick(), heatreceived);
            heatreceived = 0;
            consume = Math.min(this.input.drain(consume / 120, FluidAction.EXECUTE).getAmount() * 120, consume);
            this.output.fill(new FluidStack(FluidRegistry.steam.get().getFluid(), consume), FluidAction.EXECUTE);
            this.level.sendBlockUpdated(this.getBlockPos(), this.level.getBlockState(this.getBlockPos()), this.level.getBlockState(this.getBlockPos()), 3);
            this.setChanged();
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

    public void syncFluidContent() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("in", input.writeToNBT(new CompoundNBT()));
        nbt.put("out", output.writeToNBT(new CompoundNBT()));
        PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> {
            return this.level.getChunkAt(this.worldPosition);
        }), new TileSyncPacket(this, nbt));
    }

    public void receiveFromServer(CompoundNBT message) {
        if (message.contains("in", 10)) {
            this.input.readFromNBT(message.getCompound("in"));
        }
        if (message.contains("out", 10)) {
            this.input.readFromNBT(message.getCompound("out"));
        }
    }

    public void receiveFromClient(CompoundNBT message) {

    }

    public BlockPos getSyncPos() {
        return this.getBlockPos();
    }
}

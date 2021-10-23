package com.teammoeg.steampowered.tileentity.boiler;

import java.util.List;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.tileentity.burner.IHeatReceiver;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public abstract class BoilerTileEntity extends TileEntity implements IHeatReceiver,ITickableTileEntity,IHaveGoggleInformation{
    private FluidTank in=new FluidTank(10000) {

		@Override
		public boolean isFluidValid(FluidStack stack) {
			return stack.getFluid()==Fluids.WATER;
		}
    	
    };
    private FluidTank out=new FluidTank(10000);
    private IFluidHandler ft=new IFluidHandler() {
		@Override
		public int getTanks() {
			return 2;
		}

		@Override
		public FluidStack getFluidInTank(int tank) {
			switch(tank) {
			case 0:return in.getFluid();
			case 1:return out.getFluid();
			default:return null;
			}
		}

		@Override
		public int getTankCapacity(int tank) {
			return 10000;
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			if(tank==0&&stack.getFluid()==Fluids.WATER)
				return true;
			return false;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return in.fill(resource, action);
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return out.drain(resource, action);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return out.drain(maxDrain, action);
		}
    };
    int heatreceived;
    private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> ft);
	public BoilerTileEntity(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		in.readFromNBT(nbt.getCompound("in"));
		out.readFromNBT(nbt.getCompound("out"));
		heatreceived=nbt.getInt("hu");
		super.deserializeNBT(nbt);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT cnbt= super.serializeNBT();
		cnbt.put("in",in.writeToNBT(new CompoundNBT()));
		cnbt.put("out",out.writeToNBT(new CompoundNBT()));
		cnbt.putInt("hu",heatreceived);
		return cnbt;
	}


	@Override
	public void tick() {
		int consume=Math.min(getHUPerTick(),heatreceived);
		heatreceived=0;
		consume=Math.min(this.in.drain(consume/12,FluidAction.EXECUTE).getAmount()*12,consume);
		this.out.fill(new FluidStack(FluidRegistry.steam.get().getFluid(),consume),FluidAction.EXECUTE);
		
	}
	@Override
	public void commitHeat(float value) {
		heatreceived=(int) value;
		
	}
    public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
        this.containedFluidTooltip(tooltip, isPlayerSneaking,LazyOptional.of(()->in));
        this.containedFluidTooltip(tooltip, isPlayerSneaking,LazyOptional.of(()->out));
        return true;
    }
	protected abstract int getHUPerTick();
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.holder.isPresent()) {
            this.refreshCapability();
        }
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? holder.cast() : super.getCapability(cap,side);
	}
    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = this.holder;
        this.holder = LazyOptional.of(() -> {
            return this.ft;
        });
        oldCap.invalidate();
    }
}

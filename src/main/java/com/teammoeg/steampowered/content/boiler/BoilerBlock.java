package com.teammoeg.steampowered.content.boiler;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public abstract class BoilerBlock extends Block implements  ILiquidContainer{
    @Override
	public boolean canPlaceLiquid(IBlockReader w, BlockPos p, BlockState s,Fluid f) {
    	TileEntity te=w.getBlockEntity(p);
    	if(te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler=(BoilerTileEntity)te;
			if(boiler.input.fill(new FluidStack(f,1000),FluidAction.SIMULATE)==1000)
				return true;
    	}
    	return false;
	}

	@Override
	public boolean placeLiquid(IWorld w, BlockPos p, BlockState s,
			FluidState f){
		TileEntity te=w.getBlockEntity(p);
		if(te instanceof BoilerTileEntity) {
			BoilerTileEntity boiler=(BoilerTileEntity)te;
			if(boiler.input.fill(new FluidStack(f.getType(),1000),FluidAction.SIMULATE)==1000) {
				boiler.input.fill(new FluidStack(f.getType(),1000),FluidAction.EXECUTE);
				return true;
			}
		}
		return false;
	}

	public BoilerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    public abstract int getHuConsume() ;
    @Override
	public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t,
			ITooltipFlag f) {
    	t.add(new TranslationTextComponent("tooltip.steampowered.boiler.danger").withStyle(TextFormatting.RED));
    	t.add(new TranslationTextComponent("tooltip.steampowered.boiler.huconsume",this.getHuConsume()).withStyle(TextFormatting.GOLD));
    	t.add(new TranslationTextComponent("tooltip.steampowered.boiler.waterconsume",this.getHuConsume()/120).withStyle(TextFormatting.AQUA));
    	t.add(new TranslationTextComponent("tooltip.steampowered.boiler.steamproduce",this.getHuConsume()/10).withStyle(TextFormatting.GOLD));
		super.appendHoverText(i,w,t,f);
	}

	@Override
	public void stepOn(World w, BlockPos bp, Entity e) {
		TileEntity te=w.getBlockEntity(bp);
		if(te instanceof BoilerTileEntity&&e instanceof LivingEntity) {
			if(((BoilerTileEntity) te).lastheat>0||(!((BoilerTileEntity) te).output.isEmpty())) {
				e.hurt(DamageSource.HOT_FLOOR,2);
			}
		}
	}
}

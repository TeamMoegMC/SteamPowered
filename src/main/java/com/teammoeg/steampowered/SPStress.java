package com.teammoeg.steampowered;

import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.foundation.block.BlockStressValues.IStressValueProvider;

import net.minecraft.block.Block;

public class SPStress implements IStressValueProvider {

	@Override
	public double getCapacity(Block arg0) {
		if(!(arg0 instanceof FlywheelBlock))return 0;
		String mat=arg0.getRegistryName().getPath().split("_")[0];
		switch(mat) {
		case "bronze":return SPConfig.COMMON.bronzeFlywheelCapacity.get();
		case "cast":return SPConfig.COMMON.castIronFlywheelCapacity.get();
		case "steel":return SPConfig.COMMON.steelFlywheelCapacity.get();
		}
		return 0;
	}

	@Override
	public double getImpact(Block arg0) {
		if(arg0 instanceof FlywheelBlock)return 0;
		String mat=arg0.getRegistryName().getPath().split("_")[0];
		switch(mat) {
		case "bronze":return SPConfig.COMMON.bronzeCogwheelImpact.get();
		case "cast":return SPConfig.COMMON.castIronCogwheelImpact.get();
		case "steel":return SPConfig.COMMON.steelCogwheelImpact.get();
		}
		return 0;
	}

	@Override
	public boolean hasCapacity(Block arg0) {
		if((arg0 instanceof FlywheelBlock))return true;
		return false;
	}

	@Override
	public boolean hasImpact(Block arg0) {
		if(arg0 instanceof FlywheelBlock)return false;
		return true;
	}

}

package com.teammoeg.steampowered;

import com.simibubi.create.api.stress.BlockStressValues;
import com.teammoeg.steampowered.content.alternator.DynamoBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.DoubleSupplier;

public class SPStress {

	public DoubleSupplier getCapacity(Block arg0) {
		ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(arg0);
		String mat=registryName.getPath().split("_")[0];
		switch(mat) {
		case "bronze":return SPConfig.COMMON.bronzeFlywheelCapacity::get;
		case "cast":return SPConfig.COMMON.castIronFlywheelCapacity::get;
		case "steel":return SPConfig.COMMON.steelFlywheelCapacity::get;
		}
		return null;
	}

	public DoubleSupplier getImpact(Block arg0) {
		ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(arg0);
		if(arg0 instanceof DynamoBlock) {
			return SPConfig.COMMON.dynamoImpact::get;
		}
		String[] mat=registryName.getPath().split("_");
		if(mat[mat.length-1].equals("cogwheel")) {
			switch(mat[0]) {
			case "bronze":return SPConfig.COMMON.bronzeCogwheelImpact::get;
			case "cast":return SPConfig.COMMON.castIronCogwheelImpact::get;
			case "steel":return SPConfig.COMMON.steelCogwheelImpact::get;
			}
		}
		return null;
	}

    public void initStress() {
        BlockStressValues.IMPACTS.registerProvider(this::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(this::getCapacity);
    }

}

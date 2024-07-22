package com.teammoeg.steampowered;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.teammoeg.steampowered.content.alternator.DynamoBlock;

import com.teammoeg.steampowered.oldcreatestuff.OldEngineBlock;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SPStress implements BlockStressValues.IStressValueProvider {

	@Override
	public double getCapacity(Block arg0) {
		ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(arg0);
		if(!(arg0 instanceof OldEngineBlock))return BlockStressDefaults.DEFAULT_CAPACITIES.getOrDefault(registryName,0D);
		String mat=registryName.getPath().split("_")[0];
		switch(mat) {
		case "bronze":return SPConfig.COMMON.bronzeFlywheelCapacity.get();
		case "cast":return SPConfig.COMMON.castIronFlywheelCapacity.get();
		case "steel":return SPConfig.COMMON.steelFlywheelCapacity.get();
		}
		return BlockStressDefaults.DEFAULT_CAPACITIES.getOrDefault(registryName,0D);
	}

	@Override
	public double getImpact(Block arg0) {
		ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(arg0);
		if(arg0 instanceof OldFlywheelBlock)return BlockStressDefaults.DEFAULT_IMPACTS.getOrDefault(registryName,0D);
		if(arg0 instanceof DynamoBlock) {
			return SPConfig.COMMON.dynamoImpact.get();
		}
		String[] mat=registryName.getPath().split("_");
		if(mat[mat.length-1].equals("cogwheel")) {
			switch(mat[0]) {
			case "bronze":return SPConfig.COMMON.bronzeCogwheelImpact.get();
			case "cast":return SPConfig.COMMON.castIronCogwheelImpact.get();
			case "steel":return SPConfig.COMMON.steelCogwheelImpact.get();
			}
		}
		return BlockStressDefaults.DEFAULT_IMPACTS.getOrDefault(registryName,0D);
	}

	@Override
	public boolean hasCapacity(Block arg0) {
		if((arg0 instanceof OldEngineBlock))return true;
		return false;
	}

	@Override
	public Couple<Integer> getGeneratedRPM(Block block) {
		//block = redirectValues(block);
		ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
		Supplier<Couple<Integer>> supplier = BlockStressDefaults.GENERATOR_SPEEDS.get(key);
		if (supplier == null)
			return null;
		return supplier.get();
	}

	@Override
	public boolean hasImpact(Block arg0) {
		if(arg0 instanceof OldEngineBlock)return false;
		return true;
	}

}

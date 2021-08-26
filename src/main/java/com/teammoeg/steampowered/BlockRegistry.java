package com.teammoeg.steampowered;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SteamPowered.MODID);
    public static RegistryObject<FlowingFluidBlock> steamBlock = BLOCKS.register("steam", () -> new FlowingFluidBlock(FluidRegistry.steam, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
}

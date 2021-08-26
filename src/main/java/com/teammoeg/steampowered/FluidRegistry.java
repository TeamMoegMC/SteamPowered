package com.teammoeg.steampowered;

import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidRegistry {
    public static final ResourceLocation STILL_STEAM_TEXTURE = new ResourceLocation(SteamPowered.MODID, "block/steam");
    public static final ResourceLocation FLOWING_STEAM_TEXTURE = new ResourceLocation(SteamPowered.MODID, "block/steam");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SteamPowered.MODID);
    public static RegistryObject<FlowingFluid> steam = FLUIDS.register("steam", () -> new ForgeFlowingFluid.Source(FluidRegistry.PROPERTIES));
    public static RegistryObject<FlowingFluid> steamFlowing = FLUIDS.register("steam_flowing", () -> new ForgeFlowingFluid.Flowing(FluidRegistry.PROPERTIES));
    public static ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(steam, steamFlowing, FluidAttributes.builder(STILL_STEAM_TEXTURE, FLOWING_STEAM_TEXTURE).density(-10).viscosity(1)).bucket(ItemRegistry.pressurizedSteamContainer).block(BlockRegistry.steamBlock).slopeFindDistance(3).explosionResistance(100F);
}

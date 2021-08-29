package com.teammoeg.steampowered;

import com.teammoeg.steampowered.item.GasContainerItem;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SteamPowered.MODID);

    public static RegistryObject<Item> pressurizedGasContainer = ITEMS.register("pressurized_gas_container", () -> new GasContainerItem(Fluids.EMPTY, (new Item.Properties()).stacksTo(16).tab(SteamPowered.itemGroup)));
    public static RegistryObject<Item> pressurizedSteamContainer = ITEMS.register("pressurized_steam_container", () -> new GasContainerItem(FluidRegistry.steam, new Item.Properties().stacksTo(1).tab(SteamPowered.itemGroup).craftRemainder(ItemRegistry.pressurizedGasContainer.get())));
}

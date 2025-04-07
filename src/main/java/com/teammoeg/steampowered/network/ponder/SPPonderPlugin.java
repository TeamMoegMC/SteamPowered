package com.teammoeg.steampowered.network.ponder;

import com.simibubi.create.foundation.ponder.PonderWorldBlockEntityFix;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.registrate.SPBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;

public class SPPonderPlugin implements PonderPlugin {

    public static final ResourceLocation STEAM = SteamPowered.rl("steam");

    @Override
    public String getModId() {
        return SteamPowered.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(SPBlocks.BRONZE_STEAM_ENGINE, SPBlocks.CAST_IRON_STEAM_ENGINE, SPBlocks.STEEL_STEAM_ENGINE)
                .addStoryBoard("steam_engine", SPScenes::steamEngine, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(SPBlocks.BRONZE_STEAM_ENGINE, SPBlocks.CAST_IRON_STEAM_ENGINE, SPBlocks.STEEL_STEAM_ENGINE)
                .addStoryBoard("boiler", SPScenes::steamBoiler, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(SPBlocks.BRONZE_BOILER, SPBlocks.BRONZE_BURNER, SPBlocks.CAST_IRON_BURNER, SPBlocks.CAST_IRON_BOILER, SPBlocks.STEEL_BURNER, SPBlocks.STEEL_BOILER)
                .addStoryBoard("boiler", SPScenes::steamBoiler, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(SPBlocks.BRONZE_FLYWHEEL, SPBlocks.CAST_IRON_FLYWHEEL, SPBlocks.STEEL_FLYWHEEL)
                .addStoryBoard("steam_engine", SPScenes::steamFlywheel, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(SPBlocks.DYNAMO)
                .addStoryBoard("dynamo", SPScenes::dynamo, AllCreatePonderTags.KINETIC_APPLIANCES, STEAM);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.registerTag(STEAM)
                .addToIndex()
                .item(SPBlocks.BRONZE_STEAM_ENGINE.get(), true, false)
                .title("Steam")
                .description("Components related to steam production and usage")
                .register();
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {

    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {
        PonderWorldBlockEntityFix.fixControllerBlockEntities(ponderLevel);
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {

    }
}

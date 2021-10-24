package com.teammoeg.steampowered.ponder;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.content.KineticsScenes;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.registrate.SPBlocks;
import net.minecraft.util.ResourceLocation;

public class SPPonderIndex {
    static final PonderRegistrationHelper CREATE_HELPER = new PonderRegistrationHelper(Create.ID);
    static final PonderRegistrationHelper STEAM_HELPER = new PonderRegistrationHelper(SteamPowered.MODID);

    public static final PonderTag STEAM = new PonderTag(new ResourceLocation(SteamPowered.MODID, "steam")).item(SPBlocks.BRONZE_STEAM_ENGINE.get(), true, false)
            .defaultLang("Steam", "Components related to steam production and usage");

    public static void register() {
        CREATE_HELPER.forComponents(SPBlocks.BRONZE_COGWHEEL, SPBlocks.CAST_IRON_COGWHEEL, SPBlocks.STEEL_COGWHEEL)
                .addStoryBoard(new ResourceLocation("create", "cog/small"), KineticsScenes::cogAsRelay, PonderTag.KINETIC_RELAYS)
                .addStoryBoard(new ResourceLocation("create", "cog/speedup"), KineticsScenes::cogsSpeedUp);

        CREATE_HELPER.forComponents(SPBlocks.BRONZE_LARGE_COGWHEEL, SPBlocks.CAST_IRON_LARGE_COGWHEEL, SPBlocks.STEEL_LARGE_COGWHEEL)
                .addStoryBoard(new ResourceLocation("create", "cog/speedup"), KineticsScenes::cogsSpeedUp)
                .addStoryBoard(new ResourceLocation("create", "cog/large"), KineticsScenes::largeCogAsRelay, PonderTag.KINETIC_RELAYS);

        STEAM_HELPER.forComponents(SPBlocks.BRONZE_STEAM_ENGINE, SPBlocks.CAST_IRON_STEAM_ENGINE, SPBlocks.STEEL_STEAM_ENGINE)
                .addStoryBoard("steam_engine", SPScenes::steamEngine, PonderTag.KINETIC_SOURCES, STEAM);

        STEAM_HELPER.forComponents(SPBlocks.BRONZE_STEAM_ENGINE, SPBlocks.CAST_IRON_STEAM_ENGINE, SPBlocks.STEEL_STEAM_ENGINE)
                .addStoryBoard("boiler", SPScenes::steamBoiler, PonderTag.KINETIC_SOURCES, STEAM);

        STEAM_HELPER.forComponents(SPBlocks.BRONZE_BOILER, SPBlocks.BRONZE_BURNER, SPBlocks.CAST_IRON_BURNER, SPBlocks.CAST_IRON_BOILER, SPBlocks.STEEL_BURNER, SPBlocks.STEEL_BOILER)
                .addStoryBoard("boiler", SPScenes::steamBoiler, PonderTag.KINETIC_SOURCES, STEAM);

        STEAM_HELPER.forComponents(SPBlocks.BRONZE_FLYWHEEL, SPBlocks.CAST_IRON_FLYWHEEL, SPBlocks.STEEL_FLYWHEEL)
                .addStoryBoard("steam_engine", SPScenes::steamFlywheel, PonderTag.KINETIC_SOURCES, STEAM);

        STEAM_HELPER.forComponents(SPBlocks.ALTERNATOR)
                .addStoryBoard("alternator", SPScenes::alternator, PonderTag.KINETIC_APPLIANCES, STEAM);

    }
}

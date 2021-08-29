package com.teammoeg.steampowered.registrate;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.content.KineticsScenes;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.ponder.SPScenes;
import net.minecraft.util.ResourceLocation;

public class SPPonderIndex {
    static final PonderRegistrationHelper CREATE_HELPER = new PonderRegistrationHelper(Create.ID);
    static final PonderRegistrationHelper STEAM_HELPER = new PonderRegistrationHelper(SteamPowered.MODID);
    public static void register() {
        CREATE_HELPER.forComponents(SPBlocks.BRONZE_COGWHEEL, SPBlocks.CAST_IRON_COGWHEEL, SPBlocks.STEEL_COGWHEEL)
                .addStoryBoard(new ResourceLocation("create", "cog/small"), KineticsScenes::cogAsRelay, PonderTag.KINETIC_RELAYS)
                .addStoryBoard(new ResourceLocation("create", "cog/speedup"), KineticsScenes::cogsSpeedUp);

        CREATE_HELPER.forComponents(SPBlocks.BRONZE_LARGE_COGWHEEL, SPBlocks.CAST_IRON_LARGE_COGWHEEL, SPBlocks.STEEL_LARGE_COGWHEEL)
                .addStoryBoard(new ResourceLocation("create", "cog/speedup"), KineticsScenes::cogsSpeedUp)
                .addStoryBoard(new ResourceLocation("create", "cog/large"), KineticsScenes::largeCogAsRelay, PonderTag.KINETIC_RELAYS);

        STEAM_HELPER.forComponents(SPBlocks.STEAM_ENGINE)
                .addStoryBoard("steam_engine", SPScenes::steamEngine);
    }
}

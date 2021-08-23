package com.teammoeg.steampowered.create;

import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.teammoeg.steampowered.SteamPowered;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class SPBlocks {
    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final BlockEntry<SteamEngineBlock> STEAM_ENGINE = REGISTRATE.block("steam_engine",  SteamEngineBlock::new)
            .initialProperties(SharedProperties::stone)
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
        Create.registrate().addToSection(STEAM_ENGINE, AllSections.KINETICS);
    }
}

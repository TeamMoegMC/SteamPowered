package com.teammoeg.steampowered.oldcreatestuff;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.registrate.SPBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class OldRegistration {
    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate
            .creativeModeTab(() -> SteamPowered.itemGroup);


    public static final BlockEntityEntry<OldFlywheelBlockEntity> OLD_FLYWHEEL_BE = REGISTRATE
            .blockEntity("bronze_burner", OldFlywheelBlockEntity::new)
            .validBlocks(SPBlocks.BRONZE_FLYWHEEL, SPBlocks.STEEL_FLYWHEEL, SPBlocks.CAST_IRON_FLYWHEEL)
            .register();
}

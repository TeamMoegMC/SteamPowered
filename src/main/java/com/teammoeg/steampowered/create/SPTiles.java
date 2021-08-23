package com.teammoeg.steampowered.create;

import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;
import com.teammoeg.steampowered.SteamPowered;

public class SPTiles {
    public static final TileEntityEntry<SteamEngineTileEntity> STEAM_ENGINE = SteamPowered.registrate.get()
            .tileEntity("steam_engine", SteamEngineTileEntity::new)
            .validBlocks(SPBlocks.STEAM_ENGINE)
            .register();

    public static void register() {}
}

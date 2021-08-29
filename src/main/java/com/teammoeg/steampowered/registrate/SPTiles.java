package com.teammoeg.steampowered.registrate;

import com.simibubi.create.content.contraptions.base.HalfShaftInstance;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.client.render.AlternatorRenderer;
import com.teammoeg.steampowered.tileentity.AlternatorTileEntity;
import com.teammoeg.steampowered.tileentity.MetalCogwheelTileEntity;
import com.teammoeg.steampowered.tileentity.SteamEngineTileEntity;

public class SPTiles {
    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final TileEntityEntry<SteamEngineTileEntity> STEAM_ENGINE = REGISTRATE
            .tileEntity("steam_engine", SteamEngineTileEntity::new)
            .validBlocks(SPBlocks.STEAM_ENGINE)
            .register();

    public static final TileEntityEntry<MetalCogwheelTileEntity> METAL_COGWHEEL = REGISTRATE
            .tileEntity("metal_cogwheel", MetalCogwheelTileEntity::new)
            .instance(() -> SingleRotatingInstance::new)
            .validBlocks(SPBlocks.STEEL_COGWHEEL, SPBlocks.STEEL_LARGE_COGWHEEL, SPBlocks.CAST_IRON_COGWHEEL, SPBlocks.CAST_IRON_LARGE_COGWHEEL, SPBlocks.BRONZE_COGWHEEL, SPBlocks.BRONZE_LARGE_COGWHEEL)
            .renderer(() -> KineticTileEntityRenderer::new)
            .register();

    public static final TileEntityEntry<AlternatorTileEntity> ALTERNATOR = REGISTRATE
            .tileEntity("alternator", AlternatorTileEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(SPBlocks.ALTERNATOR)
            .renderer(() -> AlternatorRenderer::new)
            .register();

    public static void register() {}
}

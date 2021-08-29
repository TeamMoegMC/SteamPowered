package com.teammoeg.steampowered.registrate;

import com.simibubi.create.content.contraptions.base.HalfShaftInstance;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.content.contraptions.components.flywheel.FlyWheelInstance;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelRenderer;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.client.render.AlternatorRenderer;
import com.teammoeg.steampowered.client.render.BronzeFlywheelRenderer;
import com.teammoeg.steampowered.instance.BronzeFlywheelInstance;
import com.teammoeg.steampowered.tileentity.*;

public class SPTiles {
    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final TileEntityEntry<BronzeSteamEngineTileEntity> BRONZE_STEAM_ENGINE = REGISTRATE
            .tileEntity("bronze_steam_engine", BronzeSteamEngineTileEntity::new)
            .validBlocks(SPBlocks.BRONZE_STEAM_ENGINE)
            .register();

    public static final TileEntityEntry<CastIronSteamEngineTileEntity> CAST_IRON_STEAM_ENGINE = REGISTRATE
            .tileEntity("cast_iron_steam_engine", CastIronSteamEngineTileEntity::new)
            .validBlocks(SPBlocks.CAST_IRON_STEAM_ENGINE)
            .register();

    public static final TileEntityEntry<SteelSteamEngineTileEntity> STEEL_STEAM_ENGINE = REGISTRATE
            .tileEntity("steel_steam_engine", SteelSteamEngineTileEntity::new)
            .validBlocks(SPBlocks.STEEL_STEAM_ENGINE)
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

    public static final TileEntityEntry<FlywheelTileEntity> STEAM_FLYWHEEL = REGISTRATE
            .tileEntity("steam_flywheel", FlywheelTileEntity::new)
            .instance(() -> BronzeFlywheelInstance::new)
            .validBlocks(SPBlocks.BRONZE_FLYWHEEL, SPBlocks.CAST_IRON_FLYWHEEL, SPBlocks.STEEL_FLYWHEEL)
            .renderer(() -> BronzeFlywheelRenderer::new)
            .register();

    public static void register() {
    }
}

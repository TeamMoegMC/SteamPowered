/*
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Steam Powered.
 *
 * Steam Powered is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Steam Powered is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Steam Powered. If not, see <https://www.gnu.org/licenses/>.
 */

package com.teammoeg.steampowered.registrate;

import com.simibubi.create.content.contraptions.base.HalfShaftInstance;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.client.instance.BronzeFlywheelInstance;
import com.teammoeg.steampowered.client.instance.CastIronFlywheelInstance;
import com.teammoeg.steampowered.client.instance.SteelFlywheelInstance;
import com.teammoeg.steampowered.client.render.BronzeFlywheelRenderer;
import com.teammoeg.steampowered.client.render.CastIronFlywheelRenderer;
import com.teammoeg.steampowered.client.render.SteelFlywheelRenderer;
import com.teammoeg.steampowered.content.alternator.DynamoTileEntity;
import com.teammoeg.steampowered.content.boiler.CastIronBoilerTileEntity;
import com.teammoeg.steampowered.content.boiler.SteelBoilerTileEntity;
import com.teammoeg.steampowered.content.burner.CastIronBurnerTileEntity;
import com.teammoeg.steampowered.content.burner.SteelBurnerTileEntity;
import com.teammoeg.steampowered.content.cogwheel.MetalCogwheelTileEntity;
import com.teammoeg.steampowered.content.boiler.BronzeBoilerTileEntity;
import com.teammoeg.steampowered.content.burner.BronzeBurnerTileEntity;
import com.teammoeg.steampowered.content.engine.BronzeSteamEngineTileEntity;
import com.teammoeg.steampowered.content.engine.CastIronSteamEngineTileEntity;
import com.teammoeg.steampowered.content.engine.SteelSteamEngineTileEntity;

public class SPTiles {
    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final TileEntityEntry<BronzeBurnerTileEntity> BRONZE_BURNER = REGISTRATE
            .tileEntity("bronze_burner", BronzeBurnerTileEntity::new)
            .validBlocks(SPBlocks.BRONZE_BURNER)
            .register();

    public static final TileEntityEntry<CastIronBurnerTileEntity> CAST_IRON_BURNER = REGISTRATE
            .tileEntity("cast_iron_burner", CastIronBurnerTileEntity::new)
            .validBlocks(SPBlocks.CAST_IRON_BURNER)
            .register();

    public static final TileEntityEntry<SteelBurnerTileEntity> STEEL_BURNER = REGISTRATE
            .tileEntity("steel_burner", SteelBurnerTileEntity::new)
            .validBlocks(SPBlocks.STEEL_BURNER)
            .register();

    public static final TileEntityEntry<BronzeBoilerTileEntity> BRONZE_BOILER = REGISTRATE
            .tileEntity("bronze_boiler", BronzeBoilerTileEntity::new)
            .validBlocks(SPBlocks.BRONZE_BOILER)
            .register();

    public static final TileEntityEntry<CastIronBoilerTileEntity> CAST_IRON_BOILER = REGISTRATE
            .tileEntity("cast_iron_boiler", CastIronBoilerTileEntity::new)
            .validBlocks(SPBlocks.CAST_IRON_BOILER)
            .register();

    public static final TileEntityEntry<SteelBoilerTileEntity> STEEL_BOILER = REGISTRATE
            .tileEntity("steel_boiler", SteelBoilerTileEntity::new)
            .validBlocks(SPBlocks.STEEL_BOILER)
            .register();

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

    public static final TileEntityEntry<DynamoTileEntity> DYNAMO = REGISTRATE
            .tileEntity("alternator", DynamoTileEntity::new)
            .instance(() -> com.teammoeg.steampowered.content.alternator.RevertHalfShaftInstance::new)
            .validBlocks(SPBlocks.DYNAMO)
            .register();

    public static final TileEntityEntry<FlywheelTileEntity> BRONZE_STEAM_FLYWHEEL = REGISTRATE
            .tileEntity("bronze_steam_flywheel", FlywheelTileEntity::new)
            .instance(() -> BronzeFlywheelInstance::new)
            .validBlocks(SPBlocks.BRONZE_FLYWHEEL)
            .renderer(() -> BronzeFlywheelRenderer::new)
            .register();

    public static final TileEntityEntry<FlywheelTileEntity> CAST_IRON_STEAM_FLYWHEEL = REGISTRATE
            .tileEntity("cast_iron_steam_flywheel", FlywheelTileEntity::new)
            .instance(() -> CastIronFlywheelInstance::new)
            .validBlocks(SPBlocks.CAST_IRON_FLYWHEEL)
            .renderer(() -> CastIronFlywheelRenderer::new)
            .register();

    public static final TileEntityEntry<FlywheelTileEntity> STEEL_STEAM_FLYWHEEL = REGISTRATE
            .tileEntity("steel_steam_flywheel", FlywheelTileEntity::new)
            .instance(() -> SteelFlywheelInstance::new)
            .validBlocks(SPBlocks.STEEL_FLYWHEEL)
            .renderer(() -> SteelFlywheelRenderer::new)
            .register();

    public static void register() {
    }
}

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

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.teammoeg.steampowered.client.instance.BronzeFlywheelInstance;
import com.teammoeg.steampowered.client.instance.CastIronFlywheelInstance;
import com.teammoeg.steampowered.client.instance.SteelFlywheelInstance;
import com.teammoeg.steampowered.client.render.BronzeFlywheelRenderer;
import com.teammoeg.steampowered.client.render.CastIronFlywheelRenderer;
import com.teammoeg.steampowered.client.render.SteelFlywheelRenderer;
import com.teammoeg.steampowered.content.alternator.DynamoBlockEntity;
import com.teammoeg.steampowered.content.boiler.BronzeBoilerBlockEntity;
import com.teammoeg.steampowered.content.boiler.CastIronBoilerBlockEntity;
import com.teammoeg.steampowered.content.boiler.SteelBoilerBlockEntity;
import com.teammoeg.steampowered.content.burner.BronzeBurnerBlockEntity;
import com.teammoeg.steampowered.content.burner.CastIronBurnerBlockEntity;
import com.teammoeg.steampowered.content.burner.SteelBurnerBlockEntity;
import com.teammoeg.steampowered.content.cogwheel.MetalCogwheelBlockEntity;
import com.teammoeg.steampowered.content.engine.BronzeSteamEngineTileEntity;
import com.teammoeg.steampowered.content.engine.CastIronSteamEngineTileEntity;
import com.teammoeg.steampowered.content.engine.SteelSteamEngineTileEntity;
import com.teammoeg.steampowered.content.flywheel.SteamFlywheelTileEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.teammoeg.steampowered.SteamPowered.REGISTRATE;


public class SPBlockEntities {
    static {
        REGISTRATE.setCreativeTab(SPTabs.SP_BASE_TAB);
    }

    public static final BlockEntityEntry<BronzeBurnerBlockEntity> BRONZE_BURNER = REGISTRATE
            .blockEntity("bronze_burner", BronzeBurnerBlockEntity::new)
            .validBlocks(SPBlocks.BRONZE_BURNER)
            .register();

    public static final BlockEntityEntry<CastIronBurnerBlockEntity> CAST_IRON_BURNER = REGISTRATE
            .blockEntity("cast_iron_burner", CastIronBurnerBlockEntity::new)
            .validBlocks(SPBlocks.CAST_IRON_BURNER)
            .register();

    public static final BlockEntityEntry<SteelBurnerBlockEntity> STEEL_BURNER = REGISTRATE
            .blockEntity("steel_burner", SteelBurnerBlockEntity::new)
            .validBlocks(SPBlocks.STEEL_BURNER)
            .register();

    public static final BlockEntityEntry<BronzeBoilerBlockEntity> BRONZE_BOILER = REGISTRATE
            .blockEntity("bronze_boiler", BronzeBoilerBlockEntity::new)
            .validBlocks(SPBlocks.BRONZE_BOILER)
            .register();

    public static final BlockEntityEntry<CastIronBoilerBlockEntity> CAST_IRON_BOILER = REGISTRATE
            .blockEntity("cast_iron_boiler", CastIronBoilerBlockEntity::new)
            .validBlocks(SPBlocks.CAST_IRON_BOILER)
            .register();

    public static final BlockEntityEntry<SteelBoilerBlockEntity> STEEL_BOILER = REGISTRATE
            .blockEntity("steel_boiler", SteelBoilerBlockEntity::new)
            .validBlocks(SPBlocks.STEEL_BOILER)
            .register();

    public static final BlockEntityEntry<BronzeSteamEngineTileEntity> BRONZE_STEAM_ENGINE = REGISTRATE
            .blockEntity("bronze_steam_engine", BronzeSteamEngineTileEntity::new)
            .validBlocks(SPBlocks.BRONZE_STEAM_ENGINE)
            .register();

    public static final BlockEntityEntry<CastIronSteamEngineTileEntity> CAST_IRON_STEAM_ENGINE = REGISTRATE
            .blockEntity("cast_iron_steam_engine", CastIronSteamEngineTileEntity::new)
            .validBlocks(SPBlocks.CAST_IRON_STEAM_ENGINE)
            .register();

    public static final BlockEntityEntry<SteelSteamEngineTileEntity> STEEL_STEAM_ENGINE = REGISTRATE
            .blockEntity("steel_steam_engine", SteelSteamEngineTileEntity::new)
            .validBlocks(SPBlocks.STEEL_STEAM_ENGINE)
            .register();

    public static final BlockEntityEntry<MetalCogwheelBlockEntity> METAL_COGWHEEL = REGISTRATE
            .blockEntity("metal_cogwheel", MetalCogwheelBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::shaft)
            .validBlocks(SPBlocks.STEEL_COGWHEEL, SPBlocks.STEEL_LARGE_COGWHEEL, SPBlocks.CAST_IRON_COGWHEEL, SPBlocks.CAST_IRON_LARGE_COGWHEEL, SPBlocks.BRONZE_COGWHEEL, SPBlocks.BRONZE_LARGE_COGWHEEL)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<DynamoBlockEntity> DYNAMO = REGISTRATE
            .blockEntity("alternator", DynamoBlockEntity::new)
            .visual(() -> com.teammoeg.steampowered.content.alternator.DynamoShaftInstance::new)
            .validBlocks(SPBlocks.DYNAMO)
            .register();

    public static final BlockEntityEntry<SteamFlywheelTileEntity> BRONZE_STEAM_FLYWHEEL = REGISTRATE
            .blockEntity("bronze_steam_flywheel", SteamFlywheelTileEntity::new)
            .visual(() -> BronzeFlywheelInstance::new)
            .validBlocks(SPBlocks.BRONZE_FLYWHEEL)
            .renderer(() -> BronzeFlywheelRenderer::new)
            .register();

    public static final BlockEntityEntry<SteamFlywheelTileEntity> CAST_IRON_STEAM_FLYWHEEL = REGISTRATE
            .blockEntity("cast_iron_steam_flywheel", SteamFlywheelTileEntity::new)
            .visual(() -> CastIronFlywheelInstance::new)
            .validBlocks(SPBlocks.CAST_IRON_FLYWHEEL)
            .renderer(() -> CastIronFlywheelRenderer::new)
            .register();

    public static final BlockEntityEntry<SteamFlywheelTileEntity> STEEL_STEAM_FLYWHEEL = REGISTRATE
            .blockEntity("steel_steam_flywheel", SteamFlywheelTileEntity::new)
            .visual(() -> SteelFlywheelInstance::new)
            .validBlocks(SPBlocks.STEEL_FLYWHEEL)
            .renderer(() -> SteelFlywheelRenderer::new)
            .register();

    public static void register() {
    }
}

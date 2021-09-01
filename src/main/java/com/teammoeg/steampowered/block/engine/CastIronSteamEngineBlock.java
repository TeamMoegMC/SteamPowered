package com.teammoeg.steampowered.block.engine;

import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.registrate.SPTiles;
import com.teammoeg.steampowered.tileentity.engine.CastIronSteamEngineTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CastIronSteamEngineBlock extends SteamEngineBlock implements ITE<CastIronSteamEngineTileEntity> {
    public CastIronSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.CAST_IRON_STEAM_ENGINE.create();
    }

    @Override
    public Class<CastIronSteamEngineTileEntity> getTileEntityClass() {
        return CastIronSteamEngineTileEntity.class;
    }
}

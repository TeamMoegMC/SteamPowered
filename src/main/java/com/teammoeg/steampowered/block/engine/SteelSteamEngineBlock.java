package com.teammoeg.steampowered.block.engine;

import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.registrate.SPTiles;
import com.teammoeg.steampowered.tileentity.engine.SteelSteamEngineTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SteelSteamEngineBlock extends SteamEngineBlock implements ITE<SteelSteamEngineTileEntity> {
    public SteelSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.STEEL_STEAM_ENGINE.create();
    }

    @Override
    public Class<SteelSteamEngineTileEntity> getTileEntityClass() {
        return SteelSteamEngineTileEntity.class;
    }
}

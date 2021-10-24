package com.teammoeg.steampowered.content.engine;

import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BronzeSteamEngineBlock extends SteamEngineBlock implements ITE<BronzeSteamEngineTileEntity> {
    public BronzeSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.BRONZE_STEAM_ENGINE.create();
    }

    @Override
    public Class<BronzeSteamEngineTileEntity> getTileEntityClass() {
        return BronzeSteamEngineTileEntity.class;
    }
}

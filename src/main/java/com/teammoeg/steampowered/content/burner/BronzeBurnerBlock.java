package com.teammoeg.steampowered.content.burner;

import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BronzeBurnerBlock extends BurnerBlock {
    public BronzeBurnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.BRONZE_BURNER.create();
    }
}

package com.teammoeg.steampowered.content.burner;

import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CastIronBurnerBlock extends BurnerBlock {
    public CastIronBurnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.CAST_IRON_BURNER.create();
    }

	@Override
	public int getHuProduce() {
		return 240;
	}
}

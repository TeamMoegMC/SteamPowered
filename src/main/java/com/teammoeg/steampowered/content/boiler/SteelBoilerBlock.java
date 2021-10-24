package com.teammoeg.steampowered.content.boiler;

import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SteelBoilerBlock extends BoilerBlock {
    public SteelBoilerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.STEEL_BOILER.create();
    }

	@Override
	public int getHuConsume() {
		return 480;
	}
}

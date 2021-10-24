package com.teammoeg.steampowered.content.boiler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class BoilerBlock extends Block {
    public BoilerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}

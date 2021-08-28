package com.teammoeg.steampowered.create;

import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class MetalCogwheelBlock extends CogWheelBlock {
    protected MetalCogwheelBlock(boolean large, Properties properties) {
        super(large, properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.METAL_COGWHEEL.create();
    }

    public static MetalCogwheelBlock small(Properties properties) {
        return new MetalCogwheelBlock(false, properties);
    }

    public static MetalCogwheelBlock large(Properties properties) {
        return new MetalCogwheelBlock(true, properties);
    }
}

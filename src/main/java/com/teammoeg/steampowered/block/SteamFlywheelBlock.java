package com.teammoeg.steampowered.block;

import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SteamFlywheelBlock extends FlywheelBlock {
    public SteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.STEAM_FLYWHEEL.create();
    }
}

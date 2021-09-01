package com.teammoeg.steampowered.block.flywheel;

import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CastIronSteamFlywheelBlock extends FlywheelBlock {
    public CastIronSteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.CAST_IRON_STEAM_FLYWHEEL.create();
    }
}

package com.teammoeg.steampowered.tileentity.engine;

import com.teammoeg.steampowered.registrate.SPBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

public class CastIronSteamEngineTileEntity extends SteamEngineTileEntity {
    public CastIronSteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
    }

    @Override
    public Block getFlywheel() {
        return SPBlocks.CAST_IRON_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return 64F;
    }

    @Override
    public float getGeneratingSpeed() {
        return 32F;
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return 48;
    }

    @Override
    public int getSteamStorage() {
        return 64000;
    }
}

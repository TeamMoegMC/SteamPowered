package com.teammoeg.steampowered.content.engine;

import com.teammoeg.steampowered.SPConfig;
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
        return SPConfig.COMMON.castIronFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return SPConfig.COMMON.castIronFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return SPConfig.COMMON.castIronFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return SPConfig.COMMON.castIronFlywheelSteamStorage.get();
    }
}

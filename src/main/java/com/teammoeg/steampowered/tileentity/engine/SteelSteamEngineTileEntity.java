package com.teammoeg.steampowered.tileentity.engine;

import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.registrate.SPBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

public class SteelSteamEngineTileEntity extends SteamEngineTileEntity {
    public SteelSteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
    }

    @Override
    public Block getFlywheel() {
        return SPBlocks.STEEL_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return SPConfig.COMMON.steelFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return SPConfig.COMMON.steelFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return SPConfig.COMMON.steelFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return SPConfig.COMMON.steelFlywheelSteamStorage.get();
    }
}

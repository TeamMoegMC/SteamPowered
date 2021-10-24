package com.teammoeg.steampowered.content.engine;

import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.registrate.SPBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

public class BronzeSteamEngineTileEntity extends SteamEngineTileEntity {
    public BronzeSteamEngineTileEntity(TileEntityType<? extends SteamEngineTileEntity> type) {
        super(type);
    }

    @Override
    public Block getFlywheel() {
        return SPBlocks.BRONZE_FLYWHEEL.get();
    }

    @Override
    public float getGeneratingCapacity() {
        return SPConfig.COMMON.bronzeFlywheelCapacity.get();
    }

    @Override
    public float getGeneratingSpeed() {
        return SPConfig.COMMON.bronzeFlywheelSpeed.get();
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return SPConfig.COMMON.bronzeFlywheelSteamConsumptionPerTick.get();
    }

    @Override
    public int getSteamStorage() {
        return SPConfig.COMMON.bronzeFlywheelSteamStorage.get();
    }
}

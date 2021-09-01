package com.teammoeg.steampowered.tileentity.engine;

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
        return 32F;
    }

    @Override
    public float getGeneratingSpeed() {
        return 32F;
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return 16;
    }

    @Override
    public int getSteamStorage() {
        return 32000;
    }
}

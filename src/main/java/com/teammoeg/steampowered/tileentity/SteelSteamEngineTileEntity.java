package com.teammoeg.steampowered.tileentity;

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
        return 96F;
    }

    @Override
    public float getGeneratingSpeed() {
        return 32F;
    }

    @Override
    public int getSteamConsumptionPerTick() {
        return 96;
    }

    @Override
    public int getSteamStorage() {
        return 96000;
    }
}

package com.teammoeg.steampowered.content.burner;

import net.minecraft.tileentity.TileEntityType;

public class CastIronBurnerTileEntity extends BurnerTileEntity {

    public CastIronBurnerTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    protected int getHuPerTick() {
        return 240;
    }
}

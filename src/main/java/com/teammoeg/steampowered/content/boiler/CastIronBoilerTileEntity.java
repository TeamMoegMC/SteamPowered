package com.teammoeg.steampowered.content.boiler;

import net.minecraft.tileentity.TileEntityType;

public class CastIronBoilerTileEntity extends BoilerTileEntity {

    public CastIronBoilerTileEntity(TileEntityType<?> type) {
        super(type);
    }

    protected int getHUPerTick() {
        return 240;
    }

}

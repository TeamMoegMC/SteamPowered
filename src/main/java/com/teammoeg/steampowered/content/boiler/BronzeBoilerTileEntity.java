package com.teammoeg.steampowered.content.boiler;

import net.minecraft.tileentity.TileEntityType;

public class BronzeBoilerTileEntity extends BoilerTileEntity {

    public BronzeBoilerTileEntity(TileEntityType<?> type) {
        super(type);
    }

    protected int getHUPerTick() {
        return 120;
    }

}

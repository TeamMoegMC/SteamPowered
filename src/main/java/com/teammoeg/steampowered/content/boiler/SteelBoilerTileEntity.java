package com.teammoeg.steampowered.content.boiler;

import net.minecraft.tileentity.TileEntityType;

public class SteelBoilerTileEntity extends BoilerTileEntity {

    public SteelBoilerTileEntity(TileEntityType<?> type) {
        super(type);
    }

    protected int getHUPerTick() {
        return 480;
    }

}

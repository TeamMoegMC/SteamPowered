package com.teammoeg.steampowered.content.burner;

import net.minecraft.tileentity.TileEntityType;

public class SteelBurnerTileEntity extends BurnerTileEntity {

    public SteelBurnerTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    protected int getHuPerTick() {
        return 480;
    }
}

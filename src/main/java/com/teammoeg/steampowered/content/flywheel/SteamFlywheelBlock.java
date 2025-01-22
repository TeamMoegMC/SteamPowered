package com.teammoeg.steampowered.content.flywheel;

import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteamFlywheelBlock extends OldFlywheelBlock {
    public SteamFlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends SteamFlywheelTileEntity> getBlockEntityType() {
        return null;
    }
}

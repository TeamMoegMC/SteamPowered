package com.teammoeg.steampowered.content.flywheel;

import com.teammoeg.steampowered.content.engine.SteamEngineTileEntity;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteamFlywheelTileEntity extends OldFlywheelBlockEntity {

    public SteamFlywheelTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void lazyTick() {
        super.lazyTick();
        var state = getBlockState();

        if (!SteamFlywheelBlock.isConnected(state))
            return;

        var facing = SteamFlywheelBlock.getConnection(state);
        if (facing == null)
            return;

        var enginePos = this.worldPosition.relative(facing, 2);
        var engine = this.level.getBlockEntity(enginePos);
        if (engine instanceof SteamEngineTileEntity)
            return;

        clearState();
    }

    public void clearState() {
        setRotation(0.0F, 0.0F);
        SteamFlywheelBlock.setConnection(this.level, worldPosition, getBlockState(), null);
    }
}

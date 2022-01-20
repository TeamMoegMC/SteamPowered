package com.teammoeg.steampowered.content.alternator;

import com.jozufozu.flywheel.backend.material.MaterialManager;
import com.simibubi.create.content.contraptions.base.HalfShaftInstance;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.util.Direction;

public class DynamoShaftInstance extends HalfShaftInstance {

	public DynamoShaftInstance(MaterialManager<?> modelManager, KineticTileEntity tile) {
		super(modelManager, tile);
	}
	@Override
    protected Direction getShaftDirection() {
        return blockState.getValue(DynamoBlock.FACING).getOpposite();
    }

}

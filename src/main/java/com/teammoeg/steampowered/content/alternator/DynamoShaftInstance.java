package com.teammoeg.steampowered.content.alternator;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import net.minecraft.core.Direction;

public class DynamoShaftInstance extends HalfShaftInstance<DynamoBlockEntity> {

	public DynamoShaftInstance(MaterialManager modelManager, DynamoBlockEntity tile) {
		super(modelManager, tile);
	}
	@Override
    protected Direction getShaftDirection() {
        return blockState.getValue(DynamoBlock.FACING).getOpposite();
    }

}

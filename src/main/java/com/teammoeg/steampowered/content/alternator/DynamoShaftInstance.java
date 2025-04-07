package com.teammoeg.steampowered.content.alternator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class DynamoShaftInstance extends SingleAxisRotatingVisual<DynamoBlockEntity> {

	public DynamoShaftInstance(VisualizationContext modelManager, DynamoBlockEntity tile, float d) {
		super(modelManager, tile, d, Models.partial(AllPartialModels.SHAFT, Direction.UP));
	}

	protected static Direction getShaftDirection(BlockState blockState) {
		return blockState.getValue(DynamoBlock.FACING).getOpposite();
	}

}

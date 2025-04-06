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
	public int getFlickerScore() {
		return 0;
	}
	public void applyNewSpeed(float prevSpeed, float speed) {

		// Speed changed to 0
		if (speed == 0) {
			if (hasSource()) {
				notifyStressCapacityChange(0);
				getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
				return;
			}
			detachKinetics();
			setSpeed(0);
			setNetwork(null);
			return;
		}

		// Now turning - create a new Network
		if (prevSpeed == 0) {
			setSpeed(speed);
			setNetwork(createNetworkId());
			attachKinetics();
			return;
		}

		// Change speed when overpowered by other generator
		if (hasSource()) {

			// Staying below Overpowered speed
			if (Math.abs(prevSpeed) >= Math.abs(speed)) {
				if (Math.signum(prevSpeed) != Math.signum(speed)&&speed!=0&&prevSpeed!=0){
					level.destroyBlock(worldPosition, true);
					System.out.println("break because overpowered");
				}
				return;
			}

			// Faster than attached network -> become the new source
			detachKinetics();
			setSpeed(speed);
			source = null;
			setNetwork(createNetworkId());
			attachKinetics();
			return;
		}

		// Reapply source
		detachKinetics();
		setSpeed(speed);
		attachKinetics();
	}

    public void clearState() {
        setRotation(0.0F, 0.0F);
        SteamFlywheelBlock.setConnection(this.level, worldPosition, getBlockState(), null);
    }
}

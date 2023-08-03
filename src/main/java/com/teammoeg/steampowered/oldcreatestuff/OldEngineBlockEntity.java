package com.teammoeg.steampowered.oldcreatestuff;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class OldEngineBlockEntity extends SmartBlockEntity {
    public float appliedCapacity;
    public float appliedSpeed;
    protected OldFlywheelBlockEntity poweredWheel;

    public OldEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(1.5);
    }

    public void lazyTick() {
        super.lazyTick();
        if (!this.level.isClientSide) {
            if (this.poweredWheel != null && this.poweredWheel.isRemoved()) {
                this.poweredWheel = null;
            }

            if (this.poweredWheel == null) {
                this.attachWheel();
            }

        }
    }

    public void attachWheel() {
        Direction engineFacing = (Direction)this.getBlockState().getValue(OldEngineBlock.FACING);
        BlockPos wheelPos = this.worldPosition.relative(engineFacing, 2);
        BlockState wheelState = this.level.getBlockState(wheelPos);
        if (AllBlocks.FLYWHEEL.has(wheelState)) {
            Direction wheelFacing = (Direction)wheelState.getValue(OldFlywheelBlock.HORIZONTAL_FACING);
            if (wheelFacing.getAxis() == engineFacing.getClockWise().getAxis()) {
                if (!OldFlywheelBlock.isConnected(wheelState) || OldFlywheelBlock.getConnection(wheelState) == engineFacing.getOpposite()) {
                    BlockEntity te = this.level.getBlockEntity(wheelPos);
                    if (!te.isRemoved()) {
                        if (te instanceof OldFlywheelBlockEntity) {
                            if (!OldFlywheelBlock.isConnected(wheelState)) {
                                OldFlywheelBlock.setConnection(this.level, te.getBlockPos(), te.getBlockState(), engineFacing.getOpposite());
                            }

                            this.poweredWheel = (OldFlywheelBlockEntity)te;
                            this.refreshWheelSpeed();
                        }

                    }
                }
            }
        }
    }

    public void detachWheel() {
        if (this.poweredWheel != null && !this.poweredWheel.isRemoved()) {
            this.poweredWheel.setRotation(0.0F, 0.0F);
            OldFlywheelBlock.setConnection(this.level, this.poweredWheel.getBlockPos(), this.poweredWheel.getBlockState(), (Direction)null);
            this.poweredWheel = null;
        }
    }

    protected void refreshWheelSpeed() {
        if (this.poweredWheel != null) {
            this.poweredWheel.setRotation(this.appliedSpeed, this.appliedCapacity);
        }
    }
}

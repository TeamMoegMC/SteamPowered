package com.teammoeg.steampowered.oldcreatestuff;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public abstract class OldFlywheelBlock extends HorizontalKineticBlock implements IBE<OldFlywheelBlockEntity> {
    public static final EnumProperty<ConnectionState> CONNECTION = EnumProperty.create("connection", ConnectionState.class);

    public OldFlywheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(CONNECTION, OldFlywheelBlock.ConnectionState.NONE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CONNECTION));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = this.getPreferredHorizontalFacing(context);
        return preferred != null ? (BlockState)this.defaultBlockState().setValue(HORIZONTAL_FACING, preferred.getOpposite()) : (BlockState)this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    public static boolean isConnected(BlockState state) {
        return getConnection(state) != null;
    }

    public static Direction getConnection(BlockState state) {
        Direction facing = (Direction)state.getValue(HORIZONTAL_FACING);
        ConnectionState connection = (ConnectionState)state.getValue(CONNECTION);
        if (connection == OldFlywheelBlock.ConnectionState.LEFT) {
            return facing.getCounterClockWise();
        } else {
            return connection == OldFlywheelBlock.ConnectionState.RIGHT ? facing.getClockWise() : null;
        }
    }

    public static void setConnection(Level world, BlockPos pos, BlockState state, Direction direction) {
        Direction facing = (Direction)state.getValue(HORIZONTAL_FACING);
        ConnectionState connection = OldFlywheelBlock.ConnectionState.NONE;
        if (direction == facing.getClockWise()) {
            connection = OldFlywheelBlock.ConnectionState.RIGHT;
        }

        if (direction == facing.getCounterClockWise()) {
            connection = OldFlywheelBlock.ConnectionState.LEFT;
        }

        world.setBlock(pos, (BlockState)state.setValue(CONNECTION, connection), 18);
    }

    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == ((Direction)state.getValue(HORIZONTAL_FACING)).getOpposite();
    }

    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction)state.getValue(HORIZONTAL_FACING)).getAxis();
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Direction connection = getConnection(state);
        if (connection == null) {
            return super.onWrenched(state, context);
        } else if (context.getClickedFace().getAxis() == ((Direction)state.getValue(HORIZONTAL_FACING)).getAxis()) {
            return InteractionResult.PASS;
        } else {
            //Level world = context.getLevel();
            //BlockPos enginePos = context.getClickedPos().relative(connection, 2);
            //BlockState engine = world.getBlockState(enginePos);
            //if (engine.getBlock() instanceof FurnaceEngineBlock) {
            //    ((FurnaceEngineBlock)engine.getBlock()).withTileEntityDo(world, enginePos, EngineTileEntity::detachWheel);
            //}

            return super.onWrenched((BlockState)state.setValue(CONNECTION, OldFlywheelBlock.ConnectionState.NONE), context);
        }
    }

    @Override
    public Class<OldFlywheelBlockEntity> getBlockEntityClass() {
        return OldFlywheelBlockEntity.class;
    }

    public enum ConnectionState implements StringRepresentable {
        NONE,
        LEFT,
        RIGHT;

        public @NotNull String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}

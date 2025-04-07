package com.teammoeg.steampowered.oldcreatestuff;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class OldEngineBlock extends HorizontalDirectionalBlock implements IWrenchable {
    protected OldEngineBlock(BlockBehaviour.Properties builder) {
        super(builder);
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return this.isValidPosition(state, worldIn, pos, (Direction)state.getValue(FACING));
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.FAIL;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        return (BlockState)this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(new Property[]{FACING}));
    }

    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (fromPos.equals(getBaseBlockPos(state, pos)) && !this.canSurvive(state, worldIn, pos)) {
                worldIn.destroyBlock(pos, true);
            }
        }
    }

    private boolean isValidPosition(BlockState state, BlockGetter world, BlockPos pos, Direction facing) {
        BlockPos baseBlockPos = getBaseBlockPos(state, pos);
        if (!this.isValidBaseBlock(world.getBlockState(baseBlockPos), world, pos)) {
            return false;
        } else {
            Direction[] var6 = Iterate.horizontalDirections;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction otherFacing = var6[var8];
                if (otherFacing != facing) {
                    BlockPos otherPos = baseBlockPos.relative(otherFacing);
                    BlockState otherState = world.getBlockState(otherPos);
                    if (otherState.getBlock() instanceof OldEngineBlock && getBaseBlockPos(otherState, otherPos).equals(baseBlockPos)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static BlockPos getBaseBlockPos(BlockState state, BlockPos pos) {
        return pos.relative(((Direction)state.getValue(FACING)).getOpposite());
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public abstract PartialModel getFrameModel();

    protected abstract boolean isValidBaseBlock(BlockState var1, BlockGetter var2, BlockPos var3);
}

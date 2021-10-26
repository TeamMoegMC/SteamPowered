/*
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Steam Powered.
 *
 * Steam Powered is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Steam Powered is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Steam Powered. If not, see <https://www.gnu.org/licenses/>.
 */

package com.teammoeg.steampowered.content.alternator;

import com.simibubi.create.content.contraptions.base.DirectionalKineticBlock;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.block.SPShapes;
import com.teammoeg.steampowered.registrate.SPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Random;

/**
 * Adapted from: Create: Crafts & Additions
 * @author MRH0
 * @author yuesha-yc
 */
public class DynamoBlock extends DirectionalKineticBlock implements ITE<DynamoTileEntity>, IRotate {

    public static final BooleanProperty REDSTONE_LOCKED = BooleanProperty.create("redstone_locked");

    //TODO: Fix shape
    public static final VoxelShaper ALTERNATOR_SHAPE = SPShapes.shape(0, 3, 0, 16, 13, 16).add(2, 0, 2, 14, 14, 14).forDirectional();

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return ALTERNATOR_SHAPE.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) || preferred == null) {
            return super.getStateForPlacement(context).setValue(REDSTONE_LOCKED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
        }
        return defaultBlockState().setValue(FACING, preferred).setValue(REDSTONE_LOCKED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    public DynamoBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(REDSTONE_LOCKED, false));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(REDSTONE_LOCKED));
    }

    @Override
    public boolean hasShaftTowards(IWorldReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public Class<DynamoTileEntity> getTileEntityClass() {
        return DynamoTileEntity.class;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.DYNAMO.create();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> itemStacks) {
        if (ModList.get().isLoaded("createaddition") && SPConfig.SERVER.disableDynamo.get()) {
            // removes dynamo from creative tab when CC&A is loaded AND config is set to true
        } else {
            super.fillItemCategory(group, itemStacks);
        }
    }

    @Override
    public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t, ITooltipFlag f) {
        t.add(new TranslationTextComponent("block.steampowered.dynamo.tooltip.summary").withStyle(TextFormatting.GRAY));
        t.add(new StringTextComponent("We adapt and appreciate MRH0's code").withStyle(TextFormatting.GRAY));
        if (ModList.get().isLoaded("createaddition")) {
            if (SPConfig.SERVER.disableDynamo.get()) {
                t.add(new StringTextComponent("Dynamo is disabled in [save]/serverconfig/steampowered-server.toml").withStyle(TextFormatting.RED));
            }
        }
        super.appendHoverText(i,w,t,f);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean flag) {
        if (!world.isClientSide) {
            TileEntity tileentity = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
            if (tileentity != null) {
                if (tileentity instanceof DynamoTileEntity) {
                    ((DynamoTileEntity) tileentity).updateCache();
                }
            }

            boolean isLocked = state.getValue(REDSTONE_LOCKED);
            if (isLocked != world.hasNeighborSignal(pos)) {
                if (isLocked) {
                    world.getBlockTicks().scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(REDSTONE_LOCKED), 2);
                }
            }

        }
    }

    @Override
    public void tick(BlockState state, ServerWorld serverworld, BlockPos pos, Random random) {
        if (state.getValue(REDSTONE_LOCKED) && !serverworld.hasNeighborSignal(pos)) {
            serverworld.setBlock(pos, state.cycle(REDSTONE_LOCKED), 2);
        }
    }
}

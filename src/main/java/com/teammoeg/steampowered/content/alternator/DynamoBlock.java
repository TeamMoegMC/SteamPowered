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

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import com.teammoeg.steampowered.block.SPShapes;
import com.teammoeg.steampowered.registrate.SPBlockEntities;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * Adapted from: Create: Crafts & Additions
 * @author MRH0
 * @author yuesha-yc
 */
public class DynamoBlock extends DirectionalKineticBlock implements IBE<DynamoBlockEntity>, IRotate {

    public static final BooleanProperty REDSTONE_LOCKED = BooleanProperty.create("redstone_locked");
    public static final VoxelShaper DYNAMO_SHAPE = SPShapes
            .shape(2, 0, 1, 14, 4, 16)
            .add(3, 3, 2, 13, 15, 13)
            .add(0,0, 13, 16,16, 16)
            .forDirectional();

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return DYNAMO_SHAPE.get(state.getValue(FACING).getOpposite());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) || preferred == null) {
            return super.getStateForPlacement(context).setValue(REDSTONE_LOCKED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
        }
        return defaultBlockState().setValue(FACING, preferred.getOpposite()).setValue(REDSTONE_LOCKED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    public DynamoBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(REDSTONE_LOCKED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(REDSTONE_LOCKED));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public Class<DynamoBlockEntity> getBlockEntityClass() {
        return DynamoBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DynamoBlockEntity> getBlockEntityType() {
        return SPBlockEntities.DYNAMO.get();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

   /* @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> itemStacks) {
        if (ModList.get().isLoaded("createaddition") && SPConfig.SERVER.disableDynamo.get()) {
            // removes dynamo from creative tab when CC&A is loaded AND config is set to true
        } else {
            super.fillItemCategory(group, itemStacks);
        }
    }*/

   @Override
    public void appendHoverText(ItemStack i, BlockGetter w, List<Component> t, TooltipFlag f) {
        t.add(Component.translatable("tooltip.steampowered.alternator").withStyle(ChatFormatting.GRAY));
    	if(Screen.hasShiftDown()) {
    		t.add(Component.translatable("tooltip.steampowered.alternator.thanks").withStyle(ChatFormatting.GOLD));
    	}else {
    		t.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY,false));
    	}
    	if(Screen.hasControlDown()) {
    		t.add(Component.translatable("tooltip.steampowered.alternator.redstone").withStyle(ChatFormatting.RED));
    	}else {
    		t.add(CreateLang.translate("tooltip.holdForControls", CreateLang.translate("tooltip.keyCtrl")
			.style(ChatFormatting.GRAY))
			.style(ChatFormatting.DARK_GRAY).component());
    	}
        /*if (ModList.get().isLoaded("createaddition")) {
            if (SPConfig.SERVER.disableDynamo.get()) {
                t.add(new StringTextComponent("Dynamo is disabled in [save]/serverconfig/steampowered-server.toml").withStyle(TextFormatting.RED));
            }
        }*/
        super.appendHoverText(i,w,t,f);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean flag) {
        if (!world.isClientSide) {
            boolean isLocked = state.getValue(REDSTONE_LOCKED);
            if (isLocked != world.hasNeighborSignal(pos)) {
                if (isLocked) {
                    world.scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(REDSTONE_LOCKED), 2);
                }
            }

        }
    }

    @Override
    public void tick(BlockState state, ServerLevel serverworld, BlockPos pos, RandomSource random) {
        if (state.getValue(REDSTONE_LOCKED) && !serverworld.hasNeighborSignal(pos)) {
            serverworld.setBlock(pos, state.cycle(REDSTONE_LOCKED), 2);
        }
    }
}

/**
 * Available under MIT the license more info at: https://tldrlegal.com/license/mit-license
 *
 * MIT License
 *
 * Copyright 2021 MRH0
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction,
 * including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom t
 * he Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

import java.util.List;

/**
 * Adapted from: Create: Crafts & Additions
 * @author MRH0
 */
public class AlternatorBlock extends DirectionalKineticBlock implements ITE<AlternatorTileEntity>, IRotate {

    public static final VoxelShaper ALTERNATOR_SHAPE = SPShapes.shape(0, 3, 0, 16, 13, 16).add(2, 0, 2, 14, 14, 14).forDirectional();

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return ALTERNATOR_SHAPE.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred);
    }

    public AlternatorBlock(Properties properties) {
        super(properties);
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
    public Class<AlternatorTileEntity> getTileEntityClass() {
        return AlternatorTileEntity.class;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.ALTERNATOR.create();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getBlockEntity(pos) : null;
        if (tileentity != null) {
            if (tileentity instanceof AlternatorTileEntity) {
                ((AlternatorTileEntity) tileentity).updateCache();
            }
        }
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> itemStacks) {
        if (SPConfig.SERVER.disableSteamPoweredDynamo.get()) {
            // dynamo disabled
        } else {
            super.fillItemCategory(group, itemStacks);
        }
    }

    @Override
    public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t, ITooltipFlag f) {
        if (SPConfig.SERVER.disableSteamPoweredDynamo.get()) {
            t.add(new StringTextComponent("Dynamo is disabled in the server config").withStyle(TextFormatting.RED));
        } else {
            t.add(new StringTextComponent("Dynamo adapted from the mod Create: Crafts & Additions. Credits: MRH0").withStyle(TextFormatting.RED));
            t.add(new StringTextComponent("Codes and assets under MIT License").withStyle(TextFormatting.RED));
            t.add(new TranslationTextComponent("block.steampowered.alternator.tooltip.summary").withStyle(TextFormatting.GRAY));
        }
        super.appendHoverText(i,w,t,f);
    }
}

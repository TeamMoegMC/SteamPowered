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

package com.teammoeg.steampowered.content.burner;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BurnerBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BurnerBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing).setValue(LIT, Boolean.valueOf(false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void stepOn(World w, BlockPos p, Entity e) {
        if (w.getBlockState(p).getValue(LIT) == true)
            if (e instanceof LivingEntity)
                e.hurt(DamageSource.HOT_FLOOR, 2);
    }
    public abstract int getHuProduce() ;
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT).add(FACING));
    }

    @Override
	public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t,
			ITooltipFlag f) {
    	t.add(new TranslationTextComponent("tooltip.steampowered.burner.danger").withStyle(TextFormatting.RED));
    	t.add(new TranslationTextComponent("tooltip.steampowered.burner.huproduce",this.getHuProduce()).withStyle(TextFormatting.GOLD));
    	
		super.appendHoverText(i,w,t,f);
	}

	@Override
    public ActionResultType use(BlockState bs, World w, BlockPos bp, PlayerEntity pe, Hand h, BlockRayTraceResult br) {
        if (pe.getItemInHand(h).isEmpty()) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
            ItemStack is = cap.getStackInSlot(0);
            if (!is.isEmpty()) {
                pe.setItemInHand(h, cap.extractItem(0, is.getCount(), false));
                return ActionResultType.SUCCESS;
            }
        } else if (ForgeHooks.getBurnTime(pe.getItemInHand(h)) != 0) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
            pe.setItemInHand(h, cap.insertItem(0, pe.getItemInHand(h), false));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }


}

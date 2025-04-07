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

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import com.teammoeg.steampowered.client.ClientUtils;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public abstract class BurnerBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty REDSTONE_LOCKED = BooleanProperty.create("redstone_locked");
    public BurnerBlock(Properties props) {
        super(props.lightLevel(s->s.getValue(LIT)?10:0));
    }

    @Override
	public void animateTick(BlockState bs, Level w, BlockPos bp, RandomSource r) {
		super.animateTick(bs, w, bp, r);
        if (bs.getValue(BurnerBlock.LIT)) {
            double d0 = bp.getX() + 0.5D;
            double d1 = bp.getY();
            double d2 = bp.getZ() + 0.5D;
            if (r.nextDouble() < 0.2D) {
                w.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            if (r.nextDouble() < 0.5D) {
                Direction direction = bs.getValue(BurnerBlock.FACING);
                Direction.Axis direction$axis = direction.getAxis();
                double d4 = w.getRandom().nextDouble() * 0.6D - 0.3D;
                double d5 = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52D : d4;
                double d6 = w.getRandom().nextDouble() * 6.0D / 16.0D;
                double d7 = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : d4;
                w.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                w.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            }
        }
	}

	@Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing).setValue(LIT, Boolean.valueOf(false)).setValue(REDSTONE_LOCKED,false);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (pState.getValue(LIT))
            if (pEntity instanceof LivingEntity)
                pEntity.hurt(pLevel.damageSources().hotFloor(), 2);
    }

    public abstract int getHuProduce() ;
    public abstract double getEfficiency();
    public String getEfficiencyString() {
    	return ((int)(this.getEfficiency()*1000))/10F+"%";
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT).add(FACING).add(REDSTONE_LOCKED));
    }

    @Override
	public void appendHoverText(ItemStack i, BlockGetter w, List<Component> t,
			TooltipFlag f) {
    	
    	if(Screen.hasShiftDown()) {
    		t.add(Component.translatable("tooltip.steampowered.burner.brief").withStyle(ChatFormatting.GOLD));
    		if(ClientUtils.hasGoggles()) {
    			t.add(Component.translatable("tooltip.steampowered.burner.efficiency",getEfficiencyString()).withStyle(ChatFormatting.RED));
    			t.add(Component.translatable("tooltip.steampowered.burner.huproduce",this.getHuProduce()).withStyle(ChatFormatting.GOLD));
    			t.add(Component.translatable("tooltip.steampowered.burner.danger").withStyle(ChatFormatting.RED));
    		}
    	}else {
    		t.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY,false));
    	}
    	if(Screen.hasControlDown()) {
    		t.add(Component.translatable("tooltip.steampowered.burner.redstone").withStyle(ChatFormatting.RED));
    	}else {
    		t.add(CreateLang.translate("tooltip.holdForControls", CreateLang.translate("tooltip.keyCtrl")
			.style(ChatFormatting.GRAY))
			.style(ChatFormatting.DARK_GRAY).component());
    	}
		super.appendHoverText(i,w,t,f);
	}

	@Override
    public InteractionResult use(BlockState bs, Level w, BlockPos bp, Player pe, InteractionHand h, BlockHitResult br) {
        if (pe.getItemInHand(h).isEmpty()) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
            ItemStack is = cap.getStackInSlot(0);
            if (!is.isEmpty()) {
                pe.setItemInHand(h, cap.extractItem(0, is.getCount(), false));
                return InteractionResult.SUCCESS;
            }
        } else if (ForgeHooks.getBurnTime(pe.getItemInHand(h), RecipeType.BLASTING) != 0 && pe.getItemInHand(h).getCraftingRemainingItem().isEmpty() && !pe.getItemInHand(h).is(Items.LAVA_BUCKET)) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
            pe.setItemInHand(h, cap.insertItem(0, pe.getItemInHand(h), false));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
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

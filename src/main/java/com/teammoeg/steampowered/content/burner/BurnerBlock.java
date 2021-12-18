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
import java.util.Random;

import com.simibubi.create.foundation.item.ItemDescription.Palette;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.teammoeg.steampowered.client.ClientUtils;
import com.teammoeg.steampowered.content.alternator.DynamoTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BurnerBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty REDSTONE_LOCKED = BooleanProperty.create("redstone_locked");
    public BurnerBlock(Properties props) {
        super(props.lightLevel(s->s.getValue(LIT)?10:0));
    }

    @Override
	public void animateTick(BlockState bs, World w, BlockPos bp, Random r) {
		super.animateTick(bs, w, bp, r);
        if (bs.getValue(BurnerBlock.LIT)) {
            double d0 = bp.getX() + 0.5D;
            double d1 = bp.getY();
            double d2 = bp.getZ() + 0.5D;
            if (r.nextDouble() < 0.2D) {
                w.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing).setValue(LIT, Boolean.valueOf(false)).setValue(REDSTONE_LOCKED,false);
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
    public abstract double getEfficiency();
    public String getEfficiencyString() {
    	return ((int)(this.getEfficiency()*1000))/10F+"%";
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT).add(FACING).add(REDSTONE_LOCKED));
    }

    @Override
	public void appendHoverText(ItemStack i, IBlockReader w, List<ITextComponent> t,
			ITooltipFlag f) {
    	
    	if(Screen.hasShiftDown()) {
    		t.add(new TranslationTextComponent("tooltip.steampowered.burner.brief").withStyle(TextFormatting.GOLD));
    		if(ClientUtils.hasGoggles()) {
    			t.add(new TranslationTextComponent("tooltip.steampowered.burner.efficiency",getEfficiencyString()).withStyle(TextFormatting.RED));
    			t.add(new TranslationTextComponent("tooltip.steampowered.burner.huproduce",this.getHuProduce()).withStyle(TextFormatting.GOLD));
    			t.add(new TranslationTextComponent("tooltip.steampowered.burner.danger").withStyle(TextFormatting.RED));
    		}
    	}else {
    		t.add(TooltipHelper.holdShift(Palette.Gray,false));
    	}
    	if(Screen.hasControlDown()) {
    		t.add(new TranslationTextComponent("tooltip.steampowered.burner.redstone").withStyle(TextFormatting.RED));
    	}else {
    		t.add(Lang.translate("tooltip.holdForControls", Lang.translate("tooltip.keyCtrl")
			.withStyle(TextFormatting.GRAY))
			.withStyle(TextFormatting.DARK_GRAY));
    	}
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
        } else if (ForgeHooks.getBurnTime(pe.getItemInHand(h)) != 0 && pe.getItemInHand(h).getContainerItem().isEmpty()) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
            pe.setItemInHand(h, cap.insertItem(0, pe.getItemInHand(h), false));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
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

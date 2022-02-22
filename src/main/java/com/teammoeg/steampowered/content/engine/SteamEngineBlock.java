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

package com.teammoeg.steampowered.content.engine;

import java.util.Random;

import javax.annotation.Nullable;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.FurnaceEngineTileEntity;
import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.FluidRegistry;
import com.teammoeg.steampowered.ItemRegistry;
import com.teammoeg.steampowered.client.Particles;
import com.teammoeg.steampowered.registrate.SPTiles;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SteamEngineBlock extends EngineBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SteamEngineBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing).setValue(LIT, Boolean.valueOf(false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT));
    }

//    @Override
//    public Class<SteamEngineTileEntity> getTileEntityClass() {
//        return SteamEngineTileEntity.class;
//    }
//
//    @Override
//    public BlockEntityType<? extends SteamEngineTileEntity> getTileEntityType() {
//        return SPTiles.BRONZE_STEAM_ENGINE.get();
//    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.FURNACE_ENGINE.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public PartialModel getFrameModel() {
        return AllBlockPartials.FURNACE_GENERATOR_FRAME;
    }

    @Override
    protected boolean isValidBaseBlock(BlockState baseBlock, BlockGetter world, BlockPos pos) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, Level p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        if (p_180655_1_.getValue(LIT)) {
            double d0 = p_180655_3_.getX() + 0.5D;
            double d1 = p_180655_3_.getY();
            double d2 = p_180655_3_.getZ() + 0.5D;
            if (p_180655_4_.nextDouble() < 0.1D) {
                p_180655_2_.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = p_180655_1_.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = p_180655_4_.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52D : d4;
            double d6 = p_180655_4_.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : d4;
            p_180655_2_.addParticle(Particles.STEAM.get(), d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        if (player.getItemInHand(hand).getItem() == ItemRegistry.pressurizedSteamContainer.get()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof SteamEngineTileEntity) {
                SteamEngineTileEntity steamEngine = (SteamEngineTileEntity) te;
                IFluidHandler cap = steamEngine.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve().get();
                cap.fill(new FluidStack(FluidRegistry.steam.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                player.getItemInHand(hand).shrink(1);
                ItemStack ret=new ItemStack(ItemRegistry.pressurizedGasContainer.get());
                if(!player.addItem(ret))
                	world.addFreshEntity(new ItemEntity(world, pos.getX(),pos.getY(),pos.getZ(),ret));
                return InteractionResult.SUCCESS;
            }
			return InteractionResult.PASS;
        }
		return super.use(state, world, pos, player, hand, blockRayTraceResult);
    }
}

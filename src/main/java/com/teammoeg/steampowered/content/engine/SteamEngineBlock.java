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

import com.simibubi.create.AllShapes;
import com.teammoeg.steampowered.oldcreatestuff.OldBlockPartials;
import com.teammoeg.steampowered.oldcreatestuff.OldEngineBlock;
import com.teammoeg.steampowered.registrate.SPFluids;
import com.teammoeg.steampowered.registrate.SPItems;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class SteamEngineBlock extends OldEngineBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SteamEngineBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing).setValue(LIT, false);
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
        return OldBlockPartials.FURNACE_GENERATOR_FRAME;
    }

    @Override
    protected boolean isValidBaseBlock(BlockState baseBlock, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        if (player.getItemInHand(hand).getItem() == SPItems.PRESSURIZED_STEAM_CONTAINER.get()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof SteamEngineTileEntity) {
                SteamEngineTileEntity steamEngine = (SteamEngineTileEntity) te;
                IFluidHandler cap = steamEngine.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
                cap.fill(new FluidStack(SPFluids.STEAM.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                player.getItemInHand(hand).shrink(1);
                ItemStack ret=new ItemStack(SPItems.PRESSURIZED_GAS_CONTAINER.get());
                if(!player.addItem(ret))
                	world.addFreshEntity(new ItemEntity(world, pos.getX(),pos.getY(),pos.getZ(),ret));
                return InteractionResult.SUCCESS;
            }
			return InteractionResult.PASS;
        }
		return super.use(state, world, pos, player, hand, blockRayTraceResult);
    }
}

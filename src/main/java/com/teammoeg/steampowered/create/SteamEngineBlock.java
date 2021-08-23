package com.teammoeg.steampowered.create;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.FurnaceEngineTileEntity;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class SteamEngineBlock extends EngineBlock implements ITE<FurnaceEngineTileEntity> {
    protected SteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.STEAM_ENGINE.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AllShapes.FURNACE_ENGINE.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public PartialModel getFrameModel() {
        return AllBlockPartials.FURNACE_GENERATOR_FRAME;
    }

    @Override
    protected boolean isValidBaseBlock(BlockState baseBlock, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    public Class<FurnaceEngineTileEntity> getTileEntityClass() {
        return FurnaceEngineTileEntity.class;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if (player.getMainHandItem().isEmpty() && tileentity instanceof SteamEngineTileEntity) {
            if (player.isCrouching()) {
                ((SteamEngineTileEntity) tileentity).getTank().fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
            } else {
                player.displayClientMessage(new StringTextComponent("Steam Volume:" + ((SteamEngineTileEntity) tileentity).getTank().getFluidAmount()), false);
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }
}

package com.teammoeg.steampowered.block;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.foundation.block.ITE;
import com.teammoeg.steampowered.registrate.SPTiles;
import com.teammoeg.steampowered.tileentity.BronzeSteamEngineTileEntity;
import com.teammoeg.steampowered.tileentity.SteamEngineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class BronzeSteamEngineBlock extends SteamEngineBlock implements ITE<BronzeSteamEngineTileEntity> {
    public BronzeSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SPTiles.BRONZE_STEAM_ENGINE.create();
    }

    @Override
    public Class<BronzeSteamEngineTileEntity> getTileEntityClass() {
        return BronzeSteamEngineTileEntity.class;
    }
}

package com.teammoeg.steampowered.oldcreatestuff;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class OldFlywheelGenerator extends SpecialBlockStateGen {
    public OldFlywheelGenerator() {
    }

    protected int getXRotation(BlockState state) {
        return 0;
    }

    protected int getYRotation(BlockState state) {
        return this.horizontalAngle((Direction)state.getValue(OldFlywheelBlock.HORIZONTAL_FACING)) + 90;
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        BlockModelProvider var10000 = prov.models();
        String var10002 = ctx.getName();
        return var10000.getExistingFile(prov.modLoc("block/" + var10002 + "/casing_" + ((OldFlywheelBlock.ConnectionState)state.getValue(OldFlywheelBlock.CONNECTION)).getSerializedName()));
    }
}

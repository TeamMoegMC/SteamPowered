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

package com.teammoeg.steampowered.registrate;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.teammoeg.steampowered.SPTags;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.joml.Vector3f;

import java.util.function.Supplier;

import static com.teammoeg.steampowered.SteamPowered.REGISTRATE;

public class SPFluids {
    static {
        REGISTRATE.setCreativeTab(SPTabs.SP_BASE_TAB);
    }

    public static final FluidEntry<ForgeFlowingFluid.Flowing> STEAM = REGISTRATE
            .standardFluid("steam", SPFluids.SolidRenderedPlaceableFluidType.create(0xDEDEDE, () -> 1f / 8f))
            .lang("Steam")
            .properties(b -> b.density(-10).viscosity(1).temperature(473).canPushEntity(false)
                    .canConvertToSource(false).canDrown(true))
            .fluidProperties(b -> b.slopeFindDistance(3).explosionResistance(100F))
            .tag(SPTags.STEAM)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(AllTags.forgeItemTag("buckets/steam"))
            .build()
            .register();

    // From Create's AllFluids.java
    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SPFluids.SolidRenderedPlaceableFluidType fluidType = new SPFluids.SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }

    public static void register() {
    }
}

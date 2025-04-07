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

package com.teammoeg.steampowered.client.instance;

import com.teammoeg.steampowered.block.SPBlockPartials;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlockEntity;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class CastIronFlywheelInstance extends AbstractSPFlywheelInstance {
    public CastIronFlywheelInstance(VisualizationContext modelManager, OldFlywheelBlockEntity tile, float d) {
        super(modelManager, tile, d);
    }

    @Override
    protected PartialModel getWheelModel() {
        return SPBlockPartials.CAST_IRON_FLYWHEEL;
    }

    @Override
    protected PartialModel getUpperSlidingModel() {
        return SPBlockPartials.CAST_IRON_FLYWHEEL_UPPER_SLIDING;
    }

    @Override
    protected PartialModel getLowerSlidingModel() {
        return SPBlockPartials.CAST_IRON_FLYWHEEL_LOWER_SLIDING;
    }

    @Override
    protected PartialModel getUpperRotatingModel() {
        return SPBlockPartials.CAST_IRON_FLYWHEEL_UPPER_ROTATING;
    }

    @Override
    protected PartialModel getLowerRotatingModel() {
        return SPBlockPartials.CAST_IRON_FLYWHEEL_LOWER_ROTATING;
    }
}
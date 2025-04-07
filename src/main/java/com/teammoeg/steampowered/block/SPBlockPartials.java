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

package com.teammoeg.steampowered.block;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class SPBlockPartials {
    public static final PartialModel BRONZE_FLYWHEEL = get("bronze_flywheel/wheel");
    public static final PartialModel BRONZE_FLYWHEEL_UPPER_ROTATING = get("bronze_flywheel/upper_rotating_connector");
    public static final PartialModel BRONZE_FLYWHEEL_LOWER_ROTATING = get("bronze_flywheel/lower_rotating_connector");
    public static final PartialModel BRONZE_FLYWHEEL_UPPER_SLIDING = get("bronze_flywheel/upper_sliding_connector");
    public static final PartialModel BRONZE_FLYWHEEL_LOWER_SLIDING = get("bronze_flywheel/lower_sliding_connector");

    public static final PartialModel CAST_IRON_FLYWHEEL = get("cast_iron_flywheel/wheel");
    public static final PartialModel CAST_IRON_FLYWHEEL_UPPER_ROTATING = get("cast_iron_flywheel/upper_rotating_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_LOWER_ROTATING = get("cast_iron_flywheel/lower_rotating_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_UPPER_SLIDING = get("cast_iron_flywheel/upper_sliding_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_LOWER_SLIDING = get("cast_iron_flywheel/lower_sliding_connector");

    public static final PartialModel STEEL_FLYWHEEL = get("steel_flywheel/wheel");
    public static final PartialModel STEEL_FLYWHEEL_UPPER_ROTATING = get("steel_flywheel/upper_rotating_connector");
    public static final PartialModel STEEL_FLYWHEEL_LOWER_ROTATING = get("steel_flywheel/lower_rotating_connector");
    public static final PartialModel STEEL_FLYWHEEL_UPPER_SLIDING = get("steel_flywheel/upper_sliding_connector");
    public static final PartialModel STEEL_FLYWHEEL_LOWER_SLIDING = get("steel_flywheel/lower_sliding_connector");

    public static final PartialModel DYNAMO_SHAFT = get("dynamo/shaft");

    private static PartialModel get(String path) {
        return PartialModel.of(new ResourceLocation("steampowered", "block/" + path));
    }

    public static void clientInit() {
    }
}

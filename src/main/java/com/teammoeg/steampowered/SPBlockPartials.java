package com.teammoeg.steampowered;

import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.util.ResourceLocation;

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

    private static PartialModel get(String path) {
        return new PartialModel(new ResourceLocation("steampowered", "block/" + path));
    }

    public static void clientInit() {
    }
}

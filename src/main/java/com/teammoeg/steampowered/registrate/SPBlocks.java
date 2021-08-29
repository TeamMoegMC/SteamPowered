package com.teammoeg.steampowered.registrate;

import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticBlockModel;
import com.simibubi.create.content.contraptions.relays.elementary.CogwheelBlockItem;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.block.MetalCogwheelBlock;
import com.teammoeg.steampowered.block.SteamEngineBlock;
import net.minecraft.block.SoundType;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class SPBlocks {
    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final BlockEntry<SteamEngineBlock> STEAM_ENGINE = REGISTRATE.block("steam_engine",  SteamEngineBlock::new)
            .initialProperties(SharedProperties::stone)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<MetalCogwheelBlock> STEEL_COGWHEEL = REGISTRATE.block("steel_cogwheel", MetalCogwheelBlock::small)
            .initialProperties(SharedProperties::softMetal)
            .transform(BlockStressDefaults.setImpact(0.1))
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> STEEL_LARGE_COGWHEEL = REGISTRATE.block("steel_large_cogwheel", MetalCogwheelBlock::large)
            .initialProperties(SharedProperties::softMetal)
            .transform(BlockStressDefaults.setImpact(0.1))
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> CAST_IRON_COGWHEEL = REGISTRATE.block("cast_iron_cogwheel", MetalCogwheelBlock::small)
            .initialProperties(SharedProperties::softMetal)
            .transform(BlockStressDefaults.setImpact(0.2))
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> CAST_IRON_LARGE_COGWHEEL = REGISTRATE.block("cast_iron_large_cogwheel", MetalCogwheelBlock::large)
            .initialProperties(SharedProperties::softMetal)
            .transform(BlockStressDefaults.setImpact(0.2))
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> BRONZE_COGWHEEL = REGISTRATE.block("bronze_cogwheel", MetalCogwheelBlock::small)
            .initialProperties(SharedProperties::softMetal)
            .transform(BlockStressDefaults.setImpact(0.3))
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<MetalCogwheelBlock> BRONZE_LARGE_COGWHEEL = REGISTRATE.block("bronze_large_cogwheel", MetalCogwheelBlock::large)
            .initialProperties(SharedProperties::softMetal)
            .transform(BlockStressDefaults.setImpact(0.3))
            .properties(p -> p.sound(SoundType.METAL))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .item(CogwheelBlockItem::new)
            .build()
            .register();

    public static void register() {
        Create.registrate().addToSection(STEAM_ENGINE, AllSections.KINETICS);
        Create.registrate().addToSection(STEEL_COGWHEEL, AllSections.KINETICS);
        Create.registrate().addToSection(STEEL_LARGE_COGWHEEL, AllSections.KINETICS);
        Create.registrate().addToSection(CAST_IRON_COGWHEEL, AllSections.KINETICS);
        Create.registrate().addToSection(CAST_IRON_LARGE_COGWHEEL, AllSections.KINETICS);
        Create.registrate().addToSection(BRONZE_COGWHEEL, AllSections.KINETICS);
        Create.registrate().addToSection(BRONZE_LARGE_COGWHEEL, AllSections.KINETICS);
    }
}

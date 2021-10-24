package com.teammoeg.steampowered.ponder;

import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import com.teammoeg.steampowered.content.burner.BurnerBlock;
import com.teammoeg.steampowered.content.engine.SteamEngineBlock;
import com.teammoeg.steampowered.registrate.SPBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SPScenes {

    public static void steamEngine(SceneBuilder scene, SceneBuildingUtil util) {
        steamEngine(scene, util, false);
    }

    public static void steamFlywheel(SceneBuilder scene, SceneBuildingUtil util) {
        steamEngine(scene, util, true);
    }

    public static void steamBoiler(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("boiler", "Generating Steam through Boilers and Burning Chambers");
        scene.configureBasePlate(0, 0, 6);
        BlockPos burner = util.grid.at(2, 1, 2);
        BlockPos boiler = util.grid.at(2, 2, 2);
        BlockPos engine = util.grid.at(0, 2, 2);
        BlockPos steamPump = util.grid.at(1, 2, 2);
        BlockPos steamCog1 = util.grid.at(1, 3, 3);
        BlockPos steamCog2 = util.grid.at(0, 3, 3);
        BlockPos waterPump = util.grid.at(4, 3, 2);
        BlockPos waterCog1 = util.grid.at(4, 2, 1);
        BlockPos waterCog2 = util.grid.at(5, 2, 1);

        // show the whole structure
        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.idle(10);
        scene.world.showSection(util.select.layers(1, 3), Direction.NORTH);
        scene.idle(50);

        // water pumps
        scene.world.setKineticSpeed(util.select.position(waterPump), 32.0F);
        scene.world.setKineticSpeed(util.select.position(waterCog1), 16.0F);
        scene.world.setKineticSpeed(util.select.position(waterCog2), 16.0F);
        scene.idle(30);

        // boiler text
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("The Boiler needs water to produce Steam")
                .placeNearTarget()
                .pointAt(util.vector.centerOf(boiler));
        scene.idle(100);

        // burner text
        scene.overlay.showText(100)
                .attachKeyFrame()
                .text("The Burning Chamber needs furnace fuel to heat the Boiler")
                .placeNearTarget()
                .pointAt(util.vector.centerOf(burner));
        scene.idle(100);

        scene.overlay.showText(80)
                .attachKeyFrame()
                .text("Right click with fuel item such as Coal or Planks to provide it with fuel")
                .placeNearTarget()
                .pointAt(util.vector.centerOf(burner));
        scene.idle(100);

        // add fuel
        scene.overlay.showControls(new InputWindowElement(util.vector.centerOf(burner), Pointing.UP).rightClick()
                .withItem(new ItemStack(Items.COAL)), 30);
        scene.idle(40);
        scene.overlay.showControls(new InputWindowElement(util.vector.centerOf(burner), Pointing.UP).rightClick()
                .withItem(new ItemStack(Items.OAK_PLANKS)), 30);
        scene.idle(40);
        scene.world.modifyBlock(burner, s -> s.setValue(BurnerBlock.LIT, true), false);
        scene.idle(20);

        scene.overlay.showText(80)
                .attachKeyFrame()
                .text("Right click with empty hand to take out the remaining fuel")
                .placeNearTarget()
                .pointAt(util.vector.centerOf(burner));
        scene.idle(100);

        // steam pumps
        scene.world.setKineticSpeed(util.select.position(steamPump), 64.0F);
        scene.world.setKineticSpeed(util.select.position(steamCog1), 32.0F);
        scene.world.setKineticSpeed(util.select.position(steamCog2), 32.0F);
        scene.world.modifyBlock(engine, s -> s.setValue(SteamEngineBlock.LIT, true), false);
        scene.idle(50);

        // engine text
        scene.overlay.showText(100)
                .attachKeyFrame()
                .text("Pump the Steam out of the boiler to power the Steam Engines")
                .placeNearTarget()
                .pointAt(util.vector.centerOf(engine));
        scene.idle(100);

    }

    public static void steamEngine(SceneBuilder scene, SceneBuildingUtil util, boolean flywheel) {
        scene.title(flywheel ? "flywheel" : "steam_engine", "Generating Rotational Force using the " + (flywheel ? "Flywheel" : "Steam Engine"));
        scene.configureBasePlate(0, 0, 6);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        BlockPos cogPos = util.grid.at(1, 1, 2);
        BlockPos wheelPos = util.grid.at(1, 1, 3);
        BlockPos gaugePos = util.grid.at(1, 1, 1);
        BlockPos enginePos = util.grid.at(3, 1, 3);
        scene.idle(5);
        Selection furnaceSelect = util.select.position(enginePos);
        Selection wheelSelect = util.select.position(wheelPos);
        scene.world.showSection(furnaceSelect, Direction.DOWN);
        scene.idle(10);
        scene.world.showSection(util.select.position(enginePos.west()), Direction.DOWN);
        scene.idle(10);
        scene.world.showSection(util.select.position(enginePos.west(2)), Direction.EAST);
        scene.idle(10);
        scene.world.showSection(util.select.position(enginePos.east(1)), Direction.WEST);
        scene.world.showSection(util.select.position(enginePos.east(1).north(1)), Direction.WEST);
        scene.idle(10);
        scene.world.showSection(util.select.position(enginePos.east(2)), Direction.UP);
        scene.world.showSection(util.select.position(enginePos.east(2).above(1)), Direction.UP);
        scene.world.showSection(util.select.position(enginePos.east(2).above(2)), Direction.UP);
        String text = flywheel ? "Flywheels are required for generating rotational force with the Steam Engine" : "Steam Engines generate Rotational Force while Steam is provided";
        scene.overlay.showText(80).attachKeyFrame().placeNearTarget().pointAt(util.vector.topOf(enginePos.west(flywheel ? 3 : 1))).text(text);
        scene.idle(7);
        scene.world.cycleBlockProperty(enginePos, SteamEngineBlock.LIT);
        scene.idle(90);
        scene.world.setKineticSpeed(util.select.fromTo(1, 1, 3, 1, 1, 1), 32.0F);
        scene.idle(40);
        scene.world.showSection(util.select.position(cogPos), Direction.SOUTH);
        scene.idle(15);
        scene.effects.rotationSpeedIndicator(cogPos);
        scene.world.showSection(util.select.position(gaugePos), Direction.SOUTH);
        scene.idle(15);
        scene.overlay.showText(80).attachKeyFrame().placeNearTarget().colored(PonderPalette.GREEN).pointAt(util.vector.blockSurface(gaugePos, Direction.WEST)).text("The provided Rotational Force has a very large stress capacity");
        scene.idle(90);

        scene.world.hideSection(furnaceSelect, Direction.DOWN);
        scene.world.hideSection(wheelSelect, Direction.DOWN);
        scene.idle(15);
        scene.world.setBlock(enginePos, SPBlocks.CAST_IRON_STEAM_ENGINE.get().defaultBlockState().setValue(SteamEngineBlock.FACING, Direction.NORTH).setValue(SteamEngineBlock.LIT, true), false);
        scene.world.setBlock(wheelPos, SPBlocks.CAST_IRON_FLYWHEEL.get().defaultBlockState().setValue(SteamEngineBlock.FACING, Direction.NORTH), false);
        scene.world.showSection(furnaceSelect, Direction.DOWN);
        scene.world.showSection(wheelSelect, Direction.DOWN);
        scene.idle(30);
        scene.world.setKineticSpeed(util.select.fromTo(1, 1, 3, 1, 1, 1), 48.0F);
        scene.idle(50);

        scene.world.hideSection(furnaceSelect, Direction.DOWN);
        scene.world.hideSection(wheelSelect, Direction.DOWN);
        scene.idle(15);
        scene.world.setBlock(enginePos, SPBlocks.STEEL_STEAM_ENGINE.get().defaultBlockState().setValue(SteamEngineBlock.FACING, Direction.NORTH).setValue(SteamEngineBlock.LIT, true), false);
        scene.world.setBlock(wheelPos, SPBlocks.STEEL_FLYWHEEL.get().defaultBlockState().setValue(SteamEngineBlock.FACING, Direction.NORTH), false);
        scene.world.showSection(furnaceSelect, Direction.DOWN);
        scene.world.showSection(wheelSelect, Direction.DOWN);
        scene.idle(30);
        scene.world.setKineticSpeed(util.select.fromTo(1, 1, 3, 1, 1, 1), 64.0F);

        scene.idle(5);
        scene.effects.rotationSpeedIndicator(cogPos);
        scene.idle(5);
        String text3 = flywheel ? "Using Flywheels made of Steel or Cast Iron will increase efficiency and generated capacity of the Flywheel" : "Using Steam Engines made of Steel or Cast Iron will increase efficiency and generated capacity of the Flywheel";
        scene.overlay.showText(80).placeNearTarget().colored(PonderPalette.MEDIUM).pointAt(util.vector.topOf(enginePos.west())).text(text3);
    }

    public static void alternator(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("alternator", "Generating Electric energy using a Alternator");
        scene.configureBasePlate(1, 0, 4);
        scene.world.showSection(util.select.layer(0), Direction.UP);

        BlockPos generator = util.grid.at(3, 1, 2);

        for (int i = 0; i < 6; i++) {
            scene.idle(5);
            scene.world.showSection(util.select.position(i, 1, 2), Direction.DOWN);
            //scene.world.showSection(util.select.position(i, 2, 2), Direction.DOWN);
        }

        scene.idle(10);
        scene.overlay.showText(50)
                .text("The Alternator generates electric energy (fe) from rotational force")
                .placeNearTarget()
                .pointAt(util.vector.topOf(generator));
        scene.idle(60);

        scene.overlay.showText(50)
                .text("It requires atleast 32 RPM to operate")
                .placeNearTarget()
                .pointAt(util.vector.topOf(generator));
        scene.idle(60);


        scene.overlay.showText(50)
                .text("The Alternators energy production is determined by the input RPM")
                .placeNearTarget()
                .pointAt(util.vector.topOf(generator));
        scene.idle(60);
    }
}

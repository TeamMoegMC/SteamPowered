package com.teammoeg.steampowered.ponder;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.teammoeg.steampowered.block.engine.SteamEngineBlock;
import com.teammoeg.steampowered.registrate.SPBlocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SPScenes {

    public static void steamEngine(SceneBuilder scene, SceneBuildingUtil util) {
        steamEngine(scene, util, false);
    }

    public static void steamFlywheel(SceneBuilder scene, SceneBuildingUtil util) {
        steamEngine(scene, util, true);
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

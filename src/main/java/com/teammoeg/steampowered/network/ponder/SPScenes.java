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

package com.teammoeg.steampowered.network.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.content.alternator.DynamoBlock;
import com.teammoeg.steampowered.content.burner.BurnerBlock;
import com.teammoeg.steampowered.content.engine.SteamEngineBlock;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlock;
import com.teammoeg.steampowered.registrate.SPBlocks;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.element.InputWindowElement;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class SPScenes {

    public static void steamEngine(SceneBuilder scene, SceneBuildingUtil util) {
        steamEngine(scene, util, false);
    }

    public static void steamFlywheel(SceneBuilder scene, SceneBuildingUtil util) {
        steamEngine(scene, util, true);
    }

    public static void steamBoiler(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("boiler", "Generating Steam through Boilers and Burning Chambers");
        scene.configureBasePlate(0, 0, 6);
        BlockPos burner = util.grid().at(3, 1, 2);
        BlockPos boiler = util.grid().at(3, 2, 2);
        BlockPos engine = util.grid().at(2, 2, 2);
        //BlockPos steamPump = util.grid().at(1, 2, 2);
        BlockPos waterPump = util.grid().at(5, 3, 2);

        // show the whole structure
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().layers(1, 3), Direction.NORTH);
        scene.idle(50);

        // water pumps
        scene.world().setKineticSpeed(util.select().position(waterPump), 32.0F);
        scene.world().setKineticSpeed(util.select().fromTo(5, 2, 1,6, 2, 1), -16.0F);
        scene.idle(30);

        // boiler text
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The Boiler needs water to produce Steam")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(boiler));
        scene.idle(100);

        // burner text
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("The Burning Chamber needs furnace fuel to heat the Boiler")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(burner));
        scene.idle(100);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Right click with fuel item such as Coal or Planks to provide it with fuel")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(burner));
        scene.idle(100);

        // add fuel
        InputWindowElement inputWindowElement = new InputWindowElement(util.vector().centerOf(burner), Pointing.UP);
        inputWindowElement.builder().rightClick()
                .withItem(new ItemStack(Items.COAL));
       // scene.overlay().showControls(inputWindowElement, 30);
        scene.idle(40);
        //scene.overlay().showControls(new InputWindowElement(util.vector().centerOf(burner), Pointing.UP).rightClick()
         //       .withItem(new ItemStack(Items.OAK_PLANKS)), 30);
        scene.idle(40);
        scene.world().modifyBlock(burner, s -> s.setValue(BurnerBlock.LIT, true), false);
        scene.idle(20);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Right click with empty hand to take out the remaining fuel")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(burner));
        scene.idle(100);

        // steam pumps
        //scene.world().setKineticSpeed(util.select().position(steamPump), 64.0F);
        scene.world().setKineticSpeed(util.select().fromTo(0,2,2,1,2,3), -32.0F);
        scene.world().modifyBlock(engine, s -> s.setValue(SteamEngineBlock.LIT, true), false);
        scene.idle(50);

        // engine text
        scene.overlay().showText(100)
                .attachKeyFrame()
                .text("Steam Engines would withdraw steam from boilers")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(engine));
        scene.idle(100);

    }

    public static void steamEngine(SceneBuilder builder, SceneBuildingUtil util, boolean flywheel) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title(flywheel ? "flywheel" : "steam_engine", "Generating Rotational Force using the " + (flywheel ? "Flywheel" : "Steam Engine"));
        scene.configureBasePlate(0, 0, 6);
        BlockPos cogPos = util.grid().at(1, 1, 2);
        BlockPos wheelPos = util.grid().at(1, 2, 3);
        BlockPos gaugePos = util.grid().at(1, 2, 1);
        BlockPos enginePos = util.grid().at(3, 2, 3);

        Selection f2Select = util.select().fromTo(1, 2, 3, 3, 2, 3);
        Selection b2Select=util.select().fromTo(5,1,3,5,2,3);
        scene.world().showSection(util.select().layer(0), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(4,1,3,4,2,3), Direction.EAST);
        scene.idle(10);
        scene.world().cycleBlockProperty(util.grid().at(4, 1, 3), SteamEngineBlock.LIT);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(1,1,3,3,2,3), Direction.EAST);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(0,1,0,1,3,2), Direction.SOUTH);
        String text = flywheel ? "Flywheels are required for generating rotational force with the Steam Engine" : "Steam Engines generate Rotational Force while Steam is provided";
        scene.overlay().showText(80).attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(enginePos.west(flywheel ? 2 : 0))).text(text);
        scene.idle(100);
        scene.world().cycleBlockProperty(enginePos, SteamEngineBlock.LIT);
        scene.world().setKineticSpeed(util.select().fromTo(0,1,0,1,3,3), -32F);
        scene.overlay().showText(80).attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(enginePos)).text("You can attach engines directly onto active boilers to run it at reduced stress capacity.");
        scene.idle(100);


        scene.overlay().showText(80).attachKeyFrame().placeNearTarget().colored(PonderPalette.GREEN).pointAt(util.vector().blockSurface(gaugePos, Direction.WEST)).text("The provided Rotational Force has a very large stress capacity");
        scene.idle(100);
        scene.world().hideSection(util.select().fromTo(4,1,0,6,3,6), Direction.UP);
        scene.idle(20);
        //scene.world().replaceBlocks(util.select().fromTo(0,0,0,6,4,6), Blocks.AIR.defaultBlockState(), false);
        ElementLink<WorldSectionElement> bbsction=scene.world().showIndependentSectionImmediately(util.select().fromTo(5,3,0,5,6,5));
        scene.world().moveSection(bbsction,util.vector().of(0, -3, 0), 20);
        scene.world().moveSection(scene.world().showIndependentSectionImmediately(util.select().fromTo(4,3,0,4,6,6)),util.vector().of(0, -3, 0), 20);
        scene.world().moveSection(scene.world().showIndependentSectionImmediately(util.select().fromTo(3,6,0,3,6,6)),util.vector().of(0, -3, 0), 20);
        scene.world().cycleBlockProperty(util.grid().at(5, 4,3), SteamEngineBlock.LIT);
        //scene.world().showSection(util.select().fromTo(3,2,3,4,3,4), Direction.WEST);
        scene.idle(40);

        scene.world().setKineticSpeed(util.select().position(4,5,3), 64F);
        scene.world().setKineticSpeed(util.select().fromTo(3,6,4,4,6,4), -32F);
        scene.overlay().showText(80).attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(enginePos)).text("You may use pumps to pump steam so that you will get max stress capacity.");
        scene.idle(100);

        scene.world().hideSection(util.select().fromTo(0,2,2,6,2,2), Direction.UP);
        scene.idle(20);

        ElementLink<WorldSectionElement> section=scene.world().showIndependentSectionImmediately(util.select().fromTo(0,8,2,6,8,2));
        scene.world().setKineticSpeed(util.select().fromTo(0,8,2,6,8,2), -64F);
        scene.world().moveSection(section,util.vector().of(0, -6, 0), 20);
        scene.idle(40);

        scene.overlay().showText(80).attachKeyFrame().placeNearTarget().colored(PonderPalette.RED).pointAt(util.vector().topOf(1,2,2)).text("You can build a structure to make flywheels driving pumps itself.");
        scene.idle(100);
        //scene.world().moveSection(section,util.vector().of(0, 6, 0), 20);
        scene.world().hideIndependentSection(section, Direction.UP);
        //scene.world().hideSection(util.select().fromTo(0,8,2,6,8,2), Direction.UP);
        scene.idle(20);
        scene.world().showSection(util.select().fromTo(0,2,2,6,2,2), Direction.UP);


        scene.idle(30);
        //scene.world().replaceBlocks(util.select().fromTo(5,3,4,5,6,4),Blocks.AIR.defaultBlockState(),false);
        scene.world().hideSection(f2Select, Direction.DOWN);
        scene.world().hideIndependentSection(bbsction, Direction.DOWN);
        scene.idle(30);
      /*  scene.world().showSection(b2Select, Direction.UP);
        scene.world().modifyBlock(util.grid().at(5, 1, 3),state->state.setValue(BurnerBlock.LIT,true),false);
        scene.idle(10);

        scene.idle(7);

        scene.idle(90);
        scene.world().setKineticSpeed(util.select().fromTo(1, 1, 3, 1, 1, 1), 32.0F);
        scene.idle(40);
        scene.world().showSection(util.select().position(cogPos), Direction.SOUTH);
        scene.idle(15);
        scene.effects().rotationSpeedIndicator(cogPos);
        scene.world().showSection(util.select().position(gaugePos), Direction.SOUTH);
        scene.idle(15);

        scene.idle(90);

        scene.world().hideSection(f2Select, Direction.DOWN);
        scene.world().hideSection(b2Select, Direction.DOWN);
        scene.idle(15);*/
        scene.world().setBlock(util.grid().at(5, 1, 3),SPBlocks.CAST_IRON_BURNER.getDefaultState().setValue(BurnerBlock.LIT,true),false);
        scene.world().setBlock(util.grid().at(5, 2, 3),SPBlocks.CAST_IRON_BOILER.getDefaultState(),false);
        scene.world().setBlock(enginePos, SPBlocks.CAST_IRON_STEAM_ENGINE.getDefaultState().setValue(SteamEngineBlock.FACING, Direction.WEST).setValue(SteamEngineBlock.LIT, true), false);
        scene.world().setBlock(wheelPos, SPBlocks.CAST_IRON_FLYWHEEL.getDefaultState().setValue(SteamEngineBlock.FACING, Direction.SOUTH).setValue(OldFlywheelBlock.CONNECTION,OldFlywheelBlock.ConnectionState.LEFT), false);
        scene.world().showSection(f2Select, Direction.DOWN);
        scene.world().showSection(b2Select, Direction.DOWN);
        scene.idle(30);
        scene.world().setKineticSpeed(util.select().fromTo(1, 2, 3, 1, 2, 1),-SPConfig.COMMON.castIronFlywheelSpeed.get());
        scene.idle(50);

        scene.world().hideSection(f2Select, Direction.DOWN);
        scene.world().hideSection(b2Select, Direction.DOWN);
        scene.idle(15);
        scene.world().setBlock(util.grid().at(5, 1, 3),SPBlocks.STEEL_BURNER.getDefaultState().setValue(BurnerBlock.LIT,true),false);
        scene.world().setBlock(util.grid().at(5, 2, 3),SPBlocks.STEEL_BOILER.getDefaultState(),false);
        scene.world().setBlock(enginePos, SPBlocks.STEEL_STEAM_ENGINE.get().defaultBlockState().setValue(SteamEngineBlock.FACING, Direction.WEST).setValue(SteamEngineBlock.LIT, true), false);
        scene.world().setBlock(wheelPos, SPBlocks.STEEL_FLYWHEEL.get().defaultBlockState().setValue(SteamEngineBlock.FACING, Direction.SOUTH).setValue(OldFlywheelBlock.CONNECTION,OldFlywheelBlock.ConnectionState.LEFT), false);
        scene.world().showSection(f2Select, Direction.DOWN);
        scene.world().showSection(b2Select, Direction.DOWN);
        scene.idle(30);
        scene.world().setKineticSpeed(util.select().fromTo(1, 2, 3, 1, 2, 1),-SPConfig.COMMON.steelFlywheelSpeed.get());

        scene.idle(5);
        scene.effects().rotationSpeedIndicator(cogPos);
        scene.idle(5);
        String text3 = flywheel ? "Using Flywheels made of Steel or Cast Iron will increase efficiency and generated capacity of the Flywheel" : "Using Steam Engines made of Steel or Cast Iron will increase efficiency and generated capacity of the Flywheel";
        scene.overlay().showOutline(PonderPalette.MEDIUM,new Object(),f2Select,80);
        scene.overlay().showText(80).attachKeyFrame().placeNearTarget().colored(PonderPalette.MEDIUM).pointAt(util.vector().topOf(enginePos.west())).text(text3);
        scene.idle(80);
        String text4 = "However, power up higher level of "+(flywheel?"flywheel":"engine")+" require higher amount of steam, boiler and burner should match the level.";
        scene.overlay().showText(80).placeNearTarget().colored(PonderPalette.RED).text(text4).pointAt(util.vector().topOf(5,2,3));
        scene.idle(80);
    }

    public static void dynamo(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("dynamo", "Generating Electric energy using a Dynamo");
        scene.configureBasePlate(1, 0, 4);
        scene.world().showSection(util.select().layer(0), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().layer(1), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().layer(2), Direction.DOWN);
        scene.idle(10);

        BlockPos generator = util.grid().at(3, 1, 2);
        BlockPos shaft = util.grid().at(2, 1, 2);
        BlockPos gauge = util.grid().at(1, 1, 2);
        BlockPos cogwheel = util.grid().at(0, 1, 2);
        BlockPos largecog = util.grid().at(0, 2, 3);
        BlockPos redstone = util.grid().at(3, 1, 1);
        BlockPos lever = util.grid().at(3, 1, 0);

        scene.world().setKineticSpeed(util.select().position(largecog), -16.0F);
        scene.world().setKineticSpeed(util.select().position(cogwheel), 32.0F);
        scene.world().setKineticSpeed(util.select().position(shaft), 32.0F);
        scene.world().setKineticSpeed(util.select().position(gauge), 32.0F);
        scene.world().setKineticSpeed(util.select().position(generator), 32.0F);
        scene.idle(5);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The Dynamo generates electric energy (fe) from rotational force")
                .placeNearTarget()
                .pointAt(util.vector().topOf(generator));
        scene.idle(60);

        scene.effects().rotationSpeedIndicator(cogwheel);
        scene.idle(60);

        scene.overlay().showText(50)
                .text("It requires at least 32 RPM to operate")
                .placeNearTarget()
                .pointAt(util.vector().topOf(cogwheel));
        scene.idle(60);

        scene.overlay().showText(50)
                .text("The Dynamos energy production is determined by the input RPM")
                .placeNearTarget()
                .pointAt(util.vector().topOf(generator));
        scene.idle(60);

        scene.overlay().showText(50)
                .text("It has conversion efficiency of 75 Percent")
                .placeNearTarget()
                .pointAt(util.vector().topOf(generator));
        scene.idle(60);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("You can lock the Dynamo with redstone signal so it will not apply stress to the network")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(lever));
        scene.idle(60);

        scene.world().modifyBlock(lever, s -> s.setValue(LeverBlock.POWERED, true), false);
        scene.world().modifyBlock(redstone, s -> s.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(generator, s -> s.setValue(DynamoBlock.REDSTONE_LOCKED, true), false);
        scene.idle(60);
    }
}

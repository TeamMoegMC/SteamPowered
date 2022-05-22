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

package com.teammoeg.steampowered;

import net.minecraftforge.common.ForgeConfigSpec;

public class SPConfig {
    public static class Common {

        public final ForgeConfigSpec.IntValue bronzeFlywheelCapacity;
        public final ForgeConfigSpec.IntValue bronzeFlywheelSpeed;
        public final ForgeConfigSpec.IntValue bronzeFlywheelSteamConsumptionPerTick;
        public final ForgeConfigSpec.IntValue bronzeFlywheelSteamStorage;
        public final ForgeConfigSpec.IntValue castIronFlywheelCapacity;
        public final ForgeConfigSpec.IntValue castIronFlywheelSpeed;
        public final ForgeConfigSpec.IntValue castIronFlywheelSteamConsumptionPerTick;
        public final ForgeConfigSpec.IntValue castIronFlywheelSteamStorage;
        public final ForgeConfigSpec.IntValue steelFlywheelCapacity;
        public final ForgeConfigSpec.IntValue steelFlywheelSpeed;
        public final ForgeConfigSpec.IntValue steelFlywheelSteamConsumptionPerTick;
        public final ForgeConfigSpec.IntValue steelFlywheelSteamStorage;
        
        public final ForgeConfigSpec.IntValue HUPerFuelTick;
        public final ForgeConfigSpec.DoubleValue steamPerWater;
        
        public final ForgeConfigSpec.IntValue bronzeBoilerHU;
        public final ForgeConfigSpec.IntValue castIronBoilerHU;
        public final ForgeConfigSpec.IntValue steelBoilerHU;
        
        public final ForgeConfigSpec.IntValue bronzeBurnerHU;
        public final ForgeConfigSpec.IntValue castIronBurnerHU;
        public final ForgeConfigSpec.IntValue steelBurnerHU;
        public final ForgeConfigSpec.DoubleValue bronzeBurnerEfficiency;
        public final ForgeConfigSpec.DoubleValue castIronBurnerEfficiency;
        public final ForgeConfigSpec.DoubleValue steelBurnerEfficiency;

        public final ForgeConfigSpec.IntValue dynamoFeMaxOut;
        public final ForgeConfigSpec.IntValue dynamoFeCapacity;
        public final ForgeConfigSpec.IntValue dynamoImpact;
        public final ForgeConfigSpec.DoubleValue dynamoEfficiency;

        public final ForgeConfigSpec.DoubleValue bronzeCogwheelImpact;
        public final ForgeConfigSpec.DoubleValue castIronCogwheelImpact;
        public final ForgeConfigSpec.DoubleValue steelCogwheelImpact;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("steam");
            {
                HUPerFuelTick=builder.comment("HU generation for each fuel burning tick. 10HU=1mb of steam. THIS AFFECT ALL BURNERS AND BOILERS!")
                		.defineInRange("HuPerFuelTick",68,0,655360);
                steamPerWater=builder.comment("This defines how many mbs of steam does a mb of water can turn into.")
                		.defineInRange("steamPerWater",12.0,0.0,100000.0);
                
            }
            builder.pop();
            builder.push("boiler").comment("Maximum HU the boiler intakes. 10HU=1mb of steam. ");
            {
                bronzeBoilerHU=builder.defineInRange("bronzeBoilerHu",120,0,1000000);
                castIronBoilerHU=builder.defineInRange("castIronBoilerHu",240,0,1000000);;
                steelBoilerHU=builder.defineInRange("steelBoilerHu",480,0,1000000);;
            }
            builder.pop();
            builder.push("burner").comment("Maximum HU the boiler emits. Note that this does not affect steam per fuel tick. 10HU=1mb of steam. ");
            {
                bronzeBurnerHU=builder.defineInRange("bronzeBurnerHu",120,0,1000000);
                castIronBurnerHU=builder.defineInRange("castIronBurnerHu",240,0,1000000);;
                steelBurnerHU=builder.defineInRange("steelBurnerHu",480,0,1000000);;
            }
            builder.pop();
            builder.push("burnerEfficiency").comment("Fuel Tick to HU Convertion Percentage. 10HU=1mb of steam. ");
            {
                bronzeBurnerEfficiency=builder.defineInRange("bronzeBurnerEfficiency",0.8,0,1.0);
                castIronBurnerEfficiency=builder.defineInRange("castIronBurnerEfficiency",0.9,0,1.0);
                steelBurnerEfficiency=builder.defineInRange("steelBurnerEfficiency",1,0,1.0);
            }
            builder.pop();
            builder.push("flywheel");
            {
                builder.push("bronze_flywheel");
                {
                    bronzeFlywheelCapacity = builder.defineInRange("bronzeFlywheelCapacity",1024, 0, 8192);
                    bronzeFlywheelSpeed = builder.defineInRange("bronzeFlywheelSpeed", 32, 0, 8192);
                    bronzeFlywheelSteamConsumptionPerTick = builder.defineInRange("bronzeFlywheelSteamConsumptionPerTick", 12, 0, 8192);
                    bronzeFlywheelSteamStorage = builder.defineInRange("bronzeFlywheelSteamStorage", 32000, 0, 1048576);

                }
                builder.pop();
                builder.push("cast_iron_flywheel");
                {
                    castIronFlywheelCapacity = builder.defineInRange("castIronFlywheelCapacity", 2048, 0, 8192);
                    castIronFlywheelSpeed = builder.defineInRange("castIronFlywheelSpeed", 32, 0, 8192);
                    castIronFlywheelSteamConsumptionPerTick = builder.defineInRange("castIronFlywheelSteamConsumptionPerTick", 24, 0, 8192);
                    castIronFlywheelSteamStorage = builder.defineInRange("castIronFlywheelSteamStorage", 64000, 0, 1048576);
                }
                builder.pop();
                builder.push("steel_flywheel");
                {
                    steelFlywheelCapacity = builder.defineInRange("steelFlywheelCapacity", 4096, 0, 8192);
                    steelFlywheelSpeed = builder.defineInRange("steelFlywheelSpeed", 32, 0, 8192);
                    steelFlywheelSteamConsumptionPerTick = builder.defineInRange("steelFlywheelSteamConsumptionPerTick", 48, 0, 1048576);
                    steelFlywheelSteamStorage = builder.defineInRange("steelFlywheelSteamStorage", 96000, 0, 1048576);
                }
                builder.pop();
            }
            builder.pop();

            builder.push("dynamo").comment("If dynamo is disabled in the server config, the following will be ignored!");
            {
                dynamoFeMaxOut = builder.defineInRange("dynamoFeMaxOut", 256, 0, 8192);
                dynamoFeCapacity = builder.defineInRange("dynamoFeCapacity", 2048, 0, 8192);
                dynamoImpact = builder.defineInRange("dynamoImpact", 16, 0, 8192);
                dynamoEfficiency = builder.defineInRange("dynamoEfficiency", 0.75D, 0, 1);
            }
            builder.pop();

            builder.push("cogwheel").comment("For those who want to make the game more challenging, you can add stress impact to cogwheels!");
            {
                bronzeCogwheelImpact = builder.defineInRange("bronzeCogwheelImpact", 0.0D, 0, 1);
                castIronCogwheelImpact = builder.defineInRange("castIronCogwheelImpact", 0.0D, 0, 1);
                steelCogwheelImpact = builder.defineInRange("steelCogwheelImpact", 0.0D, 0, 1);
            }
            builder.pop();
        }
    }

    public static class Server {

        //public final ForgeConfigSpec.BooleanValue disableDynamo;

        Server(ForgeConfigSpec.Builder builder) {
            //Unproper comment make our main developer mad XXD
            /*builder.push("dynamo");
            {
                disableDynamo = builder
                        .comment("Set to true to DISABLE the DynamoBlock ONLY when Create: Crafts & Additions is loaded")
                        .comment("Create: Crafts & Additions is a mod which provides a similar electricity generation device called Alternator")
                        .comment("For pack developers who think having two similar devices is superfluous, you have two choices:")
                        .comment("Either you can set this config option to true and remove the DynamoBlock recipe through datapack")
                        .comment("Or if you instead want to use our DynamoBlock, you can remove the Alternator's recipe through datapack")
                        .comment("How to make datapack? Learn it from here: https://minecraft.gamepedia.com/Data_Pack")
                        .comment("However, please refer to the following: ")
                        .comment("We offers a redstone lock mechanism so that you can disconnect the DynamoBlock with redstone signal")
                        .comment("We offers a different model which provides different textures according to redstone status")
                        .comment("Clarification: As of Ver 1.1.1, Create: Steam Powered's DynamoBlock ONLY adapts from")
                        .comment("the code implementation of the Alternator created by MRH0, which is under MIT License")
                        .comment("We acknowledge and appreciate the great work done by MRH0. We learn from his code")
                        .comment("MRH0 is also under our credits list in mods.toml description")
                        .define("disableDynamo", false);
            }
            builder.pop();*/
        }
    }

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final Common COMMON;
    public static final Server SERVER;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        COMMON = new Common(COMMON_BUILDER);
        SERVER = new Server(SERVER_BUILDER);
        SERVER_CONFIG = SERVER_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

}

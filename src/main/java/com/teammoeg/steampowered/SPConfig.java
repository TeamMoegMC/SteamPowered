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

        public final ForgeConfigSpec.IntValue dynamoFeMaxIn;
        public final ForgeConfigSpec.IntValue dynamoFeMaxOut;
        public final ForgeConfigSpec.IntValue dynamoFeCapacity;
        public final ForgeConfigSpec.IntValue dynamoImpact;
        public final ForgeConfigSpec.DoubleValue dynamoEfficiency;

        public final ForgeConfigSpec.DoubleValue bronzeCogwheelImpact;
        public final ForgeConfigSpec.DoubleValue castIronCogwheelImpact;
        public final ForgeConfigSpec.DoubleValue steelCogwheelImpact;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("flywheel");
            {
                builder.push("bronze_flywheel");
                {
                    bronzeFlywheelCapacity = builder.defineInRange("bronzeFlywheelCapacity", 32, 0, 8192);
                    bronzeFlywheelSpeed = builder.defineInRange("bronzeFlywheelSpeed", 32, 0, 8192);
                    bronzeFlywheelSteamConsumptionPerTick = builder.defineInRange("bronzeFlywheelSteamConsumptionPerTick", 12, 0, 8192);
                    bronzeFlywheelSteamStorage = builder.defineInRange("bronzeFlywheelSteamStorage", 32000, 0, 1048576);

                }
                builder.pop();
                builder.push("cast_iron_flywheel");
                {
                    castIronFlywheelCapacity = builder.defineInRange("castIronFlywheelCapacity", 64, 0, 8192);
                    castIronFlywheelSpeed = builder.defineInRange("castIronFlywheelSpeed", 32, 0, 8192);
                    castIronFlywheelSteamConsumptionPerTick = builder.defineInRange("castIronFlywheelSteamConsumptionPerTick", 24, 0, 8192);
                    castIronFlywheelSteamStorage = builder.defineInRange("castIronFlywheelSteamStorage", 64000, 0, 1048576);
                }
                builder.pop();
                builder.push("steel_flywheel");
                {
                    steelFlywheelCapacity = builder.defineInRange("steelFlywheelCapacity", 96, 0, 8192);
                    steelFlywheelSpeed = builder.defineInRange("steelFlywheelSpeed", 32, 0, 8192);
                    steelFlywheelSteamConsumptionPerTick = builder.defineInRange("steelFlywheelSteamConsumptionPerTick", 48, 0, 1048576);
                    steelFlywheelSteamStorage = builder.defineInRange("steelFlywheelSteamStorage", 96000, 0, 1048576);
                }
                builder.pop();
            }
            builder.pop();

            builder.push("dynamo").comment("If dynamo is disabled in the server config, the following will be ignored!");
            {
                dynamoFeMaxIn = builder.defineInRange("dynamoFeMaxIn", 0, 0, 8192);
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

        public final ForgeConfigSpec.BooleanValue allowUnverifiedContraption;
        public final ForgeConfigSpec.BooleanValue allowCartAssembler;
        public final ForgeConfigSpec.BooleanValue disableSteamPoweredDynamo;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("createmodify");
            {
                allowUnverifiedContraption = builder.comment("Set to false to automatically disassemble contraptions formed before this mod installed").define("allowUnverifiedContraption",true);
                allowCartAssembler = builder.comment("Cart Assembler is not very \"Realistic\", so you can choose to disable it.").define("allowCartAssembler", true);
            }
            builder.pop();
            builder.push("dynamo");
            {
                disableSteamPoweredDynamo = builder.comment("Set to false to enable this mod's dynamo.").define("disableSteamPoweredDynamo", true);
            }
            builder.pop();
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

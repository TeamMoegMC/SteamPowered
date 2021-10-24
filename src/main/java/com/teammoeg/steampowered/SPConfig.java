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

        public final ForgeConfigSpec.IntValue alternatorFeMaxIn;
        public final ForgeConfigSpec.IntValue alternatorFeMaxOut;
        public final ForgeConfigSpec.IntValue alternatorFeCapacity;
        public final ForgeConfigSpec.IntValue alternatorImpact;
        public final ForgeConfigSpec.DoubleValue alternatorEfficiency;

        public final ForgeConfigSpec.DoubleValue bronzeCogwheelImpact;
        public final ForgeConfigSpec.DoubleValue castIronCogwheelImpact;
        public final ForgeConfigSpec.DoubleValue steelCogwheelImpact;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("flywheel");
            {
                bronzeFlywheelCapacity = builder.defineInRange("bronzeFlywheelCapacity", 32, 0, 8192);
                bronzeFlywheelSpeed = builder.defineInRange("bronzeFlywheelSpeed", 32, 0, 8192);
                bronzeFlywheelSteamConsumptionPerTick = builder.defineInRange("bronzeFlywheelSteamConsumptionPerTick", 24, 0, 8192);
                bronzeFlywheelSteamStorage = builder.defineInRange("bronzeFlywheelSteamStorage", 32000, 0, 1048576);

                castIronFlywheelCapacity = builder.defineInRange("castIronFlywheelCapacity", 64, 0, 8192);
                castIronFlywheelSpeed = builder.defineInRange("castIronFlywheelSpeed", 32, 0, 8192);
                castIronFlywheelSteamConsumptionPerTick = builder.defineInRange("castIronFlywheelSteamConsumptionPerTick", 48, 0, 8192);
                castIronFlywheelSteamStorage = builder.defineInRange("castIronFlywheelSteamStorage", 64000, 0, 1048576);

                steelFlywheelCapacity = builder.defineInRange("steelFlywheelCapacity", 96, 0, 8192);
                steelFlywheelSpeed = builder.defineInRange("steelFlywheelSpeed", 32, 0, 8192);
                steelFlywheelSteamConsumptionPerTick = builder.defineInRange("steelFlywheelSteamConsumptionPerTick", 72, 0, 1048576);
                steelFlywheelSteamStorage = builder.defineInRange("steelFlywheelSteamStorage", 96000, 0, 1048576);
            }
            builder.pop();

            builder.push("alternator");
            {
                alternatorFeMaxIn = builder.defineInRange("alternatorFeMaxIn", 0, 0, 8192);
                alternatorFeMaxOut = builder.defineInRange("alternatorFeMaxOut", 256, 0, 8192);
                alternatorFeCapacity = builder.defineInRange("alternatorFeCapacity", 2048, 0, 8192);
                alternatorImpact = builder.defineInRange("alternatorImpact", 16, 0, 8192);
                alternatorEfficiency = builder.defineInRange("alternatorEfficiency", 0.75D, 0, 1);
            }
            builder.pop();

            builder.push("cogwheel");
            {
                bronzeCogwheelImpact = builder.defineInRange("bronzeCogwheelImpact", 0.1D, 0, 1);
                castIronCogwheelImpact = builder.defineInRange("castIronCogwheelImpact", 0.05D, 0, 1);
                steelCogwheelImpact = builder.defineInRange("steelCogwheelImpact", 0.02D, 0, 1);
            }
            builder.pop();
        }
    }

    public static class Server {

        public final ForgeConfigSpec.BooleanValue allowUnverifiedContraption;
        public final ForgeConfigSpec.BooleanValue allowCartAssembler;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("createmodify");
            {
                allowUnverifiedContraption = builder.comment("Set to False to automatically disassemble contraptions formed before this mod installed").define("allowUnverifiedContraption", false);
                allowCartAssembler = builder.comment("Cart Assembler is not very \"Realistic\",So You can choose to disable its use").define("allowCartAssembler", false);
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

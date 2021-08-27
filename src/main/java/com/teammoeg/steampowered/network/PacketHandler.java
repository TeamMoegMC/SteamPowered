package com.teammoeg.steampowered.network;

import com.teammoeg.steampowered.SteamPowered;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String VERSION = Integer.toString(1);
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(SteamPowered.rl("network"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void send(PacketDistributor.PacketTarget target, Object message) {
        CHANNEL.send(target, message);
    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static SimpleChannel get() {
        return CHANNEL;
    }

    @SuppressWarnings("UnusedAssignment")
    public static void register() {
        int id = 0;

        CHANNEL.registerMessage(id++, TileSyncPacket.class, TileSyncPacket::encode, TileSyncPacket::new, TileSyncPacket::handle);
    }
}
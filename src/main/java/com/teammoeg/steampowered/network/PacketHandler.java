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
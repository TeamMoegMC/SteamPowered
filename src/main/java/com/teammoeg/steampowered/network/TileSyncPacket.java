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

import com.teammoeg.steampowered.client.ClientUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class TileSyncPacket {
    private BlockPos pos;
    private CompoundNBT nbt;

    public TileSyncPacket(ITileSync tile, CompoundNBT nbt) {
        this.pos = tile.getSyncPos();
        this.nbt = nbt;
    }

    public TileSyncPacket(PacketBuffer buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.nbt = buf.readNbt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(this.pos.getX()).writeInt(this.pos.getY()).writeInt(this.pos.getZ());
        buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) context.get();
        if (ctx.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ctx.enqueueWork(() -> {
                ServerWorld world = ((ServerPlayerEntity) Objects.requireNonNull(ctx.getSender())).getLevel();
                if (world.isAreaLoaded(this.pos, 1)) {
                    TileEntity tile = world.getBlockEntity(this.pos);
                    if (tile instanceof ITileSync) {
                        ((ITileSync) tile).receiveFromClient(this.nbt);
                    }
                }

            });
        } else {
            ctx.enqueueWork(() -> {
                World world = ClientUtils.getClientWorld();
                if (world != null) {
                    TileEntity tile = world.getBlockEntity(this.pos);
                    if (tile instanceof ITileSync) {
                        ((ITileSync) tile).receiveFromServer(this.nbt);
                    }
                }

            });
        }
        ctx.setPacketHandled(true);
    }
}

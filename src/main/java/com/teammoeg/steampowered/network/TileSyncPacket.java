package com.teammoeg.steampowered.network;

import com.teammoeg.steampowered.client.ClientUtils;
import com.teammoeg.steampowered.tileentity.SteamEngineTileEntity;
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

    public TileSyncPacket(SteamEngineTileEntity tile, CompoundNBT nbt) {
        this.pos = tile.getBlockPos();
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
                    if (tile instanceof SteamEngineTileEntity) {
                        ((SteamEngineTileEntity) tile).receiveFromClient(this.nbt);
                    }
                }

            });
        } else {
            ctx.enqueueWork(() -> {
                World world = ClientUtils.getClientWorld();
                if (world != null) {
                    TileEntity tile = world.getBlockEntity(this.pos);
                    if (tile instanceof SteamEngineTileEntity) {
                        ((SteamEngineTileEntity) tile).receiveFromServer(this.nbt);
                    }
                }

            });
        }
        ctx.setPacketHandled(true);
    }
}

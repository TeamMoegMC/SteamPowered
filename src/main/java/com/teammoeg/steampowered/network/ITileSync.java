package com.teammoeg.steampowered.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public interface ITileSync {
    void receiveFromServer(CompoundNBT message);
    void receiveFromClient(CompoundNBT message);
    BlockPos getSyncPos();
}

/**
 * Available under MIT the license more info at: https://tldrlegal.com/license/mit-license
 *
 * MIT License
 *
 * Copyright 2021 MRH0
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction,
 * including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom t
 * he Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.teammoeg.steampowered.content.alternator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Adapted from: Create: Crafts & Additions
 * @author MRH0
 */
public class InternalEnergyStorage extends EnergyStorage {
    public InternalEnergyStorage(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public InternalEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("energy", energy);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        setEnergy(nbt.getInt("energy"));
    }

    public CompoundTag write(CompoundTag nbt, String name) {
        nbt.putInt("energy_" + name, energy);
        return nbt;
    }

    public void read(CompoundTag nbt, String name) {
        setEnergy(nbt.getInt("energy_" + name));
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public int internalConsumeEnergy(int consume) {
        int oenergy = energy;
        energy = Math.max(0, energy - consume);
        return oenergy - energy;
    }

    public int internalProduceEnergy(int produce) {
        int oenergy = energy;
        energy = Math.min(capacity, energy + produce);
        return oenergy - energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Deprecated
    public void outputToSide(Level world, BlockPos pos, Direction side, int max) {
        BlockEntity te = world.getBlockEntity(pos.relative(side));
        if (te == null)
            return;
        LazyOptional<IEnergyStorage> opt = te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
        IEnergyStorage ies = opt.orElse(null);
        if (ies == null)
            return;
        int ext = this.extractEnergy(max, false);
        this.receiveEnergy(ext - ies.receiveEnergy(ext, false), false);
    }

    @Override
    public String toString() {
        return getEnergyStored() + "/" + getMaxEnergyStored();
    }
}

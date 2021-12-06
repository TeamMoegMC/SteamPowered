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

package com.teammoeg.steampowered.content.alternator;

import java.util.List;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.teammoeg.steampowered.SPConfig;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Adapted from: Create: Crafts & Additions under the MIT License
 * @author MRH0
 * @author yuesha-yc
 */
public class DynamoTileEntity extends KineticTileEntity {

    protected final InternalEnergyStorage energy;
    private LazyOptional<IEnergyStorage> lazyEnergy;
    private boolean redstoneLocked = false;
    
    public static final int MAX_FE_OUT = SPConfig.COMMON.dynamoFeMaxOut.get(); // FE Output
    public static final int FE_CAPACITY = SPConfig.COMMON.dynamoFeCapacity.get(); // FE Storage
    public static final int IMPACT = SPConfig.COMMON.dynamoImpact.get(); // Impact on network
    public static final double EFFICIENCY = SPConfig.COMMON.dynamoEfficiency.get(); // Efficiency

    public DynamoTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
        energy = new InternalEnergyStorage(FE_CAPACITY, 0, MAX_FE_OUT);
        lazyEnergy = LazyOptional.of(() -> energy);
    }

    @Override
    public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
        if (this.getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED)) {
            tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent("tooltip.steampowered.dynamo.locked").withStyle(TextFormatting.RED)));
            return true;
        }
		tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent("tooltip.steampowered.energy.production").withStyle(TextFormatting.GRAY)));
		tooltip.add(new StringTextComponent(spacing).append(new StringTextComponent(" " + format(getEnergyProductionRate((int) (isSpeedRequirementFulfilled() ? getSpeed() : 0))) + "fe/t ") // fix
		        .withStyle(TextFormatting.AQUA)).append(Lang.translate("gui.goggles.at_current_speed").withStyle(TextFormatting.DARK_GRAY)));
		return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    private static String format(int n) {
        if (n > 1000000)
            return Math.round(n / 100000d) / 10d + "M";
        if (n > 1000)
            return Math.round(n / 100d) / 10d + "K";
        return n + "";
    }

    @Override
    public float calculateStressApplied() {
        if (getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED)) {
            this.lastStressApplied = 0;
            return 0;
        }
		this.lastStressApplied = IMPACT;
		return IMPACT;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY && (isEnergyInput(side) || isEnergyOutput(side)))// && !level.isClientSide
            return lazyEnergy.cast();
        return super.getCapability(cap, side);
    }

    public boolean isEnergyInput(Direction side) {
        return false;
    }

    public boolean isEnergyOutput(Direction side) {
        return side != getBlockState().getValue(DynamoBlock.FACING).getOpposite();
    }

    @Override
    public void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        energy.read(compound);
        redstoneLocked = compound.getBoolean("redstonelocked");
    }

    @Override
    public void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        energy.write(compound);
        compound.putBoolean("redstonelocked", redstoneLocked);
    }

    private boolean firstTickState = true;

    @Override
    public void tick() {
        super.tick();
        if (level != null && level.isClientSide())
            return;

        if (this.getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED))
            return;

        if (firstTickState)
            firstTick();
        firstTickState = false;

        if (Math.abs(getSpeed()) > 0 && isSpeedRequirementFulfilled())
            energy.internalProduceEnergy(getEnergyProductionRate((int) getSpeed()));

        for (Direction d : Direction.values()) {
            if (!isEnergyOutput(d))
                continue;
            IEnergyStorage ies = getCachedEnergy(d);
            if (ies == null)
                continue;
            int ext = energy.extractEnergy(ies.receiveEnergy(MAX_FE_OUT, true), false);
            ies.receiveEnergy(ext, false);
        }
    }

    public static int getEnergyProductionRate(int rpm) {
        rpm = Math.abs(rpm);
        return (int) (Math.abs(rpm) * EFFICIENCY);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        lazyEnergy.invalidate();
    }

    public void firstTick() {
        updateCache();
    }

    public void updateCache() {
        if (level.isClientSide())
            return;
        for (Direction side : Direction.values()) {
            TileEntity te = level.getBlockEntity(worldPosition.relative(side));
            if (te == null) {
                setCache(side, LazyOptional.empty());
                continue;
            }
            LazyOptional<IEnergyStorage> le = te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
            setCache(side, le);
        }
    }

    private LazyOptional<IEnergyStorage> escacheUp = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> escacheDown = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> escacheNorth = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> escacheEast = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> escacheSouth = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> escacheWest = LazyOptional.empty();

    public void setCache(Direction side, LazyOptional<IEnergyStorage> storage) {
        switch (side) {
            case DOWN:
                escacheDown = storage;
                break;
            case EAST:
                escacheEast = storage;
                break;
            case NORTH:
                escacheNorth = storage;
                break;
            case SOUTH:
                escacheSouth = storage;
                break;
            case UP:
                escacheUp = storage;
                break;
            case WEST:
                escacheWest = storage;
                break;
        }
    }

    public IEnergyStorage getCachedEnergy(Direction side) {
        switch (side) {
            case DOWN:
                return escacheDown.orElse(null);
            case EAST:
                return escacheEast.orElse(null);
            case NORTH:
                return escacheNorth.orElse(null);
            case SOUTH:
                return escacheSouth.orElse(null);
            case UP:
                return escacheUp.orElse(null);
            case WEST:
                return escacheWest.orElse(null);
        }
        return null;
    }

    @Override
    public World getWorld() {
        return getLevel();
    }
}

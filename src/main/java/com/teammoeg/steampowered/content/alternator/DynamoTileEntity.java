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

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
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

    public DynamoTileEntity(BlockEntityType<?> typeIn) {
        super(typeIn);
        energy = new InternalEnergyStorage(FE_CAPACITY, 0, MAX_FE_OUT);
        lazyEnergy = LazyOptional.of(() -> energy);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (this.getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED)) {
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent("tooltip.steampowered.dynamo.locked").withStyle(ChatFormatting.RED)));
            return true;
        }
		tooltip.add(new TextComponent(spacing).append(new TranslatableComponent("tooltip.steampowered.energy.production").withStyle(ChatFormatting.GRAY)));
		tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + format(getEnergyProductionRate((int) (isSpeedRequirementFulfilled() ? getSpeed() : 0))) + "fe/t ") // fix
		        .withStyle(ChatFormatting.AQUA)).append(Lang.translate("gui.goggles.at_current_speed").withStyle(ChatFormatting.DARK_GRAY)));
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
        if (cap == CapabilityEnergy.ENERGY && side==getBlockState().getValue(DynamoBlock.FACING))// && !level.isClientSide
            return lazyEnergy.cast();
        return super.getCapability(cap, side);
    }

    public void fromTag(BlockState state, CompoundTag compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        energy.read(compound);
        redstoneLocked = compound.getBoolean("redstonelocked");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        energy.write(compound);
        compound.putBoolean("redstonelocked", redstoneLocked);
    }


    @Override
    public void tick() {
        super.tick();
        if (level != null && level.isClientSide())
            return;
        if (this.getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED))
            return;
        if (Math.abs(getSpeed()) > 0 && isSpeedRequirementFulfilled())
            energy.internalProduceEnergy(getEnergyProductionRate((int) getSpeed()));
    	Direction side=this.getBlockState().getValue(DynamoBlock.FACING);
        BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
        if (te != null) {
	        te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite())
	        .ifPresent(ies->ies.receiveEnergy(energy.extractEnergy(ies.receiveEnergy(MAX_FE_OUT, true), false), false));
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

    public Level getWorld() {
        return getLevel();
    }
}

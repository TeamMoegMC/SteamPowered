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

package com.teammoeg.steampowered.content.burner;

import java.util.List;

import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.teammoeg.steampowered.SPConfig;

import com.teammoeg.steampowered.oldcreatestuff.IGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class BurnerBlockEntity extends SmartBlockEntity implements IGoggleInformation {
    private ItemStackHandler inv = new ItemStackHandler() {

        @Override
        public boolean isItemValid(int slot,ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) != 0 && stack.getCraftingRemainingItem().isEmpty();
        }

    };
    int HURemain;
    private LazyOptional<IItemHandler> holder = LazyOptional.of(() -> inv);

    public BurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        inv.deserializeNBT(nbt.getCompound("inv"));
        HURemain = nbt.getInt("hu");
        super.read(nbt, clientPacket);
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.put("inv", inv.serializeNBT());
        nbt.putInt("hu", HURemain);
        super.write(nbt, clientPacket);
    }

//    @Override
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        CompoundTag nbt = new CompoundTag();
//        this.writeCustomNBT(nbt);
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//
//    @Override
//    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//        this.readCustomNBT(pkt.getTag());
//    }
//
//    @Override
//    public CompoundTag getUpdateTag() {
//        CompoundTag nbt = super.getUpdateTag();
//        writeCustomNBT(nbt);
//        return nbt;
//    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.holder.isPresent()) {
            this.refreshCapability();
        }
        return cap == ForgeCapabilities.ITEM_HANDLER ? holder.cast() : super.getCapability(cap, side);
    }

    private void refreshCapability() {
        LazyOptional<IItemHandler> oldCap = this.holder;
        this.holder = LazyOptional.of(() -> {
            return this.inv;
        });
        oldCap.invalidate();
    }

    public void tick() {
        if (level != null && !level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            int emit = getHuPerTick();
            while (HURemain < emit && this.consumeFuel()) ;
            if (HURemain < emit) {
                if (HURemain > 0) {
                    emitHeat(HURemain);
                    HURemain = 0;
                    this.setChanged();
                    this.level.sendBlockUpdated(worldPosition, state, state, 3);
                }
                if(state.getValue(BurnerBlock.LIT))
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, false));
            } else {
                HURemain -= emit;
                emitHeat(emit);
                this.setChanged();
                if(!state.getValue(BurnerBlock.LIT))
                    this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BurnerBlock.LIT, true));
                else this.level.sendBlockUpdated(worldPosition, state, state, 3);
            }
        }
    }

    private boolean consumeFuel() {
    	if(this.getBlockState().getValue(BurnerBlock.REDSTONE_LOCKED))return false;
        int time = ForgeHooks.getBurnTime(inv.getStackInSlot(0), RecipeType.SMELTING);
        if (time <= 0) return false;
        inv.getStackInSlot(0).shrink(1);
        HURemain += time * SPConfig.COMMON.HUPerFuelTick.get()*getEfficiency();//2.4HU/t

        return true;
    }

    protected void emitHeat(float value) {
        BlockEntity receiver = level.getBlockEntity(this.getBlockPos().above());
        if (receiver instanceof IHeatReceiver) {
            ((IHeatReceiver) receiver).commitHeat(value);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(componentSpacing.plainCopy().append(Component.translatable("tooltip.steampowered.burner.hu", HURemain).withStyle(ChatFormatting.GOLD)));
        if(!inv.getStackInSlot(0).isEmpty())
        tooltip.add(componentSpacing.plainCopy().append(Component.translatable("tooltip.steampowered.burner.item", inv.getStackInSlot(0).getCount(), inv.getStackInSlot(0).getItem().getName(inv.getStackInSlot(0))).withStyle(ChatFormatting.GRAY)));
        return true;
    }

    /*
     * HU per tick max, 10HU=1 steam
     * */
    protected abstract int getHuPerTick();
    protected abstract double getEfficiency();

    public static float getBoilerHeat(Level level, BlockPos pos, BlockState state) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof BurnerBlockEntity burner) {
            if (burner.HURemain > 0 || state.getValue(BurnerBlock.LIT)) {
                return 1;
            }
        }
        return BoilerHeater.NO_HEAT;
    }
}

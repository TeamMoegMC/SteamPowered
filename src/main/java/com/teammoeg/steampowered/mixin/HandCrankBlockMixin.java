package com.teammoeg.steampowered.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.contraptions.components.crank.HandCrankBlock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

@Mixin(HandCrankBlock.class)
public class HandCrankBlockMixin {
	/**
	 * @author khjxiaogu
	 * @reason Disable fake player from making energy
	 * */
	@Inject(at = @At("INVOKE"), method = "use", cancellable = true,remap=true)
	public void use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,BlockRayTraceResult hit,CallbackInfoReturnable<ActionResultType> ci) {
		if(player instanceof FakePlayer) {
			worldIn.destroyBlock(pos,true);
			ci.setReturnValue(ActionResultType.FAIL);
		}
	}
}

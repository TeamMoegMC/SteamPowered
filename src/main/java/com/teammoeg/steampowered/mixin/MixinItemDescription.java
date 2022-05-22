package com.teammoeg.steampowered.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.utility.Lang;
import com.teammoeg.steampowered.SPConfig;
import com.teammoeg.steampowered.content.engine.BronzeSteamEngineBlock;
import com.teammoeg.steampowered.content.engine.CastIronSteamEngineBlock;
import com.teammoeg.steampowered.content.engine.SteamEngineBlock;
import com.teammoeg.steampowered.content.engine.SteelSteamEngineBlock;

import net.minecraft.block.Block;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

@Mixin(ItemDescription.class)
public class MixinItemDescription {

	public MixinItemDescription() {
	}
	@Inject(at=@At("HEAD"),method="generatorSpeed",remap=false,cancellable=true)
	
	private static void sp$speed(Block block, ITextComponent unitRPM,CallbackInfoReturnable<IFormattableTextComponent> cbi) {
		if(block instanceof SteamEngineBlock) {
			if(block instanceof BronzeSteamEngineBlock)
				cbi.setReturnValue(Lang.translate("tooltip.generationSpeed", SPConfig.COMMON.bronzeFlywheelSpeed.get(),unitRPM));
			else if(block instanceof CastIronSteamEngineBlock)
				cbi.setReturnValue(Lang.translate("tooltip.generationSpeed", SPConfig.COMMON.castIronFlywheelSpeed.get(),unitRPM));
			else if(block instanceof SteelSteamEngineBlock)
				cbi.setReturnValue(Lang.translate("tooltip.generationSpeed", SPConfig.COMMON.steelFlywheelSpeed.get(),unitRPM));
		}
	}
}

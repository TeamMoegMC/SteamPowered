package com.teammoeg.steampowered.mixin.server;

import com.simibubi.create.content.contraptions.components.structureMovement.mounted.CartAssemblerTileEntity;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.teammoeg.steampowered.SPConfig;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CartAssemblerTileEntity.class)
public abstract class MixinCartAssemblerTileEntity extends SmartTileEntity {
    public MixinCartAssemblerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    /**
     * @author khjxiaogu
     * @reason config disable cart assembly
     */
    @Inject(at = @At("HEAD"), method = "tryAssemble", remap = false, cancellable = true)
    public void tryAssemble(AbstractMinecartEntity cart, CallbackInfo cbi) {
        if (!SPConfig.SERVER.allowCartAssembler.get()) {
            if (cart == null)
                return;

            if (!isMinecartUpdateValid())
                return;
            resetTicksSinceMinecartUpdate();
            disassemble(level, worldPosition, cart);
            cbi.cancel();

        }

    }

    @Shadow(remap = false)
    protected abstract void disassemble(World world, BlockPos pos, AbstractMinecartEntity cart);

    @Shadow(remap = false)
    public abstract void resetTicksSinceMinecartUpdate();

    @Shadow(remap = false)
    public abstract boolean isMinecartUpdateValid();
}

package com.teammoeg.steampowered.content.burner;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BurnerBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BurnerBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing).setValue(LIT, Boolean.valueOf(false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void stepOn(World w, BlockPos p, Entity e) {
        if (w.getBlockState(p).getValue(LIT) == true)
            if (e instanceof LivingEntity)
                e.hurt(DamageSource.HOT_FLOOR, 2);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT).add(FACING));
    }

    @Override
    public ActionResultType use(BlockState bs, World w, BlockPos bp, PlayerEntity pe, Hand h, BlockRayTraceResult br) {
        if (pe.getItemInHand(h).isEmpty()) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
            ItemStack is = cap.getStackInSlot(0);
            if (!is.isEmpty()) {
                pe.setItemInHand(h, cap.extractItem(0, is.getCount(), false));
                return ActionResultType.SUCCESS;
            }
        } else if (ForgeHooks.getBurnTime(pe.getItemInHand(h)) != 0) {
            IItemHandler cap = w.getBlockEntity(bp).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
            pe.setItemInHand(h, cap.insertItem(0, pe.getItemInHand(h), false));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }


}

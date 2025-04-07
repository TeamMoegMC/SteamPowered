package com.teammoeg.steampowered.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.teammoeg.steampowered.block.SPBlockPartials;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlock;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlockEntity;
import dev.engine_room.flywheel.api.backend.Backend;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AbstractSPFlywheelRenderer extends KineticBlockEntityRenderer<OldFlywheelBlockEntity> {
    public AbstractSPFlywheelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(OldFlywheelBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
        if (!VisualizationManager.supportsVisualization(te.getLevel())) {
            BlockState blockState = te.getBlockState();
            OldFlywheelBlockEntity wte = te;
            float speed = wte.visualSpeed.get(partialTicks) * 3.0F / 10.0F;
            float angle = wte.angle + speed * partialTicks;
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            if (OldFlywheelBlock.isConnected(blockState)) {
                Direction connection = OldFlywheelBlock.getConnection(blockState);
                light = LevelRenderer.getLightColor(te.getLevel(), blockState, te.getBlockPos().relative(connection));
                float rotation = connection.getAxis() == Direction.Axis.X ^ connection.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? -angle : angle;
                boolean flip = blockState.getValue(OldFlywheelBlock.CONNECTION) == OldFlywheelBlock.ConnectionState.LEFT;
                this.transformConnector(this.rotateToFacing(CachedBuffers.partial(SPBlockPartials.BRONZE_FLYWHEEL_UPPER_ROTATING, blockState), connection), true, true, rotation, flip).light(light).renderInto(ms, vb);
                this.transformConnector(this.rotateToFacing(CachedBuffers.partial(SPBlockPartials.BRONZE_FLYWHEEL_LOWER_ROTATING, blockState), connection), false, true, rotation, flip).light(light).renderInto(ms, vb);
                this.transformConnector(this.rotateToFacing(CachedBuffers.partial(SPBlockPartials.BRONZE_FLYWHEEL_UPPER_SLIDING, blockState), connection), true, false, rotation, flip).light(light).renderInto(ms, vb);
                this.transformConnector(this.rotateToFacing(CachedBuffers.partial(SPBlockPartials.BRONZE_FLYWHEEL_LOWER_SLIDING, blockState), connection), false, false, rotation, flip).light(light).renderInto(ms, vb);
            }
            this.renderFlywheel(te, ms, light, blockState, angle, vb);
        }
    }

    private void renderFlywheel(KineticBlockEntity te, PoseStack ms, int light, BlockState blockState, float angle, VertexConsumer vb) {
        @SuppressWarnings("deprecation")
        BlockState referenceState = blockState.rotate(Rotation.CLOCKWISE_90);
        Direction facing = (Direction) referenceState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        SuperByteBuffer wheel = CachedBuffers.partialFacing(SPBlockPartials.BRONZE_FLYWHEEL, referenceState, facing);
        kineticRotationTransform(wheel, te, ((Direction) blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)).getAxis(), AngleHelper.rad((double) angle), light);
        wheel.renderInto(ms, vb);
    }

    protected SuperByteBuffer getRotatedModel(KineticBlockEntity te) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, te.getBlockState(), ((Direction) te.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)).getOpposite());
    }

    protected SuperByteBuffer transformConnector(SuperByteBuffer buffer, boolean upper, boolean rotating, float angle, boolean flip) {
        float shift = upper ? 0.25F : -0.125F;
        float offset = 0.25F;
        float radians = (float) ((double) (angle / 180.0F) * 3.141592653589793D);
        float shifting = Mth.sin(radians) * shift + offset;
        float maxAngle = upper ? -5.0F : -15.0F;
        float minAngle = upper ? -45.0F : 5.0F;
        float barAngle = 0.0F;
        if (rotating) {
            barAngle = Mth.lerp((Mth.sin((float) ((double) radians + 1.5707963267948966D)) + 1.0F) / 2.0F, minAngle, maxAngle);
        }

        float pivotX = (upper ? 8.0F : 3.0F) / 16.0F;
        float pivotY = (upper ? 8.0F : 2.0F) / 16.0F;
        float pivotZ = (upper ? 23.0F : 21.5F) / 16.0F;
        buffer.translate(pivotX, pivotY, pivotZ + shifting);
        if (rotating) {
            buffer.rotate(Direction.Axis.X, AngleHelper.rad((double) barAngle));
        }

        buffer.translate(-pivotX, -pivotY, -pivotZ);
        if (flip && !upper) {
            buffer.translate(0.5625F, 0.0F, 0.0F);
        }

        return buffer;
    }

    protected SuperByteBuffer rotateToFacing(SuperByteBuffer buffer, Direction facing) {
        buffer.rotateCentered(AngleHelper.rad((double) AngleHelper.horizontalAngle(facing)), Direction.Axis.Y);
        return buffer;
    }
}

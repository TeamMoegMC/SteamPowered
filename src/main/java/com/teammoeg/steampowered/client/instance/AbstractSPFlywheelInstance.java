package com.teammoeg.steampowered.client.instance;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import com.teammoeg.steampowered.block.SPBlockPartials;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlock;
import com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlockEntity;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.FlatLit;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public abstract class AbstractSPFlywheelInstance extends KineticBlockEntityVisual<OldFlywheelBlockEntity> implements SimpleDynamicVisual {
    protected final Direction facing;
    protected final Direction connection;

    protected boolean connectedLeft;
    protected float connectorAngleMult;

    protected final RotatingInstance shaft;

    protected final TransformedInstance wheel;

    protected List<TransformedInstance> connectors;
    protected TransformedInstance upperRotating;
    protected TransformedInstance lowerRotating;
    protected TransformedInstance upperSliding;
    protected TransformedInstance lowerSliding;

    protected float lastAngle = Float.NaN;

    public AbstractSPFlywheelInstance(VisualizationContext modelManager, com.teammoeg.steampowered.oldcreatestuff.OldFlywheelBlockEntity tile, float d) {
        super(modelManager, tile, d);

        facing = blockState.getValue(HORIZONTAL_FACING);

        shaft = shaftModel().createInstance();

        shaft.setup(tile).setPosition(getVisualPosition()).rotateToFace(facing.getClockWise()).setChanged();

        wheel = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getWheelModel(), facing.getClockWise())).createInstance();//, referenceState, referenceState.getValue(HORIZONTAL_FACING)).createInstance();

        connection = OldFlywheelBlock.getConnection(blockState);
        if (connection != null) {
            connectedLeft = blockState.getValue(OldFlywheelBlock.CONNECTION) == OldFlywheelBlock.ConnectionState.LEFT;

            boolean flipAngle = connection.getAxis() == Direction.Axis.X ^ connection.getAxisDirection() == Direction.AxisDirection.NEGATIVE;

            connectorAngleMult = flipAngle ? -1 : 1;

            upperRotating = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getUpperRotatingModel())).createInstance();
            lowerRotating = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getLowerRotatingModel())).createInstance();
            upperSliding = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getUpperSlidingModel())).createInstance();
            lowerSliding = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getLowerSlidingModel())).createInstance();

            connectors = Lists.newArrayList(upperRotating, lowerRotating, upperSliding, lowerSliding);
        } else {
            connectors = Collections.emptyList();
        }
        animate(tile.angle);
    }


    @Override
    public void beginFrame(Context context) {

        float partialTicks = context.partialTick();

        float speed = blockEntity.visualSpeed.get(partialTicks) * 3 / 10f;
        float angle = blockEntity.angle + speed * partialTicks;

        if (Math.abs(angle - lastAngle) < 0.001) return;

        animate(angle);

        lastAngle = angle;
    }

    private void animate(float angle) {
        PoseStack ms = new PoseStack();
        PoseTransformStack msr = TransformStack.of(ms);

        msr.translate(getVisualPosition());

        if (connection != null) {
            float rotation = angle * connectorAngleMult;

            ms.pushPose();
            rotateToFacing(msr, connection);

            ms.pushPose();
            transformConnector(msr, true, true, rotation, connectedLeft);
            upperRotating.setTransform(ms);
            upperRotating.setChanged();
            ms.popPose();

            ms.pushPose();
            transformConnector(msr, false, true, rotation, connectedLeft);
            lowerRotating.setTransform(ms);
            lowerRotating.setChanged();
            ms.popPose();

            ms.pushPose();
            transformConnector(msr, true, false, rotation, connectedLeft);
            upperSliding.setTransform(ms);
            upperSliding.setChanged();
            ms.popPose();

            ms.pushPose();
            transformConnector(msr, false, false, rotation, connectedLeft);
            lowerSliding.setTransform(ms);
            lowerSliding.setChanged();
            ms.popPose();

            ms.popPose();
        }

        msr.center()
                .rotate(AngleHelper.rad(angle), Direction.get(Direction.AxisDirection.POSITIVE, rotationAxis()).getAxis())
                .uncenter();

        wheel.setTransform(ms);
        wheel.setChanged();
    }

    @Override
    public void update(float ticks) {
        shaft.setup(blockEntity).setChanged();
    }

    @Override
    public void updateLight(float v) {
        relight(pos, shaft, wheel);

        if (connection != null) {
            relight(this.pos.relative(connection), connectors.stream().map((f) -> (FlatLit)f).iterator());
        }
    }

    @Override
    public void _delete() {
        shaft.delete();
        wheel.delete();

        connectors.forEach(TransformedInstance::delete);
        connectors.clear();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }

    protected Instancer<RotatingInstance> shaftModel() {
        Direction opposite = facing.getOpposite();
        return instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF, opposite));
    }

    protected void transformConnector(PoseTransformStack ms, boolean upper, boolean rotating, float angle, boolean flip) {
        float shift = upper ? 1 / 4f : -1 / 8f;
        float offset = 1 / 4f;
        float radians = (float) (angle / 180 * Math.PI);
        float shifting = Mth.sin(radians) * shift + offset;

        float maxAngle = upper ? -5 : -15;
        float minAngle = upper ? -45 : 5;
        float barAngle = 0;

        if (rotating)
            barAngle = Mth.lerp((Mth.sin((float) (radians + Math.PI / 2)) + 1) / 2, minAngle, maxAngle);

        float pivotX = (upper ? 8f : 3f) / 16;
        float pivotY = (upper ? 8f : 2f) / 16;
        float pivotZ = (upper ? 23f : 21.5f) / 16f;

        ms.translate(pivotX, pivotY, pivotZ + shifting);
        if (rotating)
            ms.rotate(AngleHelper.rad(barAngle), Direction.Axis.X);
        ms.translate(-pivotX, -pivotY, -pivotZ);

        if (flip && !upper)
            ms.translate(9 / 16f, 0, 0);
    }

    protected void rotateToFacing(PoseTransformStack buffer, Direction facing) {
        buffer.center()
                .rotate(AngleHelper.rad(AngleHelper.horizontalAngle(facing)), Direction.UP)
                .uncenter();
    }

    protected abstract PartialModel getWheelModel();
    protected abstract PartialModel getUpperSlidingModel();
    protected abstract PartialModel getLowerSlidingModel();
    protected abstract PartialModel getUpperRotatingModel();
    protected abstract PartialModel getLowerRotatingModel();
}

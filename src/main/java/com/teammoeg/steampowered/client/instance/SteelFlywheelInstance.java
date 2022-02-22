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

package com.teammoeg.steampowered.client.instance;

import com.google.common.collect.Lists;
import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.MatrixTransformStack;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelTileEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.teammoeg.steampowered.block.SPBlockPartials;
import com.teammoeg.steampowered.mixin.FlywheelTileEntityAccess;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.List;

import static com.simibubi.create.content.contraptions.base.HorizontalKineticBlock.HORIZONTAL_FACING;


public class SteelFlywheelInstance extends KineticTileInstance<FlywheelTileEntity> implements DynamicInstance {
    protected final Direction facing;
    protected final Direction connection;

    protected boolean connectedLeft;
    protected float connectorAngleMult;

    protected final RotatingData shaft;

    protected final ModelData wheel;

    protected List<ModelData> connectors;
    protected ModelData upperRotating;
    protected ModelData lowerRotating;
    protected ModelData upperSliding;
    protected ModelData lowerSliding;

    protected float lastAngle = Float.NaN;

    public SteelFlywheelInstance(MaterialManager modelManager, FlywheelTileEntity tile) {
        super(modelManager, tile);

        facing = blockState.getValue(HORIZONTAL_FACING);

        shaft = setup(shaftModel().createInstance());

        BlockState referenceState = blockState.rotate(Rotation.CLOCKWISE_90);
        wheel = getTransformMaterial().getModel(SPBlockPartials.STEEL_FLYWHEEL, referenceState, referenceState.getValue(HORIZONTAL_FACING)).createInstance();

        connection = FlywheelBlock.getConnection(blockState);
        if (connection != null) {
            connectedLeft = blockState.getValue(FlywheelBlock.CONNECTION) == FlywheelBlock.ConnectionState.LEFT;

            boolean flipAngle = connection.getAxis() == Direction.Axis.X ^ connection.getAxisDirection() == Direction.AxisDirection.NEGATIVE;

            connectorAngleMult = flipAngle ? -1 : 1;

            Material<ModelData> mat = getTransformMaterial();

            upperRotating = mat.getModel(SPBlockPartials.STEEL_FLYWHEEL_UPPER_ROTATING, blockState).createInstance();
            lowerRotating = mat.getModel(SPBlockPartials.STEEL_FLYWHEEL_LOWER_ROTATING, blockState).createInstance();
            upperSliding = mat.getModel(SPBlockPartials.STEEL_FLYWHEEL_UPPER_SLIDING, blockState).createInstance();
            lowerSliding = mat.getModel(SPBlockPartials.STEEL_FLYWHEEL_LOWER_SLIDING, blockState).createInstance();

            connectors = Lists.newArrayList(upperRotating, lowerRotating, upperSliding, lowerSliding);
        } else {
            connectors = Collections.emptyList();
        }

        // Mixin
        FlywheelTileEntityAccess access = (FlywheelTileEntityAccess) tile;
        animate(access.getAngle());
    }

    @Override
    public void beginFrame() {

        float partialTicks = AnimationTickHolder.getPartialTicks();

        // Mixin
        FlywheelTileEntityAccess access = (FlywheelTileEntityAccess) blockEntity;
        float speed = access.getVisualSpeed().get(partialTicks) * 3 / 10f;
        float angle = access.getAngle() + speed * partialTicks;

        if (Math.abs(angle - lastAngle) < 0.001) return;

        animate(angle);

        lastAngle = angle;
    }

    private void animate(float angle) {
        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);

        msr.translate(getInstancePosition());

        if (connection != null) {
            float rotation = angle * connectorAngleMult;

            ms.pushPose();
            rotateToFacing(msr, connection);

            ms.pushPose();
            transformConnector(msr, true, true, rotation, connectedLeft);
            upperRotating.setTransform(ms);
            ms.popPose();

            ms.pushPose();
            transformConnector(msr, false, true, rotation, connectedLeft);
            lowerRotating.setTransform(ms);
            ms.popPose();

            ms.pushPose();
            transformConnector(msr, true, false, rotation, connectedLeft);
            upperSliding.setTransform(ms);
            ms.popPose();

            ms.pushPose();
            transformConnector(msr, false, false, rotation, connectedLeft);
            lowerSliding.setTransform(ms);
            ms.popPose();

            ms.popPose();
        }

        msr.centre()
                .rotate(Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis()), AngleHelper.rad(angle))
                .unCentre();

        wheel.setTransform(ms);
    }

    @Override
    public void update() {
        updateRotation(shaft);
    }

    @Override
    public void updateLight() {
        relight(pos, shaft, wheel);

        if (connection != null) {
            relight(this.pos.relative(connection), connectors.stream());
        }
    }

    @Override
    public void remove() {
        shaft.delete();
        wheel.delete();

        connectors.forEach(InstanceData::delete);
        connectors.clear();
    }

    protected Instancer<RotatingData> shaftModel() {
        Direction opposite = facing.getOpposite();
        return getRotatingMaterial().getModel(AllBlockPartials.SHAFT_HALF, blockState, opposite);
    }

    protected void transformConnector(TransformStack ms, boolean upper, boolean rotating, float angle, boolean flip) {
        float shift = upper ? 1 / 4f : -1 / 8f;
        float offset = upper ? 1 / 4f : 1 / 4f;
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
            ms.rotate(Direction.EAST, AngleHelper.rad(barAngle));
        ms.translate(-pivotX, -pivotY, -pivotZ);

        if (flip && !upper)
            ms.translate(9 / 16f, 0, 0);
    }

    protected void rotateToFacing(TransformStack buffer, Direction facing) {
        buffer.centre()
                .rotate(Direction.UP, AngleHelper.rad(AngleHelper.horizontalAngle(facing)))
                .unCentre();
    }
}

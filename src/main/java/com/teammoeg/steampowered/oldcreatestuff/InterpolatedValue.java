package com.teammoeg.steampowered.oldcreatestuff;

import net.minecraft.util.Mth;

public class InterpolatedValue {
    public float value = 0.0F;
    public float lastValue = 0.0F;

    public InterpolatedValue() {
    }

    public InterpolatedValue set(float value) {
        this.lastValue = this.value;
        this.value = value;
        return this;
    }

    public InterpolatedValue init(float value) {
        this.lastValue = this.value = value;
        return this;
    }

    public float get(float partialTicks) {
        return Mth.lerp(partialTicks, this.lastValue, this.value);
    }

    public boolean settled() {
        return (double)Math.abs(this.value - this.lastValue) < 0.001;
    }
}

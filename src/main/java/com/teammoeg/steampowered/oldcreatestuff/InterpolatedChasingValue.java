package com.teammoeg.steampowered.oldcreatestuff;

public class InterpolatedChasingValue extends InterpolatedValue {
    float speed = 0.5F;
    float target = 0.0F;
    float eps = 2.4414062E-4F;

    public InterpolatedChasingValue() {
    }

    public void tick() {
        float diff = this.getCurrentDiff();
        if (!(Math.abs(diff) < this.eps)) {
            this.set(this.value + diff * this.speed);
        }
    }

    protected float getCurrentDiff() {
        return this.getTarget() - this.value;
    }

    public InterpolatedChasingValue withSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public InterpolatedChasingValue target(float target) {
        this.target = target;
        return this;
    }

    public InterpolatedChasingValue start(float value) {
        this.lastValue = this.value = value;
        this.target(value);
        return this;
    }

    public float getTarget() {
        return this.target;
    }
}

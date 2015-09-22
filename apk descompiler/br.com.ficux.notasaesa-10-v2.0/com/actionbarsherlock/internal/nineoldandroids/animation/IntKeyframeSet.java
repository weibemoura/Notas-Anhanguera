package com.actionbarsherlock.internal.nineoldandroids.animation;

import java.util.ArrayList;

class IntKeyframeSet extends KeyframeSet {
    private int deltaValue;
    private boolean firstTime;
    private int firstValue;
    private int lastValue;

    public IntKeyframeSet(IntKeyframe... keyframes) {
        super(keyframes);
        this.firstTime = true;
    }

    public Object getValue(float fraction) {
        return Integer.valueOf(getIntValue(fraction));
    }

    public IntKeyframeSet clone() {
        ArrayList<Keyframe> keyframes = this.mKeyframes;
        int numKeyframes = this.mKeyframes.size();
        IntKeyframe[] newKeyframes = new IntKeyframe[numKeyframes];
        for (int i = 0; i < numKeyframes; i++) {
            newKeyframes[i] = (IntKeyframe) ((Keyframe) keyframes.get(i)).clone();
        }
        return new IntKeyframeSet(newKeyframes);
    }

    public int getIntValue(float fraction) {
        if (this.mNumKeyframes == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((IntKeyframe) this.mKeyframes.get(0)).getIntValue();
                this.lastValue = ((IntKeyframe) this.mKeyframes.get(1)).getIntValue();
                this.deltaValue = this.lastValue - this.firstValue;
            }
            if (this.mInterpolator != null) {
                fraction = this.mInterpolator.getInterpolation(fraction);
            }
            if (this.mEvaluator == null) {
                return this.firstValue + ((int) (((float) this.deltaValue) * fraction));
            }
            return ((Number) this.mEvaluator.evaluate(fraction, Integer.valueOf(this.firstValue), Integer.valueOf(this.lastValue))).intValue();
        } else if (fraction <= 0.0f) {
            prevKeyframe = (IntKeyframe) this.mKeyframes.get(0);
            nextKeyframe = (IntKeyframe) this.mKeyframes.get(1);
            prevValue = prevKeyframe.getIntValue();
            nextValue = nextKeyframe.getIntValue();
            prevFraction = prevKeyframe.getFraction();
            nextFraction = nextKeyframe.getFraction();
            interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            intervalFraction = (fraction - prevFraction) / (nextFraction - prevFraction);
            if (this.mEvaluator == null) {
                return ((int) (((float) (nextValue - prevValue)) * intervalFraction)) + prevValue;
            }
            return ((Number) this.mEvaluator.evaluate(intervalFraction, Integer.valueOf(prevValue), Integer.valueOf(nextValue))).intValue();
        } else if (fraction >= 1.0f) {
            prevKeyframe = (IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 2);
            nextKeyframe = (IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 1);
            prevValue = prevKeyframe.getIntValue();
            nextValue = nextKeyframe.getIntValue();
            prevFraction = prevKeyframe.getFraction();
            nextFraction = nextKeyframe.getFraction();
            interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            intervalFraction = (fraction - prevFraction) / (nextFraction - prevFraction);
            if (this.mEvaluator == null) {
                return ((int) (((float) (nextValue - prevValue)) * intervalFraction)) + prevValue;
            }
            return ((Number) this.mEvaluator.evaluate(intervalFraction, Integer.valueOf(prevValue), Integer.valueOf(nextValue))).intValue();
        } else {
            prevKeyframe = (IntKeyframe) this.mKeyframes.get(0);
            for (int i = 1; i < this.mNumKeyframes; i++) {
                nextKeyframe = (IntKeyframe) this.mKeyframes.get(i);
                if (fraction < nextKeyframe.getFraction()) {
                    interpolator = nextKeyframe.getInterpolator();
                    if (interpolator != null) {
                        fraction = interpolator.getInterpolation(fraction);
                    }
                    intervalFraction = (fraction - prevKeyframe.getFraction()) / (nextKeyframe.getFraction() - prevKeyframe.getFraction());
                    prevValue = prevKeyframe.getIntValue();
                    nextValue = nextKeyframe.getIntValue();
                    if (this.mEvaluator == null) {
                        return ((int) (((float) (nextValue - prevValue)) * intervalFraction)) + prevValue;
                    }
                    return ((Number) this.mEvaluator.evaluate(intervalFraction, Integer.valueOf(prevValue), Integer.valueOf(nextValue))).intValue();
                }
                prevKeyframe = nextKeyframe;
            }
            return ((Number) ((Keyframe) this.mKeyframes.get(this.mNumKeyframes - 1)).getValue()).intValue();
        }
    }
}

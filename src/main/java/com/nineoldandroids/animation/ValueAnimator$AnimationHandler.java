package com.nineoldandroids.animation;

import android.os.Handler;
import android.os.Message;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

class ValueAnimator$AnimationHandler extends Handler {
    private ValueAnimator$AnimationHandler() {
    }

    public void handleMessage(Message msg) {
        int i;
        ValueAnimator anim;
        boolean callAgain = true;
        ArrayList<ValueAnimator> animations = (ArrayList) ValueAnimator.access$000().get();
        ArrayList<ValueAnimator> delayedAnims = (ArrayList) ValueAnimator.access$100().get();
        switch (msg.what) {
            case 0:
                ArrayList<ValueAnimator> pendingAnimations = (ArrayList) ValueAnimator.access$200
                        ().get();
                if (animations.size() > 0 || delayedAnims.size() > 0) {
                    callAgain = false;
                }
                while (pendingAnimations.size() > 0) {
                    ArrayList<ValueAnimator> pendingCopy = (ArrayList) pendingAnimations.clone();
                    pendingAnimations.clear();
                    int count = pendingCopy.size();
                    for (i = 0; i < count; i++) {
                        anim = (ValueAnimator) pendingCopy.get(i);
                        if (ValueAnimator.access$300(anim) == 0) {
                            ValueAnimator.access$400(anim);
                        } else {
                            delayedAnims.add(anim);
                        }
                    }
                }
                break;
            case 1:
                break;
            default:
                return;
        }
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        ArrayList<ValueAnimator> readyAnims = (ArrayList) ValueAnimator.access$500().get();
        ArrayList<ValueAnimator> endingAnims = (ArrayList) ValueAnimator.access$600().get();
        int numDelayedAnims = delayedAnims.size();
        for (i = 0; i < numDelayedAnims; i++) {
            anim = (ValueAnimator) delayedAnims.get(i);
            if (ValueAnimator.access$700(anim, currentTime)) {
                readyAnims.add(anim);
            }
        }
        int numReadyAnims = readyAnims.size();
        if (numReadyAnims > 0) {
            for (i = 0; i < numReadyAnims; i++) {
                anim = (ValueAnimator) readyAnims.get(i);
                ValueAnimator.access$400(anim);
                ValueAnimator.access$802(anim, true);
                delayedAnims.remove(anim);
            }
            readyAnims.clear();
        }
        int numAnims = animations.size();
        i = 0;
        while (i < numAnims) {
            anim = (ValueAnimator) animations.get(i);
            if (anim.animationFrame(currentTime)) {
                endingAnims.add(anim);
            }
            if (animations.size() == numAnims) {
                i++;
            } else {
                numAnims--;
                endingAnims.remove(anim);
            }
        }
        if (endingAnims.size() > 0) {
            for (i = 0; i < endingAnims.size(); i++) {
                ValueAnimator.access$900((ValueAnimator) endingAnims.get(i));
            }
            endingAnims.clear();
        }
        if (!callAgain) {
            return;
        }
        if (!animations.isEmpty() || !delayedAnims.isEmpty()) {
            sendEmptyMessageDelayed(1, Math.max(0, ValueAnimator.access$1000() - (AnimationUtils
                    .currentAnimationTimeMillis() - currentTime)));
        }
    }
}

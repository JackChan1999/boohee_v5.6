package com.facebook.rebound;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpringChain implements SpringListener {
    private static final int                  DEFAULT_ATTACHMENT_FRICTION = 10;
    private static final int                  DEFAULT_ATTACHMENT_TENSION  = 70;
    private static final int                  DEFAULT_MAIN_FRICTION       = 6;
    private static final int                  DEFAULT_MAIN_TENSION        = 40;
    private static       int                  id                          = 0;
    private static final SpringConfigRegistry registry                    = SpringConfigRegistry
            .getInstance();
    private final SpringConfig                         mAttachmentSpringConfig;
    private       int                                  mControlSpringIndex;
    private final CopyOnWriteArrayList<SpringListener> mListeners;
    private final SpringConfig                         mMainSpringConfig;
    private final SpringSystem                         mSpringSystem;
    private final CopyOnWriteArrayList<Spring>         mSprings;

    public static SpringChain create() {
        return new SpringChain();
    }

    public static SpringChain create(int mainTension, int mainFriction, int attachmentTension,
                                     int attachmentFriction) {
        return new SpringChain(mainTension, mainFriction, attachmentTension, attachmentFriction);
    }

    private SpringChain() {
        this(40, 6, 70, 10);
    }

    private SpringChain(int mainTension, int mainFriction, int attachmentTension, int
            attachmentFriction) {
        this.mSpringSystem = SpringSystem.create();
        this.mListeners = new CopyOnWriteArrayList();
        this.mSprings = new CopyOnWriteArrayList();
        this.mControlSpringIndex = -1;
        this.mMainSpringConfig = SpringConfig.fromOrigamiTensionAndFriction((double) mainTension,
                (double) mainFriction);
        this.mAttachmentSpringConfig = SpringConfig.fromOrigamiTensionAndFriction((double)
                attachmentTension, (double) attachmentFriction);
        SpringConfigRegistry springConfigRegistry = registry;
        SpringConfig springConfig = this.mMainSpringConfig;
        StringBuilder append = new StringBuilder().append("main spring ");
        int i = id;
        id = i + 1;
        springConfigRegistry.addSpringConfig(springConfig, append.append(i).toString());
        springConfigRegistry = registry;
        springConfig = this.mAttachmentSpringConfig;
        append = new StringBuilder().append("attachment spring ");
        i = id;
        id = i + 1;
        springConfigRegistry.addSpringConfig(springConfig, append.append(i).toString());
    }

    public SpringConfig getMainSpringConfig() {
        return this.mMainSpringConfig;
    }

    public SpringConfig getAttachmentSpringConfig() {
        return this.mAttachmentSpringConfig;
    }

    public SpringChain addSpring(SpringListener listener) {
        this.mSprings.add(this.mSpringSystem.createSpring().addListener(this).setSpringConfig
                (this.mAttachmentSpringConfig));
        this.mListeners.add(listener);
        return this;
    }

    public SpringChain setControlSpringIndex(int i) {
        this.mControlSpringIndex = i;
        if (((Spring) this.mSprings.get(this.mControlSpringIndex)) == null) {
            return null;
        }
        for (Spring spring : this.mSpringSystem.getAllSprings()) {
            spring.setSpringConfig(this.mAttachmentSpringConfig);
        }
        getControlSpring().setSpringConfig(this.mMainSpringConfig);
        return this;
    }

    public Spring getControlSpring() {
        return (Spring) this.mSprings.get(this.mControlSpringIndex);
    }

    public List<Spring> getAllSprings() {
        return this.mSprings;
    }

    public void onSpringUpdate(Spring spring) {
        int idx = this.mSprings.indexOf(spring);
        SpringListener listener = (SpringListener) this.mListeners.get(idx);
        int above = -1;
        int below = -1;
        if (idx == this.mControlSpringIndex) {
            below = idx - 1;
            above = idx + 1;
        } else if (idx < this.mControlSpringIndex) {
            below = idx - 1;
        } else if (idx > this.mControlSpringIndex) {
            above = idx + 1;
        }
        if (above > -1 && above < this.mSprings.size()) {
            ((Spring) this.mSprings.get(above)).setEndValue(spring.getCurrentValue());
        }
        if (below > -1 && below < this.mSprings.size()) {
            ((Spring) this.mSprings.get(below)).setEndValue(spring.getCurrentValue());
        }
        listener.onSpringUpdate(spring);
    }

    public void onSpringAtRest(Spring spring) {
        ((SpringListener) this.mListeners.get(this.mSprings.indexOf(spring))).onSpringAtRest
                (spring);
    }

    public void onSpringActivate(Spring spring) {
        ((SpringListener) this.mListeners.get(this.mSprings.indexOf(spring))).onSpringActivate(spring);
    }

    public void onSpringEndStateChange(Spring spring) {
        ((SpringListener) this.mListeners.get(this.mSprings.indexOf(spring))).onSpringEndStateChange(spring);
    }
}

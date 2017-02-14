package com.facebook.rebound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BaseSpringSystem {
    private final Set<Spring>                               mActiveSprings = new
            CopyOnWriteArraySet();
    private       boolean                                   mIdle          = true;
    private final CopyOnWriteArraySet<SpringSystemListener> mListeners     = new
            CopyOnWriteArraySet();
    private final SpringLooper mSpringLooper;
    private final Map<String, Spring> mSpringRegistry = new HashMap();

    public BaseSpringSystem(SpringLooper springLooper) {
        if (springLooper == null) {
            throw new IllegalArgumentException("springLooper is required");
        }
        this.mSpringLooper = springLooper;
        this.mSpringLooper.setSpringSystem(this);
    }

    public boolean getIsIdle() {
        return this.mIdle;
    }

    public Spring createSpring() {
        Spring spring = new Spring(this);
        registerSpring(spring);
        return spring;
    }

    public Spring getSpringById(String id) {
        if (id != null) {
            return (Spring) this.mSpringRegistry.get(id);
        }
        throw new IllegalArgumentException("id is required");
    }

    public List<Spring> getAllSprings() {
        List<Spring> list;
        Collection<Spring> collection = this.mSpringRegistry.values();
        if (collection instanceof List) {
            list = (List) collection;
        } else {
            list = new ArrayList(collection);
        }
        return Collections.unmodifiableList(list);
    }

    void registerSpring(Spring spring) {
        if (spring == null) {
            throw new IllegalArgumentException("spring is required");
        } else if (this.mSpringRegistry.containsKey(spring.getId())) {
            throw new IllegalArgumentException("spring is already registered");
        } else {
            this.mSpringRegistry.put(spring.getId(), spring);
        }
    }

    void deregisterSpring(Spring spring) {
        if (spring == null) {
            throw new IllegalArgumentException("spring is required");
        }
        this.mActiveSprings.remove(spring);
        this.mSpringRegistry.remove(spring.getId());
    }

    void advance(double deltaTime) {
        for (Spring spring : this.mActiveSprings) {
            if (spring.systemShouldAdvance()) {
                spring.advance(deltaTime / 1000.0d);
            } else {
                this.mActiveSprings.remove(spring);
            }
        }
    }

    public void loop(double ellapsedMillis) {
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            ((SpringSystemListener) i$.next()).onBeforeIntegrate(this);
        }
        advance(ellapsedMillis);
        if (this.mActiveSprings.isEmpty()) {
            this.mIdle = true;
        }
        i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            ((SpringSystemListener) i$.next()).onAfterIntegrate(this);
        }
        if (this.mIdle) {
            this.mSpringLooper.stop();
        }
    }

    void activateSpring(String springId) {
        Spring spring = (Spring) this.mSpringRegistry.get(springId);
        if (spring == null) {
            throw new IllegalArgumentException("springId " + springId + " does not reference a " +
                    "registered spring");
        }
        this.mActiveSprings.add(spring);
        if (getIsIdle()) {
            this.mIdle = false;
            this.mSpringLooper.start();
        }
    }

    public void addListener(SpringSystemListener newListener) {
        if (newListener == null) {
            throw new IllegalArgumentException("newListener is required");
        }
        this.mListeners.add(newListener);
    }

    public void removeListener(SpringSystemListener listenerToRemove) {
        if (listenerToRemove == null) {
            throw new IllegalArgumentException("listenerToRemove is required");
        }
        this.mListeners.remove(listenerToRemove);
    }

    public void removeAllListeners() {
        this.mListeners.clear();
    }
}

package com.squareup.leakcanary;

public interface GcTrigger {
    public static final GcTrigger DEFAULT = new GcTrigger() {
        public void runGc() {
            Runtime.getRuntime().gc();
            enqueueReferences();
            System.runFinalization();
        }

        private void enqueueReferences() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new AssertionError();
            }
        }
    };

    void runGc();
}

package com.hanyou.leyusdk;

import java.util.ArrayList;

public class ConnectionManager {
    public static final int MAX_CONNECTIONS = 5;
    private static ConnectionManager instance;
    private ArrayList<Runnable> active = new ArrayList();
    private ArrayList<Runnable> queue  = new ArrayList();

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public void push(Runnable runnable) {
        this.queue.add(runnable);
        if (this.active.size() < 5) {
            startNext();
        }
    }

    private void startNext() {
        if (!this.queue.isEmpty()) {
            Runnable next = (Runnable) this.queue.get(0);
            this.queue.remove(0);
            this.active.add(next);
            new Thread(next).start();
        }
    }

    public void didComplete(Runnable runnable) {
        this.active.remove(runnable);
        startNext();
    }
}

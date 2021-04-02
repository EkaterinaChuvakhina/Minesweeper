package ru.chuvahina.stopwatch;


import ru.chuvahina.model.ModelListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Stopwatch {
    private final long initialDelay;
    private final long delayBetweenRuns;
    private final List<ModelListener> listeners;
    private ScheduledExecutorService scheduler;
    private long init;
    private long elapsed;

    public Stopwatch() {
        listeners = new ArrayList<>();
        this.initialDelay = 1L;
        this.delayBetweenRuns = 1L;
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void activateTimer() {
        init = System.currentTimeMillis();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> notify(elapsedInSeconds()),
                initialDelay, delayBetweenRuns, TimeUnit.SECONDS);
    }

    private void notify(long time) {
        for (ModelListener listener : listeners) {
            listener.timeUpdate(time);
        }
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
            elapsed = elapsedInSeconds();
        }
    }

    public long elapsed() {
        return elapsed;
    }

    private long elapsedInSeconds() {
        return (System.currentTimeMillis() - init) / 1000;
    }
}
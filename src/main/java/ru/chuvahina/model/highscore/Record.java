package ru.chuvahina.model.highscore;

import java.io.Serializable;

public class Record implements Serializable {
    private final String name;
    private final long time;

    public Record(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}

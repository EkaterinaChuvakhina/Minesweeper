package ru.chuvahina.model.highscore;

import ru.chuvahina.common.setting.GameMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordTable implements Serializable {
    private static final String DEFAULT_USER_NAME = "User";
    private static final int DEFAULT_TIME = 999;
    private final Map<GameMode, Record> recordMap;

    public RecordTable(Map<GameMode, Record> recordMap) {
        this.recordMap = recordMap;
        initTableRecord();
    }

    private void initTableRecord() {
        recordMap.putIfAbsent(GameMode.BEGINNER, new Record(DEFAULT_USER_NAME, DEFAULT_TIME));
        recordMap.putIfAbsent(GameMode.AMATEUR, new Record(DEFAULT_USER_NAME, DEFAULT_TIME));
        recordMap.putIfAbsent(GameMode.ADVANCED, new Record(DEFAULT_USER_NAME, DEFAULT_TIME));
    }

    public boolean isRecord(GameMode mode, long time) {
        Record lastRecord = recordMap.get(mode);
        return lastRecord.getTime() > time;
    }

    public void resetRecords() {
        recordMap.replaceAll((m, v) -> new Record(DEFAULT_USER_NAME, DEFAULT_TIME));
    }

    public void addNewRecord(GameMode mode, Record record) {
        recordMap.put(mode, record);
    }

    public List<List<String>> printRecordTable() {
        List<List<String>> result = new ArrayList<>();

        for (GameMode gameMode : recordMap.keySet()) {
            List<String> row = new ArrayList<>();

            row.add(String.valueOf(gameMode));

            Record currentRecord = recordMap.get(gameMode);

            row.add(currentRecord.getName());
            row.add(String.valueOf(currentRecord.getTime()));

            result.add(row);
        }
        return result;
    }

    public Map<GameMode, Record> getRecordMap() {
        return recordMap;
    }
}

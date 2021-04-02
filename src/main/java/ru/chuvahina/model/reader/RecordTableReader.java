package ru.chuvahina.model.reader;

import ru.chuvahina.common.setting.GameMode;
import ru.chuvahina.model.highscore.Record;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordTableReader {
    private static final Logger LOGGER = Logger.getLogger(RecordTableReader.class.getName());
    private static final String RECORD_PATH = "record.txt";

    @SuppressWarnings("unchecked")
    public Map<GameMode, Record> readRecordTable() {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(RECORD_PATH)) {
            if (resource != null && resource.available() > 0) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(resource)) {
                    return (Map<GameMode, Record>) objectInputStream.readObject();
                }
            }
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error reading the record table from the file", e);
        }
        return new HashMap<>();
    }
}
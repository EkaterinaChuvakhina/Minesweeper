package ru.chuvahina.model.reader;

import ru.chuvahina.common.setting.GameMode;
import ru.chuvahina.model.highscore.Record;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordTableWriter {
    private static final Logger LOGGER = Logger.getLogger(RecordTableWriter.class.getName());
    private static final String RECORD_PATH = "record.txt";

    public void writeTableRecord(Map<GameMode, Record> tableRecord) {
        URL url = getClass().getClassLoader().getResource(RECORD_PATH);
        File file;
        if (url != null) {
            file = new File(url.getPath());
        } else {
            file = new File(RECORD_PATH);
        }
        try (FileOutputStream writer = new FileOutputStream(file);
             ObjectOutputStream outputStream = new ObjectOutputStream(writer)) {
            outputStream.writeObject(tableRecord);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing the high score table to the file", e);
        }
    }
}

package ru.chuvahina.model.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileReader implements AutoCloseable {
    private static final String UTF8_CHARSET = "UTF-8";
    private final BufferedReader reader;

    public FileReader(String filePath) {
        this.reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().
                        getClassLoader().getResourceAsStream(filePath), UTF8_CHARSET)));
    }

    public String readAllLines() {
        return reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public void close() throws IOException {
        reader.close();
    }
}

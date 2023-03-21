package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FileSerializer implements Serializer {

    private static final Gson gson = new Gson();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл

        try (var outputStream = new FileOutputStream(fileName)) {
            var json = gson.toJson(data);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

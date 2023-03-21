package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private static final Gson gson = new Gson();
    private final String fileName;
    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат

        try (var reader = new InputStreamReader(new FileInputStream(fileName))){
            return gson.fromJson(reader, new TypeToken<List<Measurement>>(){}.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

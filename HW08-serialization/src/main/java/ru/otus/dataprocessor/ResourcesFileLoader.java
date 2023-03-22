package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    private static final Gson gson = new Gson();
    private final String fileName;
    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат

        try (var is = getClass().getClassLoader().getResourceAsStream(fileName);
             var isReader = new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8);
             var buffReader = new BufferedReader(isReader)
        ){
            return gson.fromJson(buffReader, new TypeToken<List<Measurement>>(){}.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

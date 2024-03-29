package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData {

    private final Class<T> type;
    private final String name;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;
    private final Constructor<T> allArgsConstructor;


    public EntityClassMetaDataImpl(Class<T> type) {
        this.type = type;
        this.name = type.getSimpleName().toLowerCase();
        this.allArgsConstructor = getAllArgsConstructor();
        this.idField = parseIdField();
        this.allFields = parseAllFields();
        this.fieldsWithoutId = parseFieldsWithoutId();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor getConstructor() {
        return allArgsConstructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }


    private List<Field> parseAllFields() {
        return List.of(type.getDeclaredFields());
    }

    private Field parseIdField() {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new RuntimeException(String.format("%s doesn't have %s annotation", name, Id.class)));
    }

    private Constructor<T> getAllArgsConstructor() {
        var fieldTypes = Arrays.stream(type.getDeclaredFields())
                .map(Field::getType)
                .toList();

        try {
            return type.getDeclaredConstructor(fieldTypes.toArray(Class[]::new));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Exception getting constructor(%s) of %s", fieldTypes, name), e);
        }
    }

    public List<Field> parseFieldsWithoutId() {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
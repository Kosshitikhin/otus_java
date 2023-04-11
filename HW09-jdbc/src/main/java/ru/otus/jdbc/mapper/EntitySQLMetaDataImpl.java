package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;
    private final String allFields;
    private final String fieldsWithoutId;
    private final String fieldsWithoutIdPlaceholder;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.allFields = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        this.fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        this.fieldsWithoutIdPlaceholder = entityClassMetaData.getFieldsWithoutId().stream()
                .map(name -> "?")
                .collect(Collectors.joining(","));

    }

    @Override
    public String getSelectAllSql() {
        return String.format("select %s fro %s", allFields, entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select %s from %s where %s = ?", allFields, entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        return String.format("insert into %s(%s) values (%s)", entityClassMetaData.getName(), fieldsWithoutId, fieldsWithoutIdPlaceholder);
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set (%s) where %s = ?", entityClassMetaData.getName(), fieldsWithoutId, entityClassMetaData.getIdField().getName());
    }

}
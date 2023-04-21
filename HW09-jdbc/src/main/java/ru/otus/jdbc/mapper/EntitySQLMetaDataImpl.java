package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        var className = entityClassMetaData.getName();
        var idFieldName = entityClassMetaData.getIdField().getName();

        var allFields = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));

        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));

        var fieldsWithoutIdPlaceholder = entityClassMetaData.getFieldsWithoutId().stream()
                .map(name -> "?")
                .collect(Collectors.joining(","));

        this.selectAllSql = String.format("select * from %s", entityClassMetaData.getName());
        this.selectByIdSql = String.format("select %s from %s where %s = ?", allFields, className, idFieldName);
        this.insertSql = String.format("insert into %s(%s) values (%s)", className, fieldsWithoutId, fieldsWithoutIdPlaceholder);
        this.updateSql = String.format("update %s set (%s) where %s = ?", className, fieldsWithoutId, idFieldName);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

}
package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::createInstance);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), this::createInstances).orElse(List.of());
    }

    @Override
    public long insert(Connection connection, T client) {
        var clientFields = getObjectFieldsValues(client, entityClassMetaData.getFieldsWithoutId());
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), clientFields);
    }

    @Override
    public void update(Connection connection, T client) {
        var clientFields = getObjectFieldsValues(client, entityClassMetaData.getFieldsWithoutId());
        clientFields.add(getObjectFieldValue(client, entityClassMetaData.getIdField()));
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), clientFields);
    }


    private List<Object> getObjectFieldsValues(Object object, List<Field> fields) {
        return fields
                .stream()
                .map(field -> getObjectFieldValue(object, field)
                )
                .collect(Collectors.toList());
    }

    private Object getObjectFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getResultsSetFieldsValues(ResultSet rs, Collection<Field> fields) {
        return fields.stream()
                .map(field -> {
                    try {
                        return rs.getObject(field.getName());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray();
    }

    private T createInstance(ResultSet rs) {
        try {
            if (rs.next()) {
                var rsValues = getResultsSetFieldsValues(rs, entityClassMetaData.getAllFields());
                return (T) entityClassMetaData.getConstructor().newInstance(rsValues);
            }
            return null;
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<T> createInstances(ResultSet rs) {
        final List<T> result = new ArrayList<>();
        try {
            while (rs.next()) {
                var rsValues = getResultsSetFieldsValues(rs, entityClassMetaData.getAllFields());
                result.add((T) entityClassMetaData.getConstructor().newInstance(rsValues));
            }
            return result;
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
    }

}
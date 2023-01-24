package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc (DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData){
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
            return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
                try {
                    if (rs.next()) {
                        return createObject(rs);
                    }return null;
                } catch (Exception e) {
                    throw new DataTemplateException(e);
                }
            });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List <T> resultList = new ArrayList<>();
            try {
                while (rs.next()) {
                    resultList.add(createObject(rs));
                }
                return resultList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private T createObject(ResultSet rs){
        try {
            T obj = entityClassMetaData.getConstructor().newInstance();
            entityClassMetaData.getAllFields().forEach(field -> {
                try {
                    field.setAccessible(true);
                    field.set(obj, rs.getObject(field.getName()));
                } catch (Exception e) {
                    throw new IllegalStateException("Object creation error");
                }
            });
            return obj;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public long insert(Connection connection, T obj){
        try {
            List<Object> params = getListFieldWithoutId();
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),params);
        }catch (Exception e){
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getListFieldWithoutId(){
        List<Object> paramsList = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()){
            paramsList.add(field.getName());
        }
        return paramsList;
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> params = getListFieldWithoutId();
            params.add(entityClassMetaData.getIdField().getName());
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}

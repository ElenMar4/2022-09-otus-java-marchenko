package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String name;
    private final List<Field> fields;
    private final List<Field> fieldsWithoutId;
    private final Field fieldWithId;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.name = entityClassMetaData.getName();
        this.fields = entityClassMetaData.getAllFields();
        this.fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        this.fieldWithId = entityClassMetaData.getIdField();
    }

    @Override
    public String getSelectAllSql() {
        //"select * from client"
        return "select * from " + name + ";";
    }

    @Override
    public String getSelectByIdSql() {
        //select id, name from client where id  = ?
        return "select " + String.join(",", changeFieldToString(fields))
                + " from " + name
                + " where " + fieldWithId.getName().toLowerCase(Locale.ROOT) + " = ?";
    }

    @Override
    public String getInsertSql() {
        //"insert into client(name) values (?)"
        return "insert into " + name
                + "(" + String.join(",", changeFieldToString(fieldsWithoutId)) + ")"
                + " values (" + calculateNumberFields(fieldsWithoutId) + ")";
    }

    @Override
    public String getUpdateSql() {
        //"update client set name = ? where id = ?"
        return "update " + name + " set "
                + String.join(",", changeFieldToString(fieldsWithoutId))
                + " = " + calculateNumberFields(fieldsWithoutId) + " where "
                + fieldWithId.getName().toLowerCase(Locale.ROOT) + " = ?";
    }

    private StringJoiner calculateNumberFields(List<Field> fields) {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < fields.size(); i++) {
            joiner.add("?");
        }
        return joiner;
    }

    private List<String> changeFieldToString(List<Field> fields) {
        return fields.stream()
                .map(field -> field.getName().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());
    }
}


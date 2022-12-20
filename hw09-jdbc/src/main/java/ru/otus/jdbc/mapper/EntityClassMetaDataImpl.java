package ru.otus.jdbc.mapper;

import ru.otus.jdbc.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<?> clazz;
    private final String name;
    private final Field[] fields;
    private final Map<Boolean, List<Field>> fieldsFilterByID;

    public EntityClassMetaDataImpl(T obj) {
        this.clazz = obj.getClass();
        this.name = clazz.getSimpleName().toLowerCase(Locale.ROOT);
        this.fields = clazz.getDeclaredFields();
        this.fieldsFilterByID = filteringFieldsById();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public Field getIdField() {
        return fieldsFilterByID.get(true).stream().findFirst().get();
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(fields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsFilterByID.get(false);
    }

    public Map<Boolean, List<Field>> filteringFieldsById() {
        return getAllFields().stream()
                .collect(Collectors.partitioningBy(field -> field.isAnnotationPresent(Id.class)));
    }
}


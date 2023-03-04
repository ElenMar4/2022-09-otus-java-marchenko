package ru.otus.appcontainer;


import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exception.ComponentDuplicationException;
import ru.otus.appcontainer.exception.MissingComponentException;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object config = initConfigClass(configClass);
        List<Method> listMethodsForInvoke = getSortedMethodsWithAnnotation(configClass);
        for (Method method : listMethodsForInvoke) {
            signUpAppComponentInContext(config, method);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object initConfigClass(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
    }

    private List<Method> getSortedMethodsWithAnnotation(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private void signUpAppComponentInContext(Object componentConfig, Method method) {
        try {
            String nameBean = method.getDeclaredAnnotation(AppComponent.class).name();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[method.getParameterCount()];
            for (int i = 0; i < args.length; i++) {
                args[i] = getAppComponent(parameterTypes[i]);
            }
            Object resultSet = method.invoke(componentConfig, args);
            if(appComponentsByName.containsKey(nameBean)){
                throw new ComponentDuplicationException("Trying to extract a duplicate component");
            } else {
                appComponentsByName.put(nameBean, resultSet);
                appComponents.add(resultSet);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws MissingComponentException, ComponentDuplicationException {

        List<Object> resultComponent = new ArrayList<>() ;
        for(Object component : appComponents){
            if (componentClass.isAssignableFrom(component.getClass())){
                resultComponent.add(component);
            }
        }
        if (resultComponent.size() == 1){
            return (C) resultComponent.get(0);
        } else {
            if (resultComponent.isEmpty()){
                throw new MissingComponentException(String.format("Component %s not found", componentClass.getComponentType()));
            } else {
                throw new ComponentDuplicationException(String.format("Many components found: %s", resultComponent.size()));
            }
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) throws MissingComponentException {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new MissingComponentException("Component isn't found");
        }
    }
}

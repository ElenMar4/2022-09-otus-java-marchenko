package ru.otus.appcontainer.api;

import ru.otus.appcontainer.exception.ComponentDuplicationException;
import ru.otus.appcontainer.exception.MissingComponentException;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass) throws MissingComponentException, ComponentDuplicationException;
    <C> C getAppComponent(String componentName) throws MissingComponentException;
}

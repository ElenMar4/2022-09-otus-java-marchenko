package ru.otus.appcontainer.exception;

public class ComponentDuplicationException extends Exception {
    public ComponentDuplicationException(String msg){
        throw new RuntimeException(msg);
    }
}

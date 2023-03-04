package ru.otus.appcontainer.exception;

public class MissingComponentException extends Exception{
    public MissingComponentException(String msg){

        throw new RuntimeException(msg);
    }
}

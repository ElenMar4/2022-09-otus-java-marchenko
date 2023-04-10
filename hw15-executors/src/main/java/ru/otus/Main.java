package ru.otus;

public class Main {
    public static void main(String[] args) {
        SequenceOfNumbers app = new SequenceOfNumbers();
        new Thread(() -> app.action(1)).start();
        new Thread(() -> app.action(2)).start();
    }
}

package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceOfNumbers {
    private static final Logger logger = LoggerFactory.getLogger(SequenceOfNumbers.class);
    private boolean isRevers = false;
    private int lastThread = 2;

    private synchronized void action(int numberThread) {
        int lastNumber = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (numberThread == lastThread) {
//                    logger.info("Thread_" + numberThread + " wait");
                    this.wait();
                }
                lastNumber = getNextNumber(lastNumber);
                logger.info("Thread_" + numberThread + ": " + lastNumber);
                lastThread = numberThread;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int getNextNumber(int last) {
        int min = 1;
        int max = 10;
        int inc = 1;
        if (last == max) {
            isRevers = true;
        } else if (last == min) {
            isRevers = false;
        }
        return isRevers ? last - inc : last + inc;
    }

    public static void main(String[] args) {
        SequenceOfNumbers app = new SequenceOfNumbers();
        new Thread(() -> app.action(1)).start();
        new Thread(() -> app.action(2)).start();
    }
}

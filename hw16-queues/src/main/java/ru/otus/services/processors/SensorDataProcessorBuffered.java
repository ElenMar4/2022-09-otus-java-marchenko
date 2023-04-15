package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);
    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> bufferData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.bufferData = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public synchronized void process(SensorData data) {
        try {
            bufferData.put(data);
            if (bufferData.size() >= bufferSize) {
                flush();
            }
        } catch (InterruptedException e) {
            log.error("Ошибка в процессе записи в буфер", e);
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void flush() {
        try {
            if (bufferData.isEmpty()) {
                return;
            } else {
                List<SensorData> recordingData = new ArrayList<>();
                bufferData.drainTo(recordingData);
                writer.writeBufferedData(recordingData);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера на носитель", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}

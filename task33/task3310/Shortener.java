package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.StorageStrategy;

public class Shortener {
    private Long lastId = 0L; //Это поле будет
    //отвечать за последнее значение идентификатора, которое было использовано при добавлении новой строки в хранилище.
    private StorageStrategy storageStrategy; // будет храниться стратегия хранения данных.

    public Shortener(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    //будет возвращать идентификатор id для заданной
    public synchronized Long getId(String string) {
        if (!storageStrategy.containsValue(string)) {
            lastId++;
            storageStrategy.put(lastId, string);
            return lastId;
        }
        return storageStrategy.getKey(string);
    }

    //будет возвращать строку для заданного
    //идентификатора или null, если передан неверный идентификатор.
    public synchronized String getString(Long id) {
        return storageStrategy.getValue(id);
    }
}

package com.company;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

// Клас, що відповідає за керування сховищем та семафорами
public class Manager {
    // Семафор для контролю доступу до сховища
    public Semaphore access;
    // Семафор, який сигналізує про повне заповнення сховища
    public Semaphore full;
    // Семафор, який сигналізує про порожнечу сховища
    public Semaphore empty;

    // Список, що представляє сховище елементів
    public ArrayList<String> storage = new ArrayList<>();

    // Конструктор класу Manager
    public Manager(int storageSize) {
        // Ініціалізація семафору access з одним дозволом
        access = new Semaphore(1);
        // Ініціалізація семафору full з максимальним числом дозволів, що дорівнює розміру сховища
        full = new Semaphore(storageSize);
        // Ініціалізація семафору empty з нульовими дозволами
        empty = new Semaphore(0);
    }
}

package com.company;

public class Consumer implements Runnable {
    private final int itemNumbers;
    private final Manager manager;
    private final int id;

    // Конструктор класу Consumer
    public Consumer(int itemNumbers, Manager manager, int id) {
        // Ініціалізація полів класу
        this.itemNumbers = itemNumbers;
        this.manager = manager;
        this.id = id;

        // Запуск нового потоку для споживача
        new Thread(this).start();
    }

    // Перевизначений метод інтерфейсу Runnable
    @Override
    public void run() {
        // Цикл споживання елементів
        for (int i = 0; i < itemNumbers; i++) {
            String item;
            try {
                // Захоплення семафора empty (якщо він доступний)
                manager.empty.acquire();
                // Затримка для синхронізації потоків
                Thread.sleep(100);
                // Захоплення семафора access (доступу до сховища)
                manager.access.acquire();

                // Отримання елемента зі сховища
                item = manager.storage.get(0);
                // Видалення елемента зі сховища
                manager.storage.remove(0);
                // Виведення повідомлення про споживаний елемент
                System.out.println("Consumer " + id + " Took " + item);

                // Звільнення семафора access (доступу до сховища)
                manager.access.release();
                // Звільнення семафора full (сигналізування про те, що в сховищі з'явилося вільне місце)
                manager.full.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

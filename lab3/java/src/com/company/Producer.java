package com.company;

// Клас виробника, який реалізує інтерфейс Runnable для створення окремого потоку
public class Producer implements Runnable {
    // Кількість елементів, які вироблятимуться цим виробником
    private final int itemNumbers;
    // Менеджер, який керує сховищем
    private final Manager manager;
    // Унікальний ідентифікатор виробника
    private final int id;

    // Конструктор класу Producer, приймає кількість елементів, менеджера і унікальний ідентифікатор
    public Producer(int itemNumbers, Manager manager, int id) {
        this.itemNumbers = itemNumbers;
        this.manager = manager;
        this.id = id;

        // Створення нового потоку для виробника і початок його виконання
        new Thread(this).start();
    }

    // Перевизначений метод інтерфейсу Runnable, який виконується в окремому потоці
    @Override
    public void run() {
        // Цикл виробництва елементів
        for (int i = 0; i < itemNumbers; i++) {
            try {
                // Очікування, доки в сховищі не звільниться місце (семафор full)
                manager.full.acquire();
                // Очікування, доки не буде отримано доступ до сховища (семафор access)
                manager.access.acquire();

                // Додавання нового елемента до сховища
                manager.storage.add("item " + i);
                // Виведення повідомлення про додавання нового елемента
                System.out.println("Producer " + id + " Added item " + i);

                // Звільнення доступу до сховища
                manager.access.release();
                // Сигналізування про те, що сховище не порожнє (семафор empty)
                manager.empty.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

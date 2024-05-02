package com.company;

public class Main {

    public static void main(String[] args) {
        // Створення об'єкта класу Main
        Main main = new Main();
        // Задання розміру сховища і кількості елементів для обробки
        int storageSize = 10;
        int itemNumbers = 20;
        // Виклик методу starter з передачею розміру сховища та кількості елементів
        main.starter(storageSize, itemNumbers);
    }

    // Метод, який ініціалізує виробників та споживачів
    private void starter(int storageSize, int itemNumbers) {
        // Створення об'єкта класу Manager для керування сховищем
        Manager manager = new Manager(storageSize);
        // Ініціалізація змінних
        int item = 0;
        int i = 0;
        // Поки кількість вироблених елементів менше, ніж загальна кількість елементів для обробки
        while (item < itemNumbers) {
            // Генерація випадкового числа для кількості елементів, які буде виробляти виробник
            int col = (int) Math.round(Math.random() * storageSize);
            // Якщо кількість елементів для обробки залишилася більше, ніж кількість, яку може виробити виробник
            if (itemNumbers - item > col) {
                // Створення виробника та споживача з використанням кількості, яку може виробити виробник
                new Producer(col, manager, i);
                new Consumer(col, manager, i);
                // Оновлення кількості вироблених елементів
                item += col;
            } else {
                // Якщо кількість елементів для обробки менше, ніж кількість, яку може виробити виробник
                // Створення виробника та споживача з залишковою кількістю елементів
                new Producer(itemNumbers - item, manager, i);
                new Consumer(itemNumbers - item, manager, i);
                // Завершення циклу
                break;
            }
            // Оновлення лічильника виробників та споживачів
            i += 1;
            // Якщо кількість ітерацій перевищила 20, завершити цикл
            if (i == 20) {
                break;
            }
        }
    }
}

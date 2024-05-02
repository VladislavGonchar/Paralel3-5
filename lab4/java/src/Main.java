import java.util.concurrent.Semaphore;

public class Main {
    // Створення масиву семафорів для кожної виделки
    static Semaphore[] forks = new Semaphore[5];
    // Створення масиву філософів
    static Philosopher[] philosophers = new Philosopher[5];

    // Клас, який представляє філософа і наслідується від потоку
    static class Philosopher extends Thread {
        int id; // Ідентифікатор філософа
        boolean locked = false; // Прапорець для визначення, чи заблоковані виделки у філософа

        // Конструктор класу філософа
        Philosopher(int id) {
            this.id = id;
        }

        // Метод для блокування виделок
        void lockForks() {
            // Перевірка, чи філософ має парний ідентифікатор
            if (id % 2 == 0) {
                forks[id].acquireUninterruptibly(); // Блокування лівої виделки
                forks[(id + 1) % 5].acquireUninterruptibly(); // Блокування правої виделки
            } else { // Якщо філософ має непарний ідентифікатор
                forks[(id + 1) % 5].acquireUninterruptibly(); // Блокування правої виделки
                forks[id].acquireUninterruptibly(); // Блокування лівої виделки
            }

            // Виведення повідомлень про те, що філософ взяв виделки
            System.out.println("Philosopher " + id + " took left fork");
            System.out.println("Philosopher " + id + " took right fork");
        }

        // Метод для розблокування виделок
        void unlockForks() {
            forks[id].release(); // Розблокування лівої виделки
            forks[(id + 1) % 5].release(); // Розблокування правої виделки
            // Виведення повідомлень про те, що філософ поклав виделки
            System.out.println("Philosopher " + id + " put left fork");
            System.out.println("Philosopher " + id + " put right fork");
        }

        // Перевизначений метод для виконання дій філософа
        @Override
        public void run() {
            // Цикл для повторення дій філософа п'ять разів
            for (int i = 0; i < 5; i++) {
                // Виведення повідомлення про те, що філософ думає
                System.out.println("Philosopher " + id + " is thinking");

                lockForks(); // Блокування виделок

                // Виведення повідомлення про те, що філософ їсть
                System.out.println("Philosopher " + id + " is eating");
                try {
                    Thread.sleep(200); // Затримка для імітації процесу їжі
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                unlockForks(); // Розблокування виделок
            }
        }
    }

    // Основний метод програми
    public static void main(String[] args) {
        // Ініціалізація семафорів для кожної виделки
        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1);
        }

        // Створення філософів та запуск їхніх потоків
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i);
        }

        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }

        // Очікування завершення всіх потоків філософів
        for (Philosopher philosopher : philosophers) {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

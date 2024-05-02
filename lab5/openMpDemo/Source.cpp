#include<iostream>
#include"omp.h" // Включення бібліотеки OpenMP для паралельного програмування
#include<chrono> // Включення бібліотеки для вимірювання часу
using namespace std;

const int arr_size = 20000; // Розмір двовимірного масиву

int arr[arr_size][arr_size]; // Глобальний двовимірний масив

void init_arr(); // Прототип функції для ініціалізації масиву
long long part_sum(int, int, int); // Прототип функції для обчислення суми елементів масиву
long long part_min(int, int, int); // Прототип функції для знаходження рядка з мінімальною сумою елементів
int sizeThread = 4; // Кількість потоків OpenMP
string out1; // Глобальна змінна типу string для виводу результатів
string out2; // Глобальна змінна типу string для виводу результатів

//============================================================================================================================================================

int main() {
	init_arr(); // Ініціалізація масиву
	omp_set_nested(1); // Включення можливості створення вкладених паралельних потоків (1/0)
	double t1 = omp_get_wtime(); // Отримання поточного часу

#pragma omp parallel sections // Директива для створення паралельних секцій
	{
#pragma omp section
		{
			long long x = part_min(0, arr_size, sizeThread); // Виклик функції part_min в одній з паралельних секцій
			printf("minsum (CountThread = %d) = %d \n", sizeThread, x); // Вивід результату обчислення
		}

#pragma omp section
		{
			long long x = part_sum(0, arr_size, sizeThread); // Виклик функції part_sum в одній з паралельних секцій
			printf("sum (CountThread = %d) = %d \n", sizeThread, x); // Вивід результату обчислення
		}
	}

	double t2 = omp_get_wtime(); // Отримання поточного часу після виконання паралельних секцій
	printf("Total time - %F seconds", t2 - t1); // Вивід загального часу виконання

	return 0;
}

//============================================================================================================================================================

void init_arr() {
	// Ініціалізація двовимірного масиву
	for (int i = 0; i < arr_size; i++) {
		for (size_t j = 0; j < arr_size; j++) {
			arr[i][j] = arr_size * arr_size - i * arr_size - j;
		}
	}

	arr[arr_size / 2][arr_size / 2] = 100; // Зміна значення центрального елементу масиву
}

//============================================================================================================================================================
// Функція для обчислення суми всіх елементів в двовимірному масиві
long long part_sum(int start_index, int finish_index, int num_threads) {
	long long sum = 0; // Змінна для збереження суми
	double t1 = omp_get_wtime(); // Отримання поточного часу перед початком обчислень

#pragma omp parallel for reduction(+:sum) num_threads(num_threads) // Директива для паралельного обчислення суми з використанням певної кількості потоків
	for (int i = start_index; i < finish_index; i++) {
		for (size_t j = 0; j < arr_size; j++) {
			sum += arr[i][j]; // Обчислення суми елементів масиву
		}
	}

	double t2 = omp_get_wtime(); // Отримання поточного часу після завершення обчислень
	printf("sum time - %F seconds \n", t2 - t1); // Вивід часу виконання обчислень

	return sum; // Повернення суми елементів масиву
}

//============================================================================================================================================================

long long part_min(int start_index, int finish_index, int num_threads) {
	long long minsum = INT32_MAX; // Змінна для збереження мінімальної суми
	int min_row = -1; // Змінна для збереження індексу рядка з мінімальною сумою
	double t1 = omp_get_wtime(); // Отримання поточного часу перед початком обчислень

#pragma omp parallel for num_threads(num_threads) // Директива для паралельних обчислень з використанням певної кількості потоків
	for (int i = start_index; i < finish_index; i++) {
		long long sum = 0; // Змінна для обчислення суми елементів рядка
		for (size_t j = 0; j < arr_size; j++) {
			sum += arr[i][j]; // Обчислення суми елементів рядка
		}

		if (minsum > sum) {
#pragma omp critical // Блок коду, до якого може отримати доступ лише один потік одночасно
			if (minsum > sum) {
				min_row = i; // Оновлення індексу рядка з мінімальною сумою
				minsum = sum; // Оновлення мінімальної суми
			}
		}
	}

	double t2 = omp_get_wtime(); // Отримання поточного часу після завершення обчислень
	printf("min sum time - %F seconds \n", t2 - t1); // Вивід часу виконання обчислень
	printf("Row with minimum sum: %d\n", min_row); // Вивід індексу рядка з мінімальною сумою

	return minsum; // Повернення мінімальної суми
}

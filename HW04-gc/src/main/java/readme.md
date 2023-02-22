## Определение нужного размера хипа
### Цель:
на примере простого приложения понять какое влияние оказывают сборщики мусора  
Есть готовое приложение (модуль homework)  
Запустите его с размером хипа 256 Мб и посмотрите в логе время выполнения.

Пример вывода:  
spend msec:18284, sec:18

Увеличьте размер хипа до 2Гб, замерьте время выполнения.   
Результаты запусков записывайте в таблицу.  
Определите оптимальный размер хипа, т.е. размер, превышение которого,  
не приводит к сокращению времени выполнения приложения.  
Оптимизируйте работу приложения.  
Т.е. не меняя логики работы, сделайте так, чтобы приложение работало быстро с минимальным хипом.  
Повторите измерения времени выполнения программы для тех же значений размера хипа.  


**ПК: Processor Intel(R) Core(TM) i5-3470 CPU @ 3.20GHz, 3200 Mhz, 4 Core(s), 4 Logical Processor(s), RAM 16GB**


**Таблица измрений**

Размер heap --- Время выполнения
1. 256 Мб --- spend msec:15560, sec:14
2. 512 Мб --- spend msec:15530, sec:15
3. 768 Мб --- spend msec:14945, sec:14
4. 1024 Мб --- spend msec:15668, sec:15 (оптимальное количество памяти)
5. 1280 Мб --- spend msec:15225, sec:15
6. 1536 Мб --- spend msec:15094, sec:15
7. 1792 Мб --- spend msec:15285, sec:15
8. 2048 Мб --- spend msec:15027, sec:15


**Таблица измерений после замены обертки на примитив (Integer -> int)**

Размер heap --- Время выполнения
1. 256 Мб --- spend msec:4139, sec:4
2. 512 Мб --- spend msec:3055, sec:3 (оптимальное количество памяти)
3. 768 Мб --- spend msec:3238, sec:3
4. 1024 Мб --- spend msec:3148, sec:3 
5. 1280 Мб --- spend msec:3056, sec:3
6. 1536 Мб --- spend msec:3003, sec:3
7. 1792 Мб --- spend msec:3235, sec:3
8. 2048 Мб --- spend msec:3017, sec:3
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Calendar; use Ada.Calendar;
with GNAT.Semaphores; use GNAT.Semaphores;

procedure main is
   task type Philosopher is
      entry Start(Id : Integer); -- Оголошення типу задачі "Philosopher" з входним параметром "Id"
   end Philosopher;

   Forks : array (1..5) of Counting_Semaphore(1, Default_Ceiling); -- Масив семафорів "Forks" для представлення вилок

   Philosophers : array (1..5) of Philosopher; -- Масив філософів

   procedure Lock_Forks(Id : Integer) is -- Процедура захоплення вилок
   begin
      if Id mod 2 = 0 then
         Forks(Id).Seize; -- Захоплення лівої вилки
         Forks(Id + 1).Seize; -- Захоплення правої вилки
      else
         Forks(Id + 1).Seize; -- Захоплення лівої вилки
         Forks(Id).Seize; -- Захоплення правої вилки
      end if;

      Put_Line("Philosopher " & Id'Img & " took left fork"); -- Виведення повідомлення про захоплення лівої вилки
      Put_Line("Philosopher " & Id'Img & " took right fork"); -- Виведення повідомлення про захоплення правої вилки
   end Lock_Forks;

   procedure Unlock_Forks(Id : Integer) is -- Процедура вивільнення вилок
   begin
      Forks(Id).Release; -- Вивільнення лівої вилки
      Forks(Id + 1).Release; -- Вивільнення правої вилки
      Put_Line("Philosopher " & Id'Img & " put left fork"); -- Виведення повідомлення про відкладення лівої вилки
      Put_Line("Philosopher " & Id'Img & " put right fork"); -- Виведення повідомлення про відкладення правої вилки
   end Unlock_Forks;

   task body Philosopher is
      Id : Integer;
      Locked : Boolean := False;
   begin
      accept Start(Id : in Integer) do -- Приймання параметру "Id" через вхідний вхід "Start"
         Philosopher.Id := Id; -- Присвоєння ідентифікатора філософу
      end Start;

      for I in 1..5 loop
         Put_Line("Philosopher " & Id'Image & " is thinking"); -- Виведення повідомлення про думки філософа

         Lock_Forks(Id); -- Захоплення вилок

         Put_Line("Philosopher " & Id'Image & " is eating"); -- Виведення повідомлення про те, що філософ їсть
         delay 0.2; -- Затримка для імітації їжі

         Unlock_Forks(Id); -- Вивільнення вилок
      end loop;
   end Philosopher;

begin
   for I in Philosophers'Range loop
      Philosophers(I).Start(I); -- Запуск кожного філософа
   end loop;

   for I in Philosophers'Range loop
      null; -- Перевірка, чи завершилися всі задачі філософів
   end loop;
end main;

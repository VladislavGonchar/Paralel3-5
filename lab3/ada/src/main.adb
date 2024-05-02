with Ada.Text_IO, GNAT.Semaphores;
use Ada.Text_IO, GNAT.Semaphores;

with Ada.Containers.Indefinite_Doubly_Linked_Lists;
use Ada.Containers;

procedure Producer_Consumer is
   -- Пакування для списку рядків
   package String_Lists is new Indefinite_Doubly_Linked_Lists (String);
   use String_Lists;

   -- Процедура ініціалізації та запуску задач
   procedure Starter (Storage_Size : in Integer; Item_Numbers : in Integer) is
      -- Оголошення списку та семафорів
      Storage : List;
      Access_Storage : Counting_Semaphore (1, Default_Ceiling);
      Full_Storage   : Counting_Semaphore (Storage_Size, Default_Ceiling);
      Empty_Storage  : Counting_Semaphore (0, Default_Ceiling);

      -- Задача виробника
      task type Producer is
         entry Start(Item_Numbers:Integer);
      end;

      -- Задача споживача
      task type Consumer is
         entry Start(Item_Numbers:Integer);
      end;

      -- Тіло задачі виробника
      task body Producer is
           Item_Numbers : Integer;
      begin
           -- Прийом вхідного параметру
           accept Start (Item_Numbers : in Integer) do
              Producer.Item_Numbers := Item_Numbers;
           end Start;

         -- Цикл створення елементів
         for i in 1 .. Item_Numbers loop
            Full_Storage.Seize;  -- Блокування доступу до повного сховища
            Access_Storage.Seize;  -- Блокування доступу до сховища

            Storage.Append ("item " & i'Img);  -- Додавання елементу до сховища
            Put_Line ("Added item " & i'Img);  -- Виведення повідомлення про додавання

            Access_Storage.Release;  -- Звільнення доступу до сховища
            Empty_Storage.Release;  -- Звільнення доступу до порожнього сховища
            delay 0.1;  -- Затримка
         end loop;

      end Producer;

      -- Тіло задачі споживача
      task body Consumer is
         Item_Numbers : Integer;
      begin
           -- Прийом вхідного параметру
           accept Start (Item_Numbers : in Integer) do
              Consumer.Item_Numbers := Item_Numbers;
           end Start;

         -- Цикл обробки елементів
         for i in 1 .. Item_Numbers loop
            Empty_Storage.Seize;  -- Блокування доступу до порожнього сховища
            Access_Storage.Seize;  -- Блокування доступу до сховища

            declare
               item : String := First_Element (Storage);  -- Отримання першого елементу зі сховища
            begin
               Put_Line ("Took " & item);  -- Виведення повідомлення про обробку елементу
            end;

            Storage.Delete_First;  -- Видалення першого елементу зі сховища

            Access_Storage.Release;  -- Звільнення доступу до сховища
            Full_Storage.Release;  -- Звільнення доступу до повного сховища

            delay 0.5;  -- Затримка
         end loop;

      end Consumer;

      -- Масиви задач та кількість елементів для кожної
      C : array (1..6) of Consumer;
      P :array (1..6) of Producer;
      Col:array(1..6) of Integer:=(7,4,6,3,5,3);
   begin
      -- Запуск кожної задачі
      for i in  C'Range loop
         C(i).Start(Col(i));  -- Запуск задачі споживача
         P(i).Start(Col(i));  -- Запуск задачі виробника
      end loop;
   end Starter;

begin
   -- Запуск процедури Starter зі значеннями розміру сховища та кількості елементів
   Starter (5, 15);
end Producer_Consumer;

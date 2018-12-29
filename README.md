# Multicore Programming
This repository contains codes and reports of three projects in my Multicore Programming course.

## Project 1
The purpose of this project is to implement n-thread mutual exclusion using Tournament and Bakery Algorithms and compare the performance of these two locks. I implemented two locks based on these two algorithms. I tested each thread with **100000** operations, thread quantity varies from **2 to 32**. The experiment runs 5 times and the results are based on the average of the 5 times test. For detailed information, please check the report in the "report" folder.

## Project 2
The purpose of this project is to solve the bridge crossing problem. Some requirements of this question are listed as follows:

  - At any time, the bridge is used by vehicle(s) traveling in one direction only.
  - If vehicles are waiting to cross the bridge at both ends, only one vehicle from one end is allowed to cross the bridge before a vehicle from the other end starts crossing the bridge.
  - If no vehicles are waiting at one end, then any number of vehicles from the other end are permitted to cross the bridge.
  - Every vehicle waiting to cross the bridge is able to cross the bridge eventually.

We are required to solve the bridge crossing problem using both **semaphore** and **monitor**. After implementing these two techiniques, I realiszed the difference between **semaphore** and **monitor**. For **semaphore**, if one thread acquired a **semaphore** and caused the counter less than zero, all other threads which try to acquire this **semaphore** will fail. For **monitor**, if one thread called **await** of a **Condition** object, such thread will go asleep and other threads can access the same lock again. Therefore, when using **semaphore**, we must see a paired existence of **acquire** and **release** a semaphore before other thread can acquire the same **semaphore**. However, when using **monitor**, we can call **await** to let a thread go to sleep and don't affect other threads to proceed.

## Project 3
The purpose of this project is to implement a lazy-synchronization version of LinkedList.The List should support **contains**, **insert**, **delete** and **replace** operations. For **contains**, **insert**, **delete** operations, I used the work discussed in the [coursebook: The Art of Multiprocessor Programming](https://www.amazon.com/Art-Multiprocessor-Programming-Revised-Reprint/dp/0123973376/ref=sr_1_1?ie=UTF8&qid=1546057286&sr=8-1&keywords=The+Art+of+Multiprocessor+Programming).
The hardest part is the implementation of **replace** operation. It takes two keys kold and knew as input arguments. It atomically removes kold from the list (if present) and adds knew to the list (if not present). It returns true if the list was modified in any way and false otherwise. How to make the **replace** operation atomic is really hard for me to think in the first place. However, after several back and forth discussion with my Professor, I figured out how to do it. During the **replace** operation, I first insert the new key(if it doesn't exist) and set a pointer pointing the removing key(if it exists). Then, I remove the old key from the list, since the newly inserted key has a pointer pointing to the old key, the status of removing the old key can be atomically shared to the newly inserted key. For detailed implementation analysis, please check the report in the "report" folder.

If you have any questions about the implementation of these three projects, feel free to drop me an email.
  



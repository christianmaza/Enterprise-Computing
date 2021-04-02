/*  
Name: Christian Mazariegos  
Course: CNT 4714Spring 2021
Assignment title: Project2–Synchronized, Cooperating Threads Under Locking
Due Date:February 17, 2021
*/
package multithreadapp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private Lock lock = new ReentrantLock();
    private int accountBalance;
    private Condition canWithdraw = lock.newCondition();
    
    
    
    public void deposit(int depositAmount, String threadName){
        lock.lock();
        try{
           accountBalance+= depositAmount;
           System.out.printf("%s%s deposits $%d\t\t\t\t\t\t\t\t\t%s%d\n", "Thread ", threadName, depositAmount, "(+) Balance is $", accountBalance);
        }
        catch(Exception exception){
           exception.printStackTrace();
        }
        finally{
           canWithdraw.signalAll();
           lock.unlock();
        } 
    }
    
    public void withdraw(int withdrawalAmount, String threadName){
        lock.lock();
        try{
            while(withdrawalAmount > accountBalance){
                System.out.printf("\t\t\t\t\t%s%s withdraws $%d\t\t\t\t%s\n", "Thread ", threadName, withdrawalAmount, "(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!");
                canWithdraw.await();
            }
            accountBalance-= withdrawalAmount;
            System.out.printf("\t\t\t\t\t%s%s withdraws $%d\t\t\t\t%s%d\n", "Thread ", threadName, withdrawalAmount, "(-) Balance is $", accountBalance);
        }
        catch(InterruptedException exception){
            exception.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }
}

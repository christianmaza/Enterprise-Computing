/*  
Name: Christian Mazariegos  
Course: CNT 4714Spring 2021
Assignment title: Project2–Synchronized, Cooperating Threads Under Locking
Due Date:February 17, 2021
*/
package multithreadapp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class MultiThreadApp {
    
    private static Account mainAccount;
    public static void main(String[] args) {
        System.out.printf("%s\t\t\t\t%s\t\t\t\t%s\n", "Deposit Threads", "Withdrawal Threads", "Balance");
        System.out.printf("%s\t\t\t%s\t\t\t\t%s\n", "--------------------","--------------------","--------------------");
        
        mainAccount = new Account();
        ExecutorService threadExecute = Executors.newCachedThreadPool();
        
        for(int i = 1; i <= 5; i++){
            threadExecute.execute(new Depositor(mainAccount, "D" + i));
        }
        for(int i = 1; i <= 9; i++){
            threadExecute.execute(new Withdrawaler(mainAccount, "W" + i));
        }    
    }
}

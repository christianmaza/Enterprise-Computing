/*  
Name: Christian Mazariegos  
Course: CNT 4714Spring 2021
Assignment title: Project2–Synchronized, Cooperating Threads Under Locking
Due Date:February 17, 2021
*/
package multithreadapp;

import java.util.Random;


public class Withdrawaler implements Runnable { 
    private String threadName;
    private Account account; 
    private Random rand;
    
    public Withdrawaler(Account accountParam, String threadNameParam){
        account = accountParam;
        threadName = threadNameParam;
    }
    public void run(){
        
        try{
            while(true){
                rand = new Random();
                account.withdraw((rand.nextInt(50) + 1), threadName);
                Thread.sleep(rand.nextInt(10));
            }
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
}

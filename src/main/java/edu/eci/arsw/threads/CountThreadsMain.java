/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

import edu.eci.arsw.threads.CountThread;
/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    private static CountThread thread1;
    private static CountThread thread2;
    private static CountThread thread3;
    
    public static void main(String a[]){
        thread1 = new CountThread(0,99);
        thread2 = new CountThread(99,199);
        thread3 = new CountThread(200,299);
        thread1.run();
        thread2.run();
        thread3.run();
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */


public class CountThread extends java.lang.Thread {
    private int min;
    private int max;
    public CountThread(int min, int max){
        this.min=min;
        this.max=max;
    }
    


    @Override 
    public void run(){
        for (int i=min; i<=max; i++){
            System.out.println(i);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author 2115237
 */
public class CounterTrustworthy extends java.lang.Thread {
    
    private int min;
    private int max;
    private String ipaddress;
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private LinkedList<Integer> listasEncontradas;
    private int visitedList;
    private AtomicInteger[] ocurrences;

    
    public CounterTrustworthy(int min,int max,String ipaddress,LinkedList<Integer> listasEncontradas,AtomicInteger[] ocurrences){
        this.min=min;
        this.max=max;
        this.ipaddress=ipaddress;
        visitedList=0;
        this.listasEncontradas=listasEncontradas;
        this.ocurrences = ocurrences;
    }

    @Override
    public void run(){
        int localOcurrences;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        for (int i=min;i<max;i++){            
            ocurrences[0].getAndIncrement();
            if (skds.isInBlackListServer(i, ipaddress)){
                listasEncontradas.add(i);
                localOcurrences = ocurrences[1].incrementAndGet();
                if(localOcurrences>=BLACK_LIST_ALARM_COUNT)
                        i=max;
            }            
        }
    }
    
    public int getVisitedList(){
        return visitedList;
    }
    
}

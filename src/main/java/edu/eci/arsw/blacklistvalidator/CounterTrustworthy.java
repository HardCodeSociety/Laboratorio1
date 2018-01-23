/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

/**
 *
 * @author 2115237
 */
public class CounterTrustworthy extends java.lang.Thread {
    
    private int min;
    private int max;
    private int counter;
    private String ipaddress;
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private LinkedList<Integer> listasEncontradas;
    private int visitedList;


    
    public CounterTrustworthy(int min,int max,String ipaddress,LinkedList<Integer> listasEncontradas){
        this.min=min;
        this.max=max;
        this.ipaddress=ipaddress;
        counter=0;
        visitedList=0;
        this.listasEncontradas=listasEncontradas;
    }

    @Override
    public void run(){
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        for (int i=min;i<max && counter<BLACK_LIST_ALARM_COUNT;i++){            
            visitedList+=1;
            if (skds.isInBlackListServer(i, ipaddress)){
                listasEncontradas.add(i);
                counter+=1;
            }            
        }
    }
    
    public int getCounter(){
        return counter;
    }
    public int getVisitedList(){
        return visitedList;
    }
    
}

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

    
    public CounterTrustworthy(int min,int max,String ipaddress){
        this.min=min;
        this.max=max;
        this.ipaddress=ipaddress;
        counter=0;
    }

    @Override
    public void run(){
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int checkedListsCount=0;
        for (int i=min;i<max && counter<BLACK_LIST_ALARM_COUNT;i++){            
            checkedListsCount++;            
            if (skds.isInBlackListServer(i, ipaddress)){    
                counter++;
            }            
        }
    }
    
    public int getCounter(){
        return counter;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2115237
 */
public class CounterTrustworthy extends java.lang.Thread {
    
    private static final Logger LOG = Logger.getLogger(CounterTrustworthy.class.getName());
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int min;
    private int max;
    private int counter;
    private String ipaddress;
    private LinkedList<Integer> blackListOcurrences;
    
    public CounterTrustworthy(int min,int max,String ipaddress,  LinkedList<Integer> blackListOcurrences){
        this.min=min;
        this.max=max;
        this.ipaddress=ipaddress;
        this.blackListOcurrences=blackListOcurrences;
        counter=0;
    }

    CounterTrustworthy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void run(){
        
        int counter=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;
        
        for (int i=min;i<max && counter<BLACK_LIST_ALARM_COUNT;i++){            
            checkedListsCount++;            
            if (skds.isInBlackListServer(i, ipaddress)){    
                counter++;
            }            
        }
        if(counter!=0){
            blackListOcurrences.add(counter);
        }        
    }
    
}

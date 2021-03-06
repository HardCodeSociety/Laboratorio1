/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N) throws InterruptedException{
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int servidores=skds.getRegisteredServersCount();
        ArrayList<CounterTrustworthy> counters = new ArrayList<CounterTrustworthy>();
        LinkedList<Integer> listasEncontradas= new LinkedList<>();
        AtomicInteger ocurrencesCount=new AtomicInteger();
        AtomicInteger checkedListsCount=new AtomicInteger();
        AtomicInteger[] ocurrences = new AtomicInteger[2];
        ocurrences[0]=checkedListsCount;
        ocurrences[1]=ocurrencesCount;
        CounterTrustworthy thread;
        if(N>servidores){
            N=servidores;
        }
        for(int i=0;i<=servidores;i+=N){
            if(i+N<=servidores){
                thread=new CounterTrustworthy(i,N+i,ipaddress,listasEncontradas,ocurrences);
            }else{
                thread=new CounterTrustworthy(i,servidores%N,ipaddress,listasEncontradas,ocurrences);
            }
            counters.add(thread);
        }    
        for(CounterTrustworthy co : counters){
            co.start();           
        }
        for(CounterTrustworthy cc : counters){
            cc.join();
        }
        if (ocurrences[1].get()>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{ocurrences[0], skds.getRegisteredServersCount()});   

        
        
                
        return listasEncontradas;
    
    }
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}

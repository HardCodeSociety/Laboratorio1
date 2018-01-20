/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
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
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        int ocurrencesCount=0;      
        int checkedListsCount=0;
        int threads =servidores/N;
        CounterTrustworthy thread;
        if(N>servidores){
            N=1;
        }
        for(int i=0;i<servidores;i+=N){
            if(i+N<servidores){
                thread=new CounterTrustworthy(i,N+i,ipaddress,blackListOcurrences);
            }else{
                thread=new CounterTrustworthy(i,servidores%N,ipaddress,blackListOcurrences);
            }
            thread.start();                               
            thread.join();
        }
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        ocurrencesCount = sumatoria(blackListOcurrences);
        System.out.println(ocurrencesCount);
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});   

        
        
                
        return blackListOcurrences;
    
    }
    
    public int sumatoria(LinkedList<Integer> blackListOcurrences){
        int sum  = 0;
        for (int i: blackListOcurrences){
            sum+=1;
        }
        return sum;
    }
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}

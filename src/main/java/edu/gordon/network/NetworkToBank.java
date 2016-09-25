/* * ATM Example system - file NetworkToBank.java * * copyright (c) 2001 - Russell C. Bjork * */package edu.gordon.network;import edu.gordon.core.Network;import java.net.InetAddress;import edu.gordon.banking.Balances;import edu.gordon.banking.Message;import edu.gordon.banking.Status;import edu.gordon.core.Log;/** * Manager for the ATM's network connection. */public class NetworkToBank implements Network {    /**     * Constructor     *     * @param log the log in which to record sending of messages and responses     * @param bankAddress the network address of the bank     */    public NetworkToBank(Log log, InetAddress bankAddress) {        this.bankAddress = bankAddress;    }    @Override    public void openConnection() {        // We don't know the real network...    }    @Override    public void closeConnection() {        // We don't know the real network...    }    @Override    public Status sendMessage(Message message, Balances balances) {        // We don't know the real network...        Status result = new Status() {            @Override            public boolean isSuccess() {                return true;            }            @Override            public boolean isInvalidPIN() {                return false;            }            @Override            public String getMessage() {                return "";            }        };        return result;    }    // LogPhysical into which to record messages    private Log log;    // Network address of the bank    private InetAddress bankAddress;}
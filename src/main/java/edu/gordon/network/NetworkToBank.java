/* * ATM Example system - file NetworkToBank.java * * copyright (c) 2001 - Russell C. Bjork * */package edu.gordon.network;import com.google.common.eventbus.EventBus;import edu.gordon.core.Network;import java.net.InetAddress;import edu.gordon.banking.Balances;import edu.gordon.banking.Message;import edu.gordon.core.Log;/** * Manager for the ATM's network connection. */public class NetworkToBank implements Network {    /**     * Constructor     *     * @param log the log in which to record sending of messages and responses     * @param bankAddress the network address of the bank     */    public NetworkToBank(EventBus bus, Log log, InetAddress bankAddress) {        this.bankAddress = bankAddress;    }    @Override    public void openConnection() {        // We don't know the real network...    }    @Override    public void closeConnection() {        // We don't know the real network...    }    @Override    public void sendMessage(Message message, Balances balances) {        // We don't know the real network...    }    // LogPhysical into which to record messages    private Log log;    // Network address of the bank    private InetAddress bankAddress;}
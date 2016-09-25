/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.core;

import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Status;

/**
 *
 * @author Zeldorine
 */
public interface Network {
    
        /** Open connection to bank at system startup
     */
    public void openConnection();
    
        /** Close connection to bank at system shutdown
     */
    public void closeConnection();
    
        /** Send a message to bank
     *
     *  @param message the message to send
     *  @param balances (out) balances in customer's account as reported
     *         by bank
     *  @return status code returned by bank
     */
    public Status sendMessage(Message message, Balances balances);
}

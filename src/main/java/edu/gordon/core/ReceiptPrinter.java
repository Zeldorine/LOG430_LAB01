/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.core;

import edu.gordon.banking.Receipt;

/**
 *
 * @author Zeldorine
 */
public interface ReceiptPrinter {

    /**
     * Print a receipt
     *
     * @param receipt object containing the information to be printed
     */
    public void printReceipt(Receipt receipt);
}

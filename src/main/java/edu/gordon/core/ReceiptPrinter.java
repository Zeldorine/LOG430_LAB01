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

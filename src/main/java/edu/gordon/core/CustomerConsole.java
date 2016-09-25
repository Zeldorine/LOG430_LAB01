package edu.gordon.core;

import edu.gordon.banking.Money;
import edu.gordon.exception.Cancelled;

/**
 *
 * @author Zeldorine
 */
public interface CustomerConsole {
       /** Display a message to the customer
     *
     *  @param message the message to display
     */
    public void display(String message);
    
    /** Read a PIN entered by the customer (echoed as asterisks)
     *
     *  @param prompt the message to display prompting the customer to enter PIN
     *  @return the PIN that was entered
     *  @exception Cancelled if customer presses the CANCEL key before pressing ENTER
     */
    public int readPIN(String prompt) throws Cancelled;
    
    /** Display a menu of options and return choice made by customer
     *
     *  @param prompt message to display before the list of options
     *  @param menu the options
     *  @return the number of the option chosen (0 .. # of options - 1)
     *  Note: the options are numbered 1 .. # of options when displayed for the
     *  customer - but the menu array indices and the final result returned are in
     *  the range 0 .. # of options - 1
     *
     *  @exception Cancelled if customer presses the CANCEL key before choosing option
     */
    public int readMenuChoice(String prompt, String[] menu) throws Cancelled;
    
    /** Read a money amount entered by the customer
     *
     *  @param prompt the message to display prompting the customer to enter amount
     *  @return the amount entered by the customer
     *  @exception Cancelled if customer presses the CANCEL key before pressing ENTER
     */
    public Money readAmount(String prompt) throws Cancelled;
}

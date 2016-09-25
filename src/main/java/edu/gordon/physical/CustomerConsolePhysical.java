/* * ATM Example system - file CustomerConsolePhysical.java    * * copyright (c) 2001 - Russell C. Bjork * */ package edu.gordon.physical;import edu.gordon.banking.Money;import edu.gordon.exception.Cancelled;import edu.gordon.core.CustomerConsole;/** Manager for the ATM's customer console.  In a real ATM, this would  *  manage a physical device; in this edu.gordon.simulation,  it uses classes  *  in package edu.gordon.simulation to simulate the device (actually two separate *  devices - the display and the keyboard.) */ public class CustomerConsolePhysical implements CustomerConsole{    /** Constructor     */    public CustomerConsolePhysical()    {    }        /** Display a message to the customer     *     *  @param message the message to display     */    public void display(String message)    {    }        /** Read a PIN entered by the customer (echoed as asterisks)     *     *  @param prompt the message to display prompting the customer to enter PIN     *  @return the PIN that was entered     *  @exception Cancelled if customer presses the CANCEL key before pressing ENTER     */    public int readPIN(String prompt) throws Cancelled    {        return 1;    }        /** Display a menu of options and return choice made by customer     *     *  @param prompt message to display before the list of options     *  @param menu the options     *  @return the number of the option chosen (0 .. # of options - 1)     *  Note: the options are numbered 1 .. # of options when displayed for the     *  customer - but the menu array indices and the final result returned are in     *  the range 0 .. # of options - 1     *     *  @exception Cancelled if customer presses the CANCEL key before choosing option     */    public synchronized int readMenuChoice(String prompt, String[] menu) throws Cancelled    {        return 1;    }        /** Read a money amount entered by the customer     *     *  @param prompt the message to display prompting the customer to enter amount     *  @return the amount entered by the customer     *  @exception Cancelled if customer presses the CANCEL key before pressing ENTER     */    public synchronized Money readAmount(String prompt) throws Cancelled    {        return new Money(1);    }}
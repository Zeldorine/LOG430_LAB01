package edu.gordon.core;

import edu.gordon.banking.Money;

/**
 *
 * @author Zeldorine
 */
public interface OperatorPanel {
        // In a real ATM, code would be needed to sense a change in the state of the
    // switch and notify the ATM - simulated in this case by a button in the GUI
    
    /** Get the amount of cash in the cash dispenser from the operator at start up
     *
     *  @return dollar value of the bills in the cash dispenser (# of bills x $20)
     */
    
    public Money getInitialCash();
}

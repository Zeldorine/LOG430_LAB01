package edu.gordon.core;

import edu.gordon.banking.Card;

/**
 *
 * @author Zeldorine
 */
public interface CardReader {
    public Card readCard();
    public void ejectCard();
    public void retainCard();
}

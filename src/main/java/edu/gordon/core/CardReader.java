package edu.gordon.core;

/**
 *
 * @author Zeldorine
 */
public interface CardReader {
    public Integer readCard();
    public void ejectCard();
    public void retainCard();
}

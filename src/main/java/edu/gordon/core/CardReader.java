/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

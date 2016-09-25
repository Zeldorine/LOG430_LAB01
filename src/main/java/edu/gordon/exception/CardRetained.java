package edu.gordon.exception;

/**
 * Local class representing card retained exception
 *
 * Exception that is thrown when the customer's card is retained due to too many
 * invalid PIN entries
 *
 */
public class CardRetained extends Exception {

    /**
     * Constructor
     */
    public CardRetained() {
        super("Card retained due to too many invalid PINs");
    }
}

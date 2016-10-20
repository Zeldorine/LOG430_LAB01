package edu.gordon.core;

/**
 *
 * @author Zeldorine
 */
public interface Log {
        /** LogPhysical the sending of a message to the bank
     *
     *  @param message the message to be logged
     */
    public void logSend(String message);
    
    /** LogPhysical a response received from a message
     *
     *  @param status the status object returned by the bank in response
     */
    public void logResponse(String response);
    
    /** LogPhysical the dispensing of cash by the cash dispenser
     *
     *  @param amount the amount of cash being dispensed
     */
    public void logCashDispensed(String amount);
    
    /** LogPhysical accepting an envelope.  This method is only called if an envelope
     *  is actually received from the customer
     */
    public void logEnvelopeAccepted();
}

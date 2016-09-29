package edu.gordon.simulation;

import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Status;
import edu.gordon.core.Log;

/**
 *
 * @author Zeldorine
 */
public class SimLog implements Log {
        public void logSend(Message message)
    {
        Simulation.getInstance().printLogLine("Message:   " + message.toString()); 
    }
    
    /** LogPhysical a response received from a message
     *
     *  @param status the status object returned by the bank in response
     */
    public void logResponse(Status response)
    {
        Simulation.getInstance().printLogLine("Response:  " + response.toString());
    }
    
    /** LogPhysical the dispensing of cash by the cash dispenser
     *
     *  @param amount the amount of cash being dispensed
     */
    public void logCashDispensed(Money amount)
    {
        Simulation.getInstance().printLogLine("Dispensed: " + amount.toString());
    }
    
    /** LogPhysical accepting an envelope.  This method is only called if an envelope
     *  is actually received from the customer
     */
    public void logEnvelopeAccepted()
    {
        Simulation.getInstance().printLogLine("Envelope:  received");
    }
}

package edu.gordon.simulation;

import edu.gordon.core.Log;

/**
 *
 * @author Zeldorine
 */
public class SimLog implements Log {
        public void logSend(String message)
    {
        Simulation.getInstance().printLogLine("Message:   " + message); 
    }
    
    /** LogPhysical a response received from a message
     *
     *  @param status the status object returned by the bank in response
     */
    public void logResponse(String response)
    {
        Simulation.getInstance().printLogLine("Response:  " + response);
    }
    
    /** LogPhysical the dispensing of cash by the cash dispenser
     *
     *  @param amount the amount of cash being dispensed
     */
    public void logCashDispensed(String amount)
    {
        Simulation.getInstance().printLogLine("Dispensed: " + amount);
    }
    
    /** LogPhysical accepting an envelope.  This method is only called if an envelope
     *  is actually received from the customer
     */
    public void logEnvelopeAccepted()
    {
        Simulation.getInstance().printLogLine("Envelope:  received");
    }
}

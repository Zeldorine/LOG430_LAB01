package edu.gordon.core;

import edu.gordon.exception.Cancelled;

/**
 *
 * @author Zeldorine
 */
public interface EnvelopeAcceptor {
        /** Accept an envelope from customer.
     *
     *  @exception CustomerConsole.Cancelled if operation timed out or the
     *             customer cancelled it
     */
    public void acceptEnvelope() throws Cancelled;
}

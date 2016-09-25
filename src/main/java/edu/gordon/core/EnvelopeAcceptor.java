/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public boolean acceptEnvelope() throws Cancelled;
}

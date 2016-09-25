/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.simulation;

import edu.gordon.core.CardReader;
import edu.gordon.core.CashDispenser;
import edu.gordon.core.CoreFactory;
import edu.gordon.core.CustomerConsole;
import edu.gordon.core.Display;
import edu.gordon.core.EnvelopeAcceptor;
import edu.gordon.core.Keyboard;
import edu.gordon.core.Listener;
import edu.gordon.core.Log;
import edu.gordon.core.OperatorPanel;
import edu.gordon.core.ReceiptPrinter;
import edu.gordon.core.Network;
import edu.gordon.network.SimulatedNetworkToBank;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public class CoreFactorySimulated implements CoreFactory {

    public CardReader getCardReader(Listener listener) {
        return new SimCardReader(listener);
    }

    public Log getLog() {
        return new SimLog();
    }

    public Network getNetwork(Log log, InetAddress bankAddress) {
        return new SimulatedNetworkToBank(log, bankAddress);
    }

    public CashDispenser getCashDispenser(Log log) {
        return new SimCashDispenser(log);
    }

    public EnvelopeAcceptor getEnvelopeAcceptor(Log log) {
        return new SimEnvelopeAcceptor(log);
    }

    public CustomerConsole getCustomerConsole() {
        return new SimCustomerConsole();
    }

    public OperatorPanel getOperatorPanel(Listener listener) {
        return new SimOperatorPanel(listener);
    }

    public ReceiptPrinter getReceiptPrinter() {
        return new SimReceiptPrinter();
    }

    public Display getDisplay() {
        return new SimDisplay();
    }

    public Keyboard getKeyboard(Display display, EnvelopeAcceptor envelopeAcceptor) {
        return new SimKeyboard(display, envelopeAcceptor);
    }
}

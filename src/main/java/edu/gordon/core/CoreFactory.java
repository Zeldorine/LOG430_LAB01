package edu.gordon.core;

import com.google.common.eventbus.EventBus;
import edu.gordon.banking.State;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public abstract class CoreFactory {
    public abstract void init();
    public abstract State getState();
    public abstract CardReader getCardReader(EventBus eventBus);
    public abstract Log getLog();
    public abstract Network getNetwork(EventBus bus, InetAddress bankAddress);
    public abstract CashDispenser getCashDispenser();
    public abstract EnvelopeAcceptor getEnvelopeAcceptor();
    public abstract CustomerConsole getCustomerConsole(EventBus eventBus);
    public abstract OperatorPanel getOperatorPanel(EventBus eventBus);
    public abstract ReceiptPrinter getReceiptPrinter();
    public abstract Display getDisplay();
    public abstract Keyboard getKeyboard(EnvelopeAcceptor envelopeAcceptor);
}

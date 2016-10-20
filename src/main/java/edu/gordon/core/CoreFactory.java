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
    public abstract Network getNetwork(EventBus bus, Log log, InetAddress bankAddress);
    public abstract CashDispenser getCashDispenser(Log log);
    public abstract EnvelopeAcceptor getEnvelopeAcceptor(Log log);
    public abstract CustomerConsole getCustomerConsole(EventBus bus);
    public abstract OperatorPanel getOperatorPanel(EventBus eventBus);
    public abstract ReceiptPrinter getReceiptPrinter();
    public abstract Display getDisplay();
    public abstract Keyboard getKeyboard(Display display, EnvelopeAcceptor envelopeAcceptor);
}

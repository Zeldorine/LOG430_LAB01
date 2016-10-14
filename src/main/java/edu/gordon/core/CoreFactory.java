package edu.gordon.core;

import com.google.common.eventbus.EventBus;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public interface CoreFactory {
    public CardReader getCardReader(Listener listener);
    public Log getLog();
    public Network getNetwork(EventBus bus, Log log, InetAddress bankAddress);
    public CashDispenser getCashDispenser(Log log);
    public EnvelopeAcceptor getEnvelopeAcceptor(Log log);
    public CustomerConsole getCustomerConsole();
    public OperatorPanel getOperatorPanel(Listener listener);
    public ReceiptPrinter getReceiptPrinter();
    public Display getDisplay();
    public Keyboard getKeyboard(Display display, EnvelopeAcceptor envelopeAcceptor);
}

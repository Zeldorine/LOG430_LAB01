package edu.gordon.physical;

import com.google.common.eventbus.EventBus;
import edu.gordon.banking.State;
import edu.gordon.core.CardReader;
import edu.gordon.core.CashDispenser;
import edu.gordon.core.CoreFactory;
import edu.gordon.core.CustomerConsole;
import edu.gordon.core.Display;
import edu.gordon.core.EnvelopeAcceptor;
import edu.gordon.core.Keyboard;
import edu.gordon.core.Log;
import edu.gordon.core.Network;
import edu.gordon.core.OperatorPanel;
import edu.gordon.core.ReceiptPrinter;
import edu.gordon.network.NetworkToBank;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public class CoreFactoryPhysical extends CoreFactory {

    public CardReader cardReader;
    public Log log = new LogPhysical();
    public Network network;
    public CashDispenser cashDispenser;
    public EnvelopeAcceptor envelopeAcceptor;
    public CustomerConsole customerConsole;
    public OperatorPanel operatorPanel;
    public ReceiptPrinter receiptPrinter = new ReceiptPrinterPhysical();
    public Display display = new DisplayPhysical();
    public Keyboard keyboard;

    public void init() {
    }

    public State getState() {
        return State.REEL;
    }

    public CardReader getCardReader(EventBus evenBus) {
        if (cardReader == null) {
            cardReader = new CardReaderPhysical();
        }
        return cardReader;
    }

    public Log getLog() {
        return log;
    }

    public Network getNetwork(EventBus bus, InetAddress bankAddress) {
        if (network == null) {
            network = new NetworkToBank(bus, log, bankAddress);
        }
        return network;
    }

    public CashDispenser getCashDispenser() {
        if (cashDispenser == null) {
            cashDispenser = new CashDispenserPhysical((LogPhysical)log);
        }
        return cashDispenser;
    }

    public EnvelopeAcceptor getEnvelopeAcceptor(EventBus eventBus) {
        if (envelopeAcceptor == null) {
            envelopeAcceptor = new EnvelopeAcceptorPhysical((LogPhysical)log, eventBus);
        }
        return envelopeAcceptor;
    }

    public CustomerConsole getCustomerConsole(EventBus bus) {
        if (customerConsole == null) {
            customerConsole = new CustomerConsolePhysical();
        }
        return customerConsole;
    }

    public OperatorPanel getOperatorPanel(EventBus evenBus) {
        if (operatorPanel == null) {
            operatorPanel = new OperatorPanelPhysical();
        }
        return operatorPanel;
    }

    public ReceiptPrinter getReceiptPrinter(EventBus bus) {
        return receiptPrinter;
    }

    public Display getDisplay() {
        return display;
    }

    public Keyboard getKeyboard(EnvelopeAcceptor envelopeAcceptor) {
        if (keyboard == null) {
            keyboard = new KeyboardPhysical();
        }
        return keyboard;
    } 
}

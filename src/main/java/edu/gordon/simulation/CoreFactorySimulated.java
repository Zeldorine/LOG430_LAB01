package edu.gordon.simulation;

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
import edu.gordon.core.OperatorPanel;
import edu.gordon.core.ReceiptPrinter;
import edu.gordon.core.Network;
import edu.gordon.network.SimulatedNetworkToBank;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public class CoreFactorySimulated extends CoreFactory {

    public CardReader cardReader;
    public Log log;
    public Network network;
    public CashDispenser cashDispenser;
    public EnvelopeAcceptor envelopeAcceptor;
    public CustomerConsole customerConsole;
    public OperatorPanel operatorPanel;
    public ReceiptPrinter receiptPrinter;
    public Display display;
    public Keyboard keyboard;
    
    public void init() {
        new Simulation((SimOperatorPanel)operatorPanel, (SimCardReader)cardReader,
                (SimDisplay)display, (SimKeyboard)keyboard, (SimCashDispenser)cashDispenser, (SimEnvelopeAcceptor)envelopeAcceptor,
                (SimReceiptPrinter)receiptPrinter);
    }

    public State getState() {
        return State.SIMULATION;
    }

    public CardReader getCardReader(EventBus eventBus) {
        if (cardReader == null) {
            cardReader = new SimCardReader(eventBus);
        }
        return cardReader;
    }

    public Log getLog() {
        if (log == null) {
            log = new SimLog();
        }
        return log;
    }

    public Network getNetwork(EventBus bus, Log log, InetAddress bankAddress) {
        if (network == null) {
            network = new SimulatedNetworkToBank(bus, log, bankAddress);
        }
        return network;
    }

    public CashDispenser getCashDispenser(Log log) {
        if (cashDispenser == null) {
            cashDispenser = new SimCashDispenser((SimLog)log);
        }
        return cashDispenser;
    }

    public EnvelopeAcceptor getEnvelopeAcceptor(Log log) {
        if (envelopeAcceptor == null) {
            envelopeAcceptor = new SimEnvelopeAcceptor((SimLog)log);
        }
        return envelopeAcceptor;
    }

    public CustomerConsole getCustomerConsole(EventBus bus) {
        if (customerConsole == null) {
            customerConsole = new SimCustomerConsole(bus);
        }
        return customerConsole;
    }

    public OperatorPanel getOperatorPanel(EventBus eventBus) {
        if (operatorPanel == null) {
            operatorPanel = new SimOperatorPanel(eventBus);
        }
        return operatorPanel;
    }

    public ReceiptPrinter getReceiptPrinter() {
        if (receiptPrinter == null) {
            receiptPrinter = new SimReceiptPrinter();
        }
        return receiptPrinter;
    }

    public Display getDisplay() {
        if (display == null) {
            display = new SimDisplay();
        }
        return display;
    }

    public Keyboard getKeyboard(Display display, EnvelopeAcceptor envelopeAcceptor) {
        if (keyboard == null) {
            keyboard = new SimKeyboard((SimDisplay)display, (SimEnvelopeAcceptor)envelopeAcceptor);
        }
        return keyboard;
    }
}

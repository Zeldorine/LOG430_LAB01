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
    public Log log = new SimLog();
    public Network network;
    public CashDispenser cashDispenser;
    public EnvelopeAcceptor envelopeAcceptor;
    public CustomerConsole customerConsole;
    public OperatorPanel operatorPanel;
    public ReceiptPrinter receiptPrinter = new SimReceiptPrinter();
    public Display display = new SimDisplay();
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
        return log;
    }

    public Network getNetwork(EventBus eventBus, InetAddress bankAddress) {
        if (network == null) {
            network = new SimulatedNetworkToBank(eventBus, log, bankAddress);
        }
        return network;
    }

    public CashDispenser getCashDispenser() {
        if (cashDispenser == null) {
            cashDispenser = new SimCashDispenser((SimLog)log);
        }
        return cashDispenser;
    }

    public EnvelopeAcceptor getEnvelopeAcceptor() {
        if (envelopeAcceptor == null) {
            envelopeAcceptor = new SimEnvelopeAcceptor((SimLog)log);
        }
        return envelopeAcceptor;
    }

    public CustomerConsole getCustomerConsole(EventBus eventBus) {
        if (customerConsole == null) {
            customerConsole = new SimCustomerConsole(eventBus);
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
        return receiptPrinter;
    }

    public Display getDisplay() {
        return display;
    }

    public Keyboard getKeyboard(EnvelopeAcceptor envelopeAcceptor) {
        if (keyboard == null) {
            keyboard = new SimKeyboard((SimDisplay)display, (SimEnvelopeAcceptor)envelopeAcceptor);
        }
        return keyboard;
    }
}

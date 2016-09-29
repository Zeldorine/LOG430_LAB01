package edu.gordon.physical;

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
import edu.gordon.network.NetworkToBank;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public class CoreFactoryPhysical implements CoreFactory {

    public CardReader getCardReader(Listener listener) {
        return new CardReaderPhysical();
    }

    public Log getLog() {
        return new LogPhysical();
    }

    public Network getNetwork(Log log, InetAddress bankAddress) {
        return new NetworkToBank(log, bankAddress);
    }

    public CashDispenser getCashDispenser(Log log) {
        return new CashDispenserPhysical(log);
    }

    public EnvelopeAcceptor getEnvelopeAcceptor(Log log){
        return new EnvelopeAcceptorPhysical(log);
    }

    public CustomerConsole getCustomerConsole() {
        return new CustomerConsolePhysical();
    }

    public OperatorPanel getOperatorPanel(Listener listener) {
        return new OperatorPanelPhysical();
    }

    public ReceiptPrinter getReceiptPrinter() {
        return new ReceiptPrinterPhysical();
    }

    public Display getDisplay() {
        return new DisplayPhysical();
    }

    public Keyboard getKeyboard(Display display, EnvelopeAcceptor envelopeAcceptor) {
        return new KeyboardPhysical();
    }
    
}

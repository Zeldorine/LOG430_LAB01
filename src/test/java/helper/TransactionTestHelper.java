package helper;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import edu.gordon.atm.ATM;
import edu.gordon.atm.Session;
import edu.gordon.physical.CustomerConsolePhysical;
import edu.gordon.transaction.Transaction;
import edu.gordon.banking.Card;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import edu.gordon.banking.State;
import edu.gordon.banking.Status;
import edu.gordon.core.EnvelopeAcceptor;
import edu.gordon.exception.Cancelled;
import edu.gordon.simulation.Simulation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.spy;

/**
 *
 * @author Zeldorine
 */
public class TransactionTestHelper {

    @Mock
    protected Card card;
    @Mock
    protected ATM atm;
    @Mock
    protected Session session;
    @Mock
    protected CustomerConsolePhysical console;

    protected Status status;

    protected static final EventBus bus = new EventBus();

    public TransactionTestHelper() {
        bus.register(this);
    }

    protected void setMenuChoice(int choice) throws Cancelled {
        Mockito.when(console.readMenuChoice(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(choice);
    }

    protected void resetConsoleMock() {
        Mockito.reset(console);
    }

    protected void setReadAmount(Money amount) throws Cancelled {
        Mockito.when(console.readAmount(Mockito.anyString())).thenReturn(amount);
    }

    @BeforeClass
    public static void setUpClass() {
        Simulation.setTimeToWait(0, 0, 0);
        Transaction.timeToWait = 0;
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    protected void init(boolean initSimulation) {
        card = Mockito.mock(Card.class);
        Mockito.when(card.getNumber()).thenReturn(123456);

        initATMMock(initSimulation);

        session = Mockito.mock(Session.class);
    }

    protected void initATMMock(boolean initSimulation) {
        atm = spy(new ATM(bus, 0, "test adress", "Bank test", null, State.SIMULATION));
        atm.getCashDispenser().setInitialCash(new Money(200));

        console = Mockito.mock(CustomerConsolePhysical.class);
        Mockito.when(atm.getCustomerConsole()).thenReturn(console);

        if (initSimulation) {
            Simulation sim = spy(new Simulation(atm.getOperatorPanel(), atm.getCardReader(),
                    atm.getDisplay(), atm.getKeyboard(), atm.getCashDispenser(), atm.getEnvelopeAcceptor(),
                    atm.getReceiptPrinter()));

            setSimulationInstance(ATM.class, "envelopeAcceptor", atm, getEnvelopeAcceptor());
            setSimulationInstance(Simulation.class, "theInstance", Simulation.getInstance(), sim);
        } else {
            new Simulation(atm.getOperatorPanel(), atm.getCardReader(),
                    atm.getDisplay(), atm.getKeyboard(), atm.getCashDispenser(), atm.getEnvelopeAcceptor(),
                    atm.getReceiptPrinter());
        }

    }

    private EnvelopeAcceptor getEnvelopeAcceptor() {
        return new EnvelopeAcceptor() {
            public boolean acceptEnvelope() throws Cancelled {
                return true;
            }
        };
    }

    @After
    public void tearDown() {
        card = null;
        atm = null;
        session = null;
        console = null;
    }

    public List<String> getLines(Receipt receipt) {
        List<String> lines = new ArrayList();
        if (receipt == null) {
            return lines;
        }

        Enumeration receiptLines = receipt.getLines();

        while (receiptLines.hasMoreElements()) {
            lines.add((String) receiptLines.nextElement());
        }

        return lines;
    }

    public Status getSuccessStatus() {
        return new Success();
    }

    public Status getFailureStatus(String msg) {
        return new Failure(msg);
    }

    private void setSimulationInstance(Class clazz, String attribut, Object instance, Object newValue) {
        try {
            Field field = clazz.getDeclaredField(attribut);
            field.setAccessible(true);
            field.set(instance, newValue);
            field.setAccessible(false);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TransactionTestHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TransactionTestHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TransactionTestHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TransactionTestHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Representation for status of a transaction that succeeded
     */
    private static class Success extends Status {

        public boolean isSuccess() {
            return true;
        }

        public boolean isInvalidPIN() {
            return false;
        }

        public String getMessage() {
            return null;
        }
    }

    @Subscribe
    public void handleEvent(Status evt) {
        status = evt;
        session.setStatus(evt);
    }

    /**
     * Representation for status of a transaction that failed (for reason other
     * than invalid PIN)
     */
    public static class Failure extends Status {

        /**
         * Constructor
         *
         * @param message description of the failure
         */
        public Failure(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return false;
        }

        public boolean isInvalidPIN() {
            return false;
        }

        public String getMessage() {
            return message;
        }

        private String message;
    }
}

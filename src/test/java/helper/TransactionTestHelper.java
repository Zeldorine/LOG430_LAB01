package helper;

import edu.gordon.atm.ATM;
import edu.gordon.atm.Session;
import edu.gordon.atm.physical.CustomerConsole;
import edu.gordon.banking.Card;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import edu.gordon.banking.Status;
import edu.gordon.simulation.Simulation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
    protected CustomerConsole console;

    public TransactionTestHelper() {
    }

    protected void setMenuChoice(int choice) throws CustomerConsole.Cancelled {
        Mockito.when(console.readMenuChoice(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(choice);
    }

    protected void setReadAmount(Money amount) throws CustomerConsole.Cancelled {
        Mockito.when(console.readAmount(Mockito.anyString())).thenReturn(amount);
    }

    @BeforeClass
    public static void setUpClass() {
        Simulation.setTimeToWait(0, 0, 0);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        card = Mockito.mock(Card.class);
        Mockito.when(card.getNumber()).thenReturn(123456);

        initATMMock();

        session = Mockito.mock(Session.class);
    }

    protected void initATMMock() {
        atm = spy(new ATM(0, "test adress", "Bank test", null));
        console = Mockito.mock(CustomerConsole.class);
        Mockito.when(atm.getCustomerConsole()).thenReturn(console);
        new Simulation(atm);
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

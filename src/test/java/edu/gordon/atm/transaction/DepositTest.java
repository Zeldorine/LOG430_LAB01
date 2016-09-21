package edu.gordon.atm.transaction;

import helper.TransactionTestHelper;
import edu.gordon.atm.physical.CustomerConsole;
import edu.gordon.atm.physical.EnvelopeAcceptor;
import edu.gordon.atm.physical.NetworkToBank;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 * Test unitaire du requis deposit pour la generation de message et la completion
 * de la trx
 *
 * @author Zeldorine
 */
public class DepositTest extends TransactionTestHelper {

    Transaction deposit;

    public DepositTest() {
    }

    @BeforeClass
    public static void setUpClass() {
                TransactionTestHelper.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            super.setUp();
            setMenuChoice(Message.INITIATE_DEPOSIT);
            setReadAmount(new Money(100));
            EnvelopeAcceptor envelopeAcceptor = Mockito.mock(EnvelopeAcceptor.class);
            Mockito.when(atm.getEnvelopeAcceptor()).thenReturn(envelopeAcceptor);
        } catch (CustomerConsole.Cancelled ex) {
            fail("Error occured during set up deposit");
        }
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testCreate() {
        deposit = new Deposit(atm, session, card, 1414);
        assertNotNull(deposit);
    }

    @Test
    public void testGetSpecificsFromCustomer() {
        Message message = null;
        try {
            deposit = new Deposit(atm, session, card, 1414);
            deposit.serialNumber = 1;
            message = deposit.getSpecificsFromCustomer();
        } catch (CustomerConsole.Cancelled ex) {
            fail("error to get specifics message");
        }

        assertNotNull(message);
        assertEquals("$100.00", message.getAmount().toString());
        assertEquals(card, message.getCard());
        assertEquals(-1, message.getFromAccount());
        assertEquals(1, message.getToAccount());
        assertEquals(Message.INITIATE_DEPOSIT, message.getMessageCode());
        assertEquals(1414, message.getPIN());
        assertEquals("INIT_DEP CARD# 123456 TRANS# 1 NO FROM TO  1 $100.00", message.toString());
    }

    @Test
    public void testCompleteTransaction() {
        try {
            setMenuChoice(Message.COMPLETE_DEPOSIT);
            NetworkToBank network = Mockito.mock(NetworkToBank.class);
            Mockito.when(atm.getNetworkToBank()).thenReturn(network);
            Mockito.when(network.sendMessage(Mockito.any(Message.class), Mockito.any(Balances.class))).thenReturn(getSuccessStatus());

            deposit = new Deposit(atm, session, card, 1414);
            deposit.serialNumber = 1;
            deposit.getSpecificsFromCustomer();
            deposit.balances.setBalances(new Money(200), new Money(100));
            Receipt receipt = deposit.completeTransaction();

            assertNotNull(receipt);
            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 123456 TRANS #1", lines.get(3));
            assertEquals("DEPOSIT TO: MMKT", lines.get(4));
            assertEquals("AMOUNT: $100.00", lines.get(5));
            assertEquals("TOTAL BAL: $200.00", lines.get(6));
            assertEquals("AVAILABLE: $100.00", lines.get(7));
        } catch (CustomerConsole.Cancelled ex) {
            fail("An error occured during complete deposit trx");
        }
    }
}

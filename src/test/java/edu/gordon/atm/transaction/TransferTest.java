package edu.gordon.atm.transaction;

import helper.TransactionTestHelper;
import edu.gordon.atm.physical.CustomerConsole;
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

/**
 * Test unitaire du requis transfer pour la generation de message et la
 * completion de la trx
 *
 * @author Zeldorine
 */
public class TransferTest extends TransactionTestHelper {

    Transaction transfer;

    public TransferTest() {
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
            init(false);
            setMenuChoice(Message.TRANSFER);
            setReadAmount(new Money(100));
        } catch (CustomerConsole.Cancelled ex) {
            fail("Error occured during set up transfer");
        }
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testCreate() {
        transfer = new Transfer(atm, session, card, 1414);
        assertNotNull(transfer);
    }

    @Test
    public void testGetSpecificsFromCustomer() {
        Message message = null;
        try {
            transfer = new Transfer(atm, session, card, 1414);
            transfer.serialNumber = 1;
            message = transfer.getSpecificsFromCustomer();
        } catch (CustomerConsole.Cancelled ex) {
            fail("error to get specifics message");
        }

        assertNotNull(message);
        assertEquals("$100.00", message.getAmount().toString());
        assertEquals(card, message.getCard());
        assertEquals(3, message.getFromAccount());
        assertEquals(3, message.getToAccount());
        assertEquals(Message.TRANSFER, message.getMessageCode());
        assertEquals(1414, message.getPIN());
        assertEquals("TRANSFER CARD# 123456 TRANS# 1 FROM  3 TO  3 $100.00", message.toString());
    }

    @Test
    public void testCompleteTransaction() {
        try {
            transfer = new Transfer(atm, session, card, 1414);
            transfer.serialNumber = 1;
            transfer.balances.setBalances(new Money(100), new Money(40));
            setMenuChoice(2);
            transfer.getSpecificsFromCustomer();
            Receipt receipt = transfer.completeTransaction();

            assertNotNull(receipt);
            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 123456 TRANS #1", lines.get(3));
            assertEquals("TRANSFER FROM: MMKT TO: MMKT", lines.get(4));
            assertEquals("AMOUNT: $100.00", lines.get(5));
            assertEquals("TOTAL BAL: $100.00", lines.get(6));
            assertEquals("AVAILABLE: $40.00", lines.get(7));
        } catch (CustomerConsole.Cancelled ex) {
            fail("An error occured during complete inquiry");
        }
    }
}

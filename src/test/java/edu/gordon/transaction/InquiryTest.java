package edu.gordon.transaction;

import edu.gordon.transaction.Transaction;
import edu.gordon.transaction.Inquiry;
import helper.TransactionTestHelper;
import edu.gordon.physical.CustomerConsolePhysical;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import edu.gordon.exception.Cancelled;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test unitaire du requis inquiry pour la generation de message et la
 * completion de la trx
 *
 * @author Zeldorine
 */
public class InquiryTest extends TransactionTestHelper {

    Transaction inquiry;

    public InquiryTest() {
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
            setMenuChoice(Message.INQUIRY);
        } catch (Cancelled ex) {
            fail("Error occured during set up inquiry");
        }
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testCreate() {
        inquiry = new Inquiry(atm.getID(), atm.getBankName(), atm.getPlace(), atm.getNetworkToBank(), card, 1414);
        assertNotNull(inquiry);
    }

    @Test
    public void testGetSpecificsFromCustomer() {
        Message message = null;
        try {
            inquiry = new Inquiry(atm.getID(), atm.getBankName(), atm.getPlace(), atm.getNetworkToBank(), card, 1414);
            inquiry.serialNumber = 1;
            message = inquiry.getSpecificsFromCustomer(4,-1,null);
        } catch (Cancelled ex) {
            fail("error to get specifics message");
        }

        assertNotNull(message);
        assertEquals("$0.00", message.getAmount().toString());
        assertEquals(card, message.getCard());
        assertEquals(4, message.getFromAccount());
        assertEquals(-1, message.getToAccount());
        assertEquals(Message.INQUIRY, message.getMessageCode());
        assertEquals(1414, message.getPIN());
        assertEquals("INQUIRY  CARD# 123456 TRANS# 1 FROM  4 NO TO NO AMOUNT", message.toString());
    }

    @Test
    public void testCompleteTransaction() {
        try {
            inquiry = new Inquiry(atm.getID(), atm.getBankName(), atm.getPlace(), atm.getNetworkToBank(), card, 1414);
            inquiry.serialNumber = 1;
            inquiry.balances.setBalances(new Money(100), new Money(40));
            Receipt receipt = inquiry.completeTransaction();

            assertNotNull(receipt);
            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 123456 TRANS #1", lines.get(3));
            assertEquals("INQUIRY FROM: CHKG", lines.get(4));
            assertEquals("", lines.get(5));
            assertEquals("TOTAL BAL: $100.00", lines.get(6));
            assertEquals("AVAILABLE: $40.00", lines.get(7));
        } catch (Cancelled ex) {
            fail("An error occured during complete inquiry");
        }
    }

}

package edu.gordon.atm.transaction;

import helper.TransactionTestHelper;
import edu.gordon.atm.Session;
import edu.gordon.atm.physical.CustomerConsole;
import edu.gordon.banking.Card;
import edu.gordon.banking.Message;
import edu.gordon.banking.Receipt;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test d'integration entre les differentes composantes (transaction -
 * simulation - simulatedBank) pour verifier la fonctionnalites des 4 requis : -
 * Deposit - Transfer - Inquiry - Withdrawal
 *
 * Pour faire les test d'integration, l'atm doit etre mocké (partiellement via
 * un spy) pour simuler les actions utilisateurs sur la console afin de generer
 * une transaction complete et utiliser la classe simulatedBank. Les seules
 * actiosn simulees sont les input faite par l'utilisateur via la console de
 * l'atm. De cette facon, nous pouvons tester entierement le flow de chacun des
 * requis.
 *
 * Devra etre revu apres la mise en place de l'architecture en couche
 *
 * @author Zeldorine
 */
public class TransactionTest extends TransactionTestHelper {

    Session session;
    Card card;

    public TransactionTest() {
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
        initATMMock();
        card = new Card(2);
        session = new Session(atm);
    }

    @After
    public void tearDown() {
    }

    // @Test
    public void testMakeTransactionWithdrawal() {
    }

    // @Test
    public void testMakeTransactionDeposit() {
    }

    //  @Test
    public void testMakeTransactionTransfer() {
    }

    //  @Test
    public void testMakeTransactionInquiry() {
    }

    //  @Test
    public void testMakeTransactionNull() {
    }

    //  @Test
    public void testPerformTransactionDepositSuccess() {

    }

    //  @Test
    public void testPerformTransactionTransferSuccess() {

    }

    @Test
    public void testPerformTransactionInquirySuccess() {
        try {
            setMenuChoice(3);
            Transaction inquiry = Transaction.makeTransaction(atm, session, card, 1234);
            inquiry.serialNumber = 1;
            setMenuChoice(0);
            inquiry.performTransaction();

            Message message = inquiry.message;
            Receipt receipt = inquiry.receipt;

            Assert.assertNotNull(message);
            Assert.assertNotNull(receipt);

            assertEquals("$0.00", message.getAmount().toString());
            assertEquals(card, message.getCard());
            assertEquals(0, message.getFromAccount());
            assertEquals(-1, message.getToAccount());
            assertEquals(Message.INQUIRY, message.getMessageCode());
            assertEquals(1234, message.getPIN());
            assertEquals("INQUIRY  CARD# 2 TRANS# 1 FROM  0 NO TO NO AMOUNT", message.toString());

            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 2 TRANS #1", lines.get(3));
            assertEquals("INQUIRY FROM: CHKG", lines.get(4));
            assertEquals("", lines.get(5));
            assertEquals("TOTAL BAL: $100.00", lines.get(6));
            assertEquals("AVAILABLE: $100.00", lines.get(7));
        } catch (CustomerConsole.Cancelled ex) {
            fail();
        } catch (Transaction.CardRetained ex) {
            fail();
        }
    }

    //@Test
    public void testPerformTransactionWithdrawalSuccess() {

    }

    // @Test
    public void testPerformTransactionDepositInvalidPin() {

    }

//    @Test
    public void testPerformTransactionTransferInvalidPin() {

    }

    // @Test
    public void testPerformTransactionInquiryInvalidPin() {

    }

    //  @Test
    public void testPerformTransactionWithdrawalInvalidPin() {

    }
}

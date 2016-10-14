package edu.gordon.transaction;

import helper.TransactionTestHelper;
import edu.gordon.atm.Session;
import edu.gordon.banking.Card;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import edu.gordon.exception.Cancelled;
import edu.gordon.exception.CardRetained;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

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
        initATMMock(true);
        card = new Card(2);
        session = new Session(atm);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMakeTransactionWithdrawal() {
        try {
            setMenuChoice(0);
            Transaction withdrawal = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 0, atm.getNetworkToBank(), card, 1414);
            Assert.assertNotNull(withdrawal);
            assertThat(withdrawal, instanceOf(Withdrawal.class));
        } catch (Cancelled ex) {
            fail("Fail to create withdrawal trx");
        }
    }

    @Test
    public void testMakeTransactionDeposit() {
        try {
            setMenuChoice(1);
            Transaction deposit = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 1, atm.getNetworkToBank(), card, 1414);
            Assert.assertNotNull(deposit);
            assertThat(deposit, instanceOf(Deposit.class));
        } catch (Cancelled ex) {
            fail("Fail to create deposit trx");
        }
    }

    @Test
    public void testMakeTransactionTransfer() {
        try {
            setMenuChoice(2);
            Transaction transfer = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 2, atm.getNetworkToBank(), card, 1414);
            Assert.assertNotNull(transfer);
            assertThat(transfer, instanceOf(Transfer.class));
        } catch (Cancelled ex) {
            fail("Fail to create transfer trx");
        }
    }

    @Test
    public void testMakeTransactionInquiry() {
        try {
            setMenuChoice(3);
            Transaction inquiry = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 3, atm.getNetworkToBank(), card, 1414);
            Assert.assertNotNull(inquiry);
            assertThat(inquiry, instanceOf(Inquiry.class));
        } catch (Cancelled ex) {
            fail("Fail to create inquiry trx");
        }
    }

    @Test
    public void testMakeTransactionNull() {
        try {
            setMenuChoice(4);
            Transaction nullValue = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 4, atm.getNetworkToBank(), card, 1414);
            Assert.assertNull(nullValue);
        } catch (Cancelled ex) {
            fail("Fail to create null trx");
        }
    }

    @Test
    public void testPerformTransactionDepositSuccess() {
        try {
            setMenuChoice(1);
            setReadAmount(new Money(100));
            Transaction deposit = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 1, atm.getNetworkToBank(), card, 1234);
            assertThat(deposit, instanceOf(Deposit.class));
            deposit.serialNumber = 1;
            setMenuChoice(0);
            session.performTransaction(deposit);
            setMenuChoice(0);

            Message message = session.getMessage();
            Receipt receipt = session.getReceipt();

            Assert.assertNotNull(message);
            Assert.assertNotNull(receipt);

            assertEquals("$100.00", message.getAmount().toString());
            assertEquals(card, message.getCard());
            assertEquals(-1, message.getFromAccount());
            assertEquals(0, message.getToAccount());
            assertEquals(Message.INITIATE_DEPOSIT, message.getMessageCode());
            assertEquals(1234, message.getPIN());
            assertEquals("INIT_DEP CARD# 2 TRANS# 1 NO FROM TO  0 $100.00", message.toString());

            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 2 TRANS #1", lines.get(3));
            assertEquals("DEPOSIT TO: CHKG", lines.get(4));
            assertEquals("AMOUNT: $100.00", lines.get(5));
            assertEquals("TOTAL BAL: $200.00", lines.get(6));
            assertEquals("AVAILABLE: $100.00", lines.get(7));
        } catch (Cancelled ex) {
            fail();
        } catch (CardRetained ex) {
            fail();
        }
    }

    @Test
    public void testPerformTransactionDepositNoEnvelop() {
        try {
            setMenuChoice(1);
            setReadAmount(new Money(100));
            Transaction withdrawal = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 1, atm.getNetworkToBank(), card, 1234);
            assertThat(withdrawal, instanceOf(Deposit.class));
            withdrawal.serialNumber = 1;
            setMenuChoice(0);
            session.performTransaction(withdrawal);
        } catch (Cancelled ex) {
            //C'est ce qu'on veut
        } catch (CardRetained ex) {
            fail();
        }
    }

    @Test
    public void testPerformTransactionTransferSuccess() {
        try {
            setMenuChoice(2);
            Transaction transfer = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 2, atm.getNetworkToBank(), card, 1234);
            assertThat(transfer, instanceOf(Transfer.class));
            transfer.serialNumber = 1;
            setMenuChoice(0);

            resetConsoleMock();
            setReadAmount(new Money(100));
            Mockito.when(atm.getCustomerConsole().readMenuChoice(Mockito.eq("Account to transfer from"), Mockito.any(String[].class))).thenReturn(2);
            Mockito.when(atm.getCustomerConsole().readMenuChoice(Mockito.eq("Account to transfer to"), Mockito.any(String[].class))).thenReturn(0);
            Mockito.when(atm.getCustomerConsole().readMenuChoice(Mockito.contains("Would you like to do another transaction?"), Mockito.any(String[].class))).thenReturn(1);

            session.performTransaction(transfer);
            setMenuChoice(1);

            Message message = session.getMessage();
            Receipt receipt = session.getReceipt();

            Assert.assertNotNull(message);
            Assert.assertNotNull(receipt);

            assertEquals("$100.00", message.getAmount().toString());
            assertEquals(card, message.getCard());
            assertEquals(2, message.getFromAccount());
            assertEquals(0, message.getToAccount());
            assertEquals(Message.TRANSFER, message.getMessageCode());
            assertEquals(1234, message.getPIN());
            assertEquals("TRANSFER CARD# 2 TRANS# 1 FROM  2 TO  0 $100.00", message.toString());

            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 2 TRANS #1", lines.get(3));
            assertEquals("TRANSFER FROM: MMKT TO: CHKG", lines.get(4));
            assertEquals("AMOUNT: $100.00", lines.get(5));
            assertEquals("TOTAL BAL: $200.00", lines.get(6));
            assertEquals("AVAILABLE: $200.00", lines.get(7));
        } catch (Cancelled ex) {
            resetConsoleMock();
            fail();
        } catch (CardRetained ex) {
            resetConsoleMock();
            fail();
        }

        resetConsoleMock();
    }

    @Test
    public void testPerformTransactionInquirySuccess() {
        try {
            setMenuChoice(3);
            Transaction inquiry = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 3, atm.getNetworkToBank(), card, 1234);
            inquiry.serialNumber = 1;
            setMenuChoice(0);
            session.performTransaction(inquiry);

            Message message = session.getMessage();
            Receipt receipt = session.getReceipt();

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
        } catch (Cancelled ex) {
            fail();
        } catch (CardRetained ex) {
            fail();
        }
    }

    @Test
    public void testPerformTransactionWithdrawalSuccess() {
        try {
            setMenuChoice(0);
            Transaction withdrawal = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 0, atm.getNetworkToBank(), card, 1234);
            assertThat(withdrawal, instanceOf(Withdrawal.class));
            withdrawal.serialNumber = 1;
            setMenuChoice(0);
            session.performTransaction(withdrawal);

            Message message = session.getMessage();
            Receipt receipt = session.getReceipt();

            Assert.assertNotNull(message);
            Assert.assertNotNull(receipt);

            assertEquals("$20.00", message.getAmount().toString());
            assertEquals(card, message.getCard());
            assertEquals(0, message.getFromAccount());
            assertEquals(-1, message.getToAccount());
            assertEquals(Message.WITHDRAWAL, message.getMessageCode());
            assertEquals(1234, message.getPIN());
            assertEquals("WITHDRAW CARD# 2 TRANS# 1 FROM  0 NO TO $20.00", message.toString());

            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 2 TRANS #1", lines.get(3));
            assertEquals("WITHDRAWAL FROM: CHKG", lines.get(4));
            assertEquals("AMOUNT: $20.00", lines.get(5));
            assertEquals("TOTAL BAL: $80.00", lines.get(6));
            assertEquals("AVAILABLE: $80.00", lines.get(7));
        } catch (Cancelled ex) {
            fail();
        } catch (CardRetained ex) {
            fail();
        }
    }

    @Test
    public void testPerformTransactionInvalidPin() {
        try {
            setMenuChoice(3);
            Mockito.when(atm.getCustomerConsole().readPIN(Mockito.anyString())).thenReturn(-1);
            Transaction inquiry = session.makeTransaction(atm.getID(), atm.getBankName(),
                    atm.getPlace(), 3, atm.getNetworkToBank(), card, 1234);
            assertThat(inquiry, instanceOf(Inquiry.class));
            inquiry.serialNumber = 1;
            setMenuChoice(0);
            session.performTransaction(inquiry);

            //Status status = inquiry.
        } catch (Cancelled ex) {
            fail();
        } catch (CardRetained cardRetained) {
            // Ne rien faire, on veut cette exception !
        }
    }
}

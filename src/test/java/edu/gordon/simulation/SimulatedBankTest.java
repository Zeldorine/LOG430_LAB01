package edu.gordon.simulation;

import edu.gordon.network.SimulatedBank;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Card;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Status;
import helper.TransactionTestHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test unitaire pour la simulation de la bank selon les 4 requis
 *
 * @author Zeldorine
 */
public class SimulatedBankTest extends TransactionTestHelper {

    SimulatedBank simulatedBank;
    Balances balances;

    public SimulatedBankTest() {
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
        simulatedBank = new SimulatedBank();

        balances = new Balances();
        balances.setBalances(new Money(200), new Money(200));
    }

    @After
    public void tearDown() {
        balances = new Balances();
        balances.setBalances(new Money(200), new Money(200));
    }

    @Test
    public void TestSendMessageWithdrawalSuccess() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 42, 1, 1, -1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(true, status.isSuccess());
        assertEquals("$900.00", balances.getTotal().toString());
        assertEquals("$900.00", balances.getAvailable().toString());
    }

    @Test
    public void TestSendMessageWithdrawalInvalidAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 42, 2, 2, -1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid account type", status.getMessage());
    }

    @Test
    public void TestSendMessageWithdrawalLimiteExceeded() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 42, 1, 1, -1, new Money(400));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Daily withdrawal limit exceeded", status.getMessage());
    }

    @Test
    public void TestSendMessageWithdrawalInsufficientBalances() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 42, 1, 0, -1, new Money(200));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Insufficient available balance", status.getMessage());
    }

    @Test
    public void TestSendMessageInitDepositSuccess() {
        Card card = new Card(1);
        Message message = new Message(Message.INITIATE_DEPOSIT, card, 42, 1, 1, 1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(true, status.isSuccess());
        assertEquals("$200.00", balances.getTotal().toString());
        assertEquals("$200.00", balances.getAvailable().toString());
    }

    @Test
    public void TestSendMessageInitDepositInvalidAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.INITIATE_DEPOSIT, card, 42, 2, 2, 2, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid account type", status.getMessage());
    }

    @Test
    public void TestSendMessageCmpltDepositSuccess() {
        Card card = new Card(1);
        Message message = new Message(Message.COMPLETE_DEPOSIT, card, 42, 1, 1, 1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(true, status.isSuccess());
        assertEquals("$1100.00", balances.getTotal().toString());
        assertEquals("$1000.00", balances.getAvailable().toString());
    }

    @Test
    public void TestSendMessageCmpltDepositInvalidAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.COMPLETE_DEPOSIT, card, 42, 2, 2, 2, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid account type", status.getMessage());
    }

    @Test
    public void TestSendMessageTransferSuccess() {
        Card card = new Card(1);
        Message message = new Message(Message.TRANSFER, card, 42, 1, 1, 0, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(true, status.isSuccess());
        assertEquals("$200.00", balances.getTotal().toString());
        assertEquals("$200.00", balances.getAvailable().toString());
    }

    @Test
    public void TestSendMessageTransferInvalidFromAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.TRANSFER, card, 42, 2, 2, 1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid from account type", status.getMessage());
    }

    @Test
    public void TestSendMessageTransferInvalidToAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.TRANSFER, card, 42, 1, 1, 2, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid to account type", status.getMessage());
    }

    @Test
    public void TestSendMessageTransferInvalidSameAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.TRANSFER, card, 42, 2, 1, 1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Can't transfer money from\n"
                + "an account to itself", status.getMessage());
    }

    @Test
    public void TestSendMessageTransferInsufficientBalance() {
        Card card = new Card(1);
        Message message = new Message(Message.TRANSFER, card, 42, 0, 1, 0, new Money(1500));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Insufficient available balance", status.getMessage());
    }

    @Test
    public void TestSendMessageInquirySuccess() {
        Card card = new Card(1);
        Message message = new Message(Message.INQUIRY, card, 42, 1, 1, 1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(true, status.isSuccess());
        assertEquals("$1000.00", balances.getTotal().toString());
        assertEquals("$1000.00", balances.getAvailable().toString());
    }

    @Test
    public void TestSendMessageInquiryInvalidAccount() {
        Card card = new Card(1);
        Message message = new Message(Message.INQUIRY, card, 42, 2, 2, 2, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid account type", status.getMessage());
    }

    @Test
    public void TestSendMessageInvalidPin() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 1414, 0, 1, -1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid PIN", status.getMessage());
    }

    @Test
    public void TestSendMessageNull() {
        Card card = new Card(1);
        Message message = new Message(-1, card, 42, 1, 1, -1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNull(status);
    }

    @Test
    public void TestSendInvalidCard() {
        Card card = new Card(123456);
        Message message = new Message(Message.WITHDRAWAL, card, 42, 1, 1, -1, new Money(100));
        Status status = simulatedBank.handleMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid card", status.getMessage());
    }
}

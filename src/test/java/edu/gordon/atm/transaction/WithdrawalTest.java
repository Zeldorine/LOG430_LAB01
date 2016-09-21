/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.atm.transaction;

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
 *
 * @author Zeldorine
 */
public class WithdrawalTest extends TransactionTestHelper {

    Transaction withdrawal;

    public WithdrawalTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            super.setUp();
            atm.getCashDispenser().setInitialCash(new Money(200));
            setMenuChoice(Message.WITHDRAWAL);
            setReadAmount(new Money(100));
        } catch (CustomerConsole.Cancelled ex) {
            fail("Error occured during set menu choice");
        }
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testCreate() {
        withdrawal = new Withdrawal(atm, session, card, 1414);
        assertNotNull(withdrawal);
    }

    @Test
    public void testGetSpecificsFromCustomer() {
        Message message = null;
        try {
            withdrawal = new Withdrawal(atm, session, card, 1414);
            withdrawal.serialNumber = 1;
            setMenuChoice(2);
            message = withdrawal.getSpecificsFromCustomer();
        } catch (CustomerConsole.Cancelled ex) {
            fail("error to get specifics message");
        }

        assertNotNull(message);
        assertEquals("$60.00", message.getAmount().toString());
        assertEquals(card, message.getCard());
        assertEquals(2, message.getFromAccount());
        assertEquals(-1, message.getToAccount());
        assertEquals(Message.WITHDRAWAL, message.getMessageCode());
        assertEquals(1414, message.getPIN());
        assertEquals("WITHDRAW CARD# 123456 TRANS# 1 FROM  2 NO TO $60.00", message.toString());
    }

    @Test
    public void testCompleteTransaction() {
        try {
            withdrawal = new Withdrawal(atm, session, card, 1414);
            withdrawal.serialNumber = 1;
            withdrawal.balances.setBalances(new Money(100), new Money(40));
            setMenuChoice(2);
            withdrawal.getSpecificsFromCustomer();
            Receipt receipt = withdrawal.completeTransaction();

            assertNotNull(receipt);
            List<String> lines = getLines(receipt);

            assertEquals(8, lines.size());
            assertEquals("Bank test", lines.get(1));
            assertEquals("ATM #0 test adress", lines.get(2));
            assertEquals("CARD 123456 TRANS #1", lines.get(3));
            assertEquals("WITHDRAWAL FROM: MMKT", lines.get(4));
            assertEquals("AMOUNT: $60.00", lines.get(5));
            assertEquals("TOTAL BAL: $100.00", lines.get(6));
            assertEquals("AVAILABLE: $40.00", lines.get(7));
        } catch (CustomerConsole.Cancelled ex) {
            fail("An error occured during complete inquiry");
        }
    }
}

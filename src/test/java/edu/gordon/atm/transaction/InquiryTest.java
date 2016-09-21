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
 * Test unitaire du requis inquiry pour la generation de message
 * 
 * @author Zeldorine
 */
public class InquiryTest extends TransactionTestHelper {

    Transaction inquiry;
    
    public InquiryTest() {
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
            setMenuChoice(Message.INQUIRY);
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
        inquiry = new Inquiry(atm, session, card, 1414);
        assertNotNull(inquiry);
    }

    @Test
    public void testGetSpecificsFromCustomer() {
        Message message = null;
        try {
            inquiry = new Inquiry(atm, session, card, 1414);
            inquiry.serialNumber = 1;
            message = inquiry.getSpecificsFromCustomer();
        } catch (CustomerConsole.Cancelled ex) {
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
            inquiry = new Inquiry(atm, session, card, 1414);
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
        } catch (CustomerConsole.Cancelled ex) {
            fail("An error occured during complete inquiry");
        }
    }

}

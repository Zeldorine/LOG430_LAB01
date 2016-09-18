/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.atm.transaction;

import edu.gordon.atm.ATM;
import edu.gordon.atm.Session;
import edu.gordon.atm.physical.CustomerConsole;
import edu.gordon.atm.transaction.Inquiry;
import edu.gordon.banking.AccountInformation;
import edu.gordon.banking.Card;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import edu.gordon.simulation.Simulation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Zeldorine
 */
public class InquiryTest {

    @Mock
    Card card;
    @Mock
    ATM atm;
    @Mock
    Session session;

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
            card = Mockito.mock(Card.class);
            Mockito.when(card.getNumber()).thenReturn(123456);

            atm = Mockito.mock(ATM.class);
            Mockito.when(atm.getBankName()).thenReturn("Bank test");
            Mockito.when(atm.getID()).thenReturn(0);
            Mockito.when(atm.getNetworkToBank()).thenReturn(null);
            Mockito.when(atm.getPlace()).thenReturn("test adress");
            Mockito.when(atm.getBankName()).thenReturn("Bank test");

            CustomerConsole console = Mockito.mock(CustomerConsole.class);
            Mockito.when(atm.getCustomerConsole()).thenReturn(console);

            Mockito.when(atm.getCustomerConsole().readMenuChoice(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(1);

            session = Mockito.mock(Session.class);
        } catch (CustomerConsole.Cancelled ex) {
            fail("Error occured during setUp test class");
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        Inquiry inquiry = new Inquiry(atm, session, card, 1414);
        assertNotNull(inquiry);
    }

    @Test
    public void testGetSpecificsFromCustomer() {
        Message message = null;
        try {
            Inquiry inquiry = new Inquiry(atm, session, card, 1414);
            message = inquiry.getSpecificsFromCustomer();
        } catch (CustomerConsole.Cancelled ex) {
            fail("error to get specifics message");
        }

        assertNotNull(message);
        assertEquals("$0.00", message.getAmount().toString());
        assertEquals(card, message.getCard());
        assertEquals(1, message.getFromAccount());
        assertEquals(-1, message.getToAccount());
        assertEquals(Message.INQUIRY, message.getMessageCode());
        assertEquals(1414, message.getPIN());
        assertEquals(1, message.getSerialNumber());
        assertEquals("INQUIRY  CARD# 123456 TRANS# 1 FROM  1 NO TO NO AMOUNT", message.toString());
    }

    @Test
    public void testCompleteTransaction() {
        Inquiry inquiry = new Inquiry(atm, session, card, 1414);
        inquiry.balances.setBalances(new Money(100), new Money(40));
        Receipt receipt = inquiry.completeTransaction();

        assertNotNull(receipt);
        Enumeration receiptLines = receipt.getLines();
        List<String> lines = new ArrayList();
        
        while (receiptLines.hasMoreElements()) {
            lines.add((String) receiptLines.nextElement());
        }
        
        assertEquals(8, lines.size());
        assertEquals("Bank test", lines.get(1));
        assertEquals("ATM #0 test adress", lines.get(2));
        assertEquals("CARD 123456 TRANS #3", lines.get(3));
        assertEquals("INQUIRY FROM: CHKG", lines.get(4));
        assertEquals("", lines.get(5));
        assertEquals("TOTAL BAL: $100.00", lines.get(6));
        assertEquals("AVAILABLE: $40.00", lines.get(7));
    }

}

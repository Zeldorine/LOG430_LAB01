package edu.gordon.banking;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 *
 * @author Zeldorine
 */
public class MessageTest {

    @Mock
    Card card;

    @Mock
    Money amount;

    Message message;

    public MessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        card = Mockito.mock(Card.class);
        amount = Mockito.mock(Money.class);
        Mockito.when(card.getNumber()).thenReturn(123456789);
        message = new Message(1, card, 1414, 6758, 1, 2, amount);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        assertNotNull(message);
    }

    @Test
    public void testCreateNegativeValue() {
        Mockito.when(card.getNumber()).thenReturn(-123456789);
        message = new Message(0, card, -1414, -6758, -1, -2, amount);
        assertNotNull(message);
    }

    @Test
    public void testSetPin() {
        message.setPIN(1536);
        assertEquals(1536, message.getPIN());
    }

    @Test
    public void testSetNegativePin() {
        message.setPIN(-1536);
        assertEquals(-1536, message.getPIN());
    }

    @Test
    public void testGetMessageCode() {
        assertEquals(1, message.getMessageCode());
    }

    @Test
    public void testGetPin() {
        assertEquals(1414, message.getPIN());
    }

    @Test
    public void testGetCard() {
        assertEquals(card, message.getCard());
    }

    @Test
    public void testGetSerialNumber() {
        assertEquals(6758, message.getSerialNumber());
    }

    @Test
    public void testGetFromAccount() {
        assertEquals(1, message.getFromAccount());
    }

    @Test
    public void testGetToAccount() {
        assertEquals(2, message.getToAccount());
    }

    @Test
    public void testGetAmount() {
        assertEquals(amount, message.getAmount());
    }
}

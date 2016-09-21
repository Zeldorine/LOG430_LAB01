package edu.gordon.banking;

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
public class CardTest {

    public CardTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        Card card = new Card(1234567889);
        assertNotNull(card);
    }

    @Test
    public void testNumber() {
        Card card = new Card(1234567889);
        assertEquals(1234567889, card.getNumber());
    }
}

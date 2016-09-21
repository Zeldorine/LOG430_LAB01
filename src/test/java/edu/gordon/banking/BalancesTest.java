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
public class BalancesTest {

    Balances balances;

    @Mock
    Money total;

    @Mock
    Money available;

    public BalancesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        balances = new Balances();
        total = Mockito.mock(Money.class);
        available = Mockito.mock(Money.class);

        balances.setBalances(total, available);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetTotal() {
        assertNotNull(balances.getTotal());
        assertEquals(total, balances.getTotal());
    }

    @Test
    public void testGetAvailable() {
        assertNotNull(balances.getAvailable());
        assertEquals(available, balances.getAvailable());
    }
}

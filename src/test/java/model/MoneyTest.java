/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import edu.gordon.banking.Money;
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
public class MoneyTest {

    public MoneyTest() {
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
        Money money = new Money(5);
        assertNotNull(money);
        assertEquals("$5.00", money.toString());

        money = new Money(0);
        assertNotNull(money);
        assertEquals("$0.00", money.toString());
    }

    @Test
    public void testCreateNegativeValue() {
        Money money = new Money(-5);
        assertNotNull(money);
        assertEquals("$-5.00", money.toString());

        money = new Money(-0);
        assertNotNull(money);
        assertEquals("$0.00", money.toString());
    }

    @Test
    public void testCreateTwoArguments() {
        Money money = new Money(5, 27);
        assertNotNull(money);
        assertEquals("$5.27", money.toString());
    }

    @Test
    public void testCreateCopy() {

    }
}

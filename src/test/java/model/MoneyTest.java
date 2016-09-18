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

        money = new Money(-5, 27);
        assertNotNull(money);
        assertEquals("$-4.0-73", money.toString());

        money = new Money(5, -27);
        assertNotNull(money);
        assertEquals("$4.73", money.toString());

        money = new Money(-5, -27);
        assertNotNull(money);
        assertEquals("$-5.0-27", money.toString());
    }

    @Test
    public void testCreateCopy() {
        Money money = new Money(5, 27);
        Money copy = new Money(money);
        assertNotNull(copy);
        assertEquals("$5.27", copy.toString());
    }

    @Test
    public void testAdd() {
        Money money = new Money(5, 27);
        Money money1 = new Money(money);
        money.add(money1);
        assertEquals("$10.54", money.toString());

        Money money2 = new Money(24, 58);
        money.add(money2);
        assertEquals("$35.12", money.toString());

        Money money3 = new Money(24, 58);
        money3.add(money3);
        assertEquals("$49.16", money3.toString());
        
        Money money4 = new Money(-24, 48);
        Money money5 = new Money(37, 72);
        money4.add(money5);
        assertEquals("$14.20", money4.toString());
        
        Money money6 = new Money(-24, -48);
        Money money7 = new Money(37, -72);
        money6.add(money7);
        assertEquals("$11.80", money6.toString());
    }

    @Test
    public void testSubstract() {
        Money money = new Money(5, 27);
        Money money1 = new Money(money);
        money.subtract(money1);
        assertEquals("$0.00", money.toString());

        Money money2 = new Money(24, 48);
        money.subtract(money2);
        assertEquals("$-24.0-48", money.toString());

        Money money3 = new Money(24, 48);
        money3.subtract(money1);
        assertEquals("$19.21", money3.toString());
        
        Money money4 = new Money(24, 48);
        Money money5 = new Money(37, 72);
        money4.subtract(money5);
        assertEquals("$-13.0-24", money4.toString());
    }

    @Test
    public void testLessEqual() {
        Money money = new Money(5, 27);
        Money copy = new Money(money);
        assertEquals(true, copy.lessEqual(money));
        
        money = new Money(5, 27);
        Money money1 = new Money(5, 26);
        assertEquals(true, money1.lessEqual(money));
        
        money = new Money(5, 27);
        money1 = new Money(5, 28);
        assertEquals(false, money1.lessEqual(money));
        
        money = new Money(5, 27);
        money1 = new Money(-5, 28);
        assertEquals(true, money1.lessEqual(money));
                
        money = new Money(-5, -27);
        money1 = new Money(-6, 28);
        assertEquals(true, money1.lessEqual(money));
    }
}

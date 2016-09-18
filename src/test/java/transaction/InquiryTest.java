/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import edu.gordon.atm.ATM;
import edu.gordon.atm.Session;
import edu.gordon.banking.Card;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
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
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() {
    }

    @Test
    public void testGetSpecificsFromCustomer() {
    }

    @Test
    public void testCompleteTransaction() {
    }

}

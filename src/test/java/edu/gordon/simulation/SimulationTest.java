package edu.gordon.simulation;

import edu.gordon.atm.physical.CustomerConsole;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Card;
import helper.TransactionTestHelper;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Status;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test integration de la simulation d'envoie de message envoye a la banque
 * simulee.
 *
 * Chaque cas d'erreur sont testes dans les tests unitaire couver par la classe
 * SimulatedBankTest.
 *
 * @author Zeldorine
 */
public class SimulationTest extends TransactionTestHelper {

    Simulation simulation;
    Balances balances;

    public SimulationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Simulation.timeToWait = 50;
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            init(false);
            simulation = new Simulation(atm);

            balances = new Balances();
            balances.setBalances(new Money(200), new Money(200));

            setMenuChoice(Message.WITHDRAWAL);
            setReadAmount(new Money(100));
        } catch (CustomerConsole.Cancelled ex) {
            fail("Error occured during set up simulation test");
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void TestSendMessageSuccess() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 42, 1, 1, -1, new Money(100));
        Status status = simulation.sendMessage(message, balances);
        assertNotNull(status);
        assertEquals(true, status.isSuccess());
    }

    @Test
    public void TestSendMessageFail() {
        Card card = new Card(1);
        Message message = new Message(Message.WITHDRAWAL, card, 0, 1, 1, -1, new Money(100));
        Status status = simulation.sendMessage(message, balances);
        assertNotNull(status);
        assertEquals(false, status.isSuccess());
        assertEquals("Invalid PIN", status.getMessage());
    }

}

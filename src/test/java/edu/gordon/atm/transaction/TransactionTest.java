/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.atm.transaction;

import edu.gordon.atm.ATM;
import edu.gordon.atm.Session;
import edu.gordon.atm.physical.CustomerConsole;
import edu.gordon.banking.Card;
import edu.gordon.banking.Money;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Test d'integration entre les differentes composantes (transaction -
 * simulation - simulatedBank) pour verifier la fonctionnalites des 4 requis : -
 * Deposit - Transfer - Inquiry - Withdrawal
 * 
 * Devra etre revu apres la mise en place de l'architecture en couche
 *
 * @author Zeldorine
 */
public class TransactionTest {

    @Mock
    Card card;
    @Mock
    ATM atm;
    @Mock
    Session session;
    @Mock
    CustomerConsole console;

    public TransactionTest() {
    }

    protected void setMenuChoice(int choice) throws CustomerConsole.Cancelled {
        Mockito.when(console.readMenuChoice(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(choice);
    }

    protected void setReadAmount(Money amount) throws CustomerConsole.Cancelled {
        Mockito.when(console.readAmount(Mockito.anyString())).thenReturn(amount);
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
        Mockito.when(card.getNumber()).thenReturn(123456);

        atm = Mockito.mock(ATM.class);
        Mockito.when(atm.getBankName()).thenReturn("Bank test");
        Mockito.when(atm.getID()).thenReturn(0);
        Mockito.when(atm.getNetworkToBank()).thenReturn(null);
        Mockito.when(atm.getPlace()).thenReturn("test adress");
        Mockito.when(atm.getBankName()).thenReturn("Bank test");

        console = Mockito.mock(CustomerConsole.class);
        Mockito.when(atm.getCustomerConsole()).thenReturn(console);

        session = Mockito.mock(Session.class);
    }

    @After
    public void tearDown() {
        card = null;
        atm = null;
        session = null;
        console = null;
    }

    @Test
    public void testMakeTransactionWithdrawal() {
    }

    @Test
    public void testMakeTransactionDeposit() {
    }

    @Test
    public void testMakeTransactionTransfer() {
    }

    @Test
    public void testMakeTransactionInquiry() {
    }

    @Test
    public void testMakeTransactionNull() {
    }
    
    @Test
    public void testPerformTransactionDepositSuccess(){
        
    }
    
        @Test
    public void testPerformTransactionTransferSuccess(){
        
    }
    
        @Test
    public void testPerformTransactionInquirySuccess(){
        
    }
    
        @Test
    public void testPerformTransactionWithdrawalSuccess(){
        
    }
    
        @Test
    public void testPerformTransactionDepositInvalidPin(){
        
    }
    
        @Test
    public void testPerformTransactionTransferInvalidPin(){
        
    }
    
        @Test
    public void testPerformTransactionInquiryInvalidPin(){
        
    }
    
        @Test
    public void testPerformTransactionWithdrawalInvalidPin(){
        
    }
}
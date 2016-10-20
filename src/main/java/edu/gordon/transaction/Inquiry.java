/* * ATM Example system - file Inquiry.java    * * copyright (c) 2001 - Russell C. Bjork * */package edu.gordon.transaction;import edu.gordon.banking.AccountInformation;import edu.gordon.banking.Balances;import edu.gordon.banking.Card;import edu.gordon.banking.Message;import edu.gordon.banking.Money;import edu.gordon.banking.Receipt;import edu.gordon.exception.Cancelled;import static edu.gordon.transaction.Transaction.nextSerialNumber;/** * Representation for a balance inquiry transaction */public class Inquiry extends Transaction {    /**     * Constructor     *     * @param edu.gordon.atm the ATM used to communicate with customer     * @param session the session in which the transaction is being performed     * @param card the customer's card     * @param pin the PIN entered by the customer     */    public Inquiry(int atmId, String bankName, String atmPlace,            Card card, int pin) {        this.card = card;        this.pin = pin;        this.serialNumber = nextSerialNumber++;        this.balances = new Balances();        this.atmId = atmId;        this.bankName = bankName;        this.atmPlace = atmPlace;        state = GETTING_SPECIFICS_STATE;    }    /**     * Get specifics for the transaction from the customer     *     * @return message to bank for initiating this transaction     * @exception CustomerConsole.Cancelled if customer cancelled this     * transaction     */    public Message getSpecificsFromCustomer(int from, int to, Money amount) throws Cancelled {        this.from = from;                return new Message(Message.INQUIRY,                card, pin, serialNumber, from, -1, new Money(0));    }    /**     * Complete an approved transaction     *     * @return receipt to be printed for this transaction     */    public Receipt completeTransaction() {        return new Receipt(atmId, bankName, atmPlace, this.card, serialNumber, this.balances) {            {                detailsPortion = new String[2];                detailsPortion[0] = "INQUIRY FROM: "                        + AccountInformation.ACCOUNT_ABBREVIATIONS[from];                detailsPortion[1] = "";            }        };    }    @Override    public boolean needFromAccount() {        return true;    }    @Override    public boolean needToAccount() {        return false;    }    @Override    public boolean needAmount() {        return false;    }    /**     * Account to inquire about     */    private int from;    @Override    public boolean needDispanceAmount() {        return false;    }    @Override    public boolean needAcceptEnvelope() {        return false;    }    @Override    public Message getEnvelopeAcceptMessage() {        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    }}
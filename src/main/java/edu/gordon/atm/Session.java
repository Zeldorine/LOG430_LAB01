/*
 * ATM Example system - file Session.java
 *
 * copyright (c) 2001 - Russell C. Bjork
 *
 */
package edu.gordon.atm;

import edu.gordon.event.DisplayMessageEvent;
import edu.gordon.event.ReceiptEvent;
import edu.gordon.banking.AccountInformation;
import edu.gordon.banking.Balances;
import edu.gordon.transaction.Transaction;
import edu.gordon.exception.Cancelled;
import edu.gordon.exception.CardRetained;
import edu.gordon.banking.Card;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Receipt;
import edu.gordon.banking.Status;
import edu.gordon.core.CustomerConsole;
import edu.gordon.transaction.Deposit;
import edu.gordon.transaction.Inquiry;
import static edu.gordon.transaction.Transaction.timeToWait;
import edu.gordon.transaction.Transfer;
import edu.gordon.transaction.Withdrawal;

/**
 * Representation for one ATM session serving a single customer.
 */
public class Session {

    /**
     * Constructor
     *
     * @param edu.gordon.atm the ATM on which the session is performed
     */
    public Session(ATM atm) {
        this.atm = atm;
        state = READING_CARD_STATE;
    }

    /**
     * Perform the Session Use Case
     */
    public void performSession() {
        Card card = null;
        Transaction currentTransaction = null;

        while (state != FINAL_STATE) {
            switch (state) {
                case READING_CARD_STATE:

                    Integer cardNumber = atm.getCardReader().readCard();

                    if (cardNumber != null) {
                        card = new Card(cardNumber);
                        state = READING_PIN_STATE;
                    } else {
                        atm.getEventBus().post(new DisplayMessageEvent("Unable to read card"));
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case READING_PIN_STATE:

                    try {
                        pin = atm.getCustomerConsole().readPIN(
                                "Please enter your PIN\n"
                                + "Then press ENTER");
                        state = CHOOSING_TRANSACTION_STATE;
                    } catch (Cancelled e) {
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case CHOOSING_TRANSACTION_STATE:

                    try {
                        int choice = atm.getCustomerConsole().readMenuChoice(
                                "Please choose transaction type", Transaction.getTrxMenu());

                        currentTransaction
                                = makeTransaction(atm.getID(), atm.getBankName(), atm.getPlace(), choice, card, pin);
                        state = PERFORMING_TRANSACTION_STATE;
                    } catch (Cancelled e) {
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case PERFORMING_TRANSACTION_STATE:

                    try {
                        boolean doAgain = performTransaction(currentTransaction);
                        if (doAgain) {
                            state = CHOOSING_TRANSACTION_STATE;
                        } else {
                            state = EJECTING_CARD_STATE;
                        }
                    } catch (CardRetained e) {
                        state = FINAL_STATE;
                    }
                    break;

                case EJECTING_CARD_STATE:

                    atm.getCardReader().ejectCard();
                    state = FINAL_STATE;
                    break;
            }
        }
    }

    /**
     * Create a transaction of an appropriate type by asking the customer what
     * type of transaction is desired and then returning a newly-created member
     * of the appropriate subclass
     *
     * @param edu.gordon.atm the ATM used to communicate with customer
     * @param session the session in which this transaction is being performed
     * @param card the customer's card
     * @param pin the PIN entered by the customer
     * @return a newly created Transaction object of the appropriate type
     * @exception CustomerConsole.Cancelled if the customer presses cancel
     * instead of choosing a transaction type
     */
    public Transaction makeTransaction(int atmId, String bankName, String atmPlace, int choice, Card card, int pin)
            throws Cancelled {
        switch (choice) {
            case 0:

                return new Withdrawal(atmId, bankName, atmPlace, card, pin);

            case 1:

                return new Deposit(atmId, bankName, atmPlace, card, pin);

            case 2:

                return new Transfer(atmId, bankName, atmPlace, card, pin);

            case 3:

                return new Inquiry(atmId, bankName, atmPlace, card, pin);

            default:

                return null;    // To keep compiler happy - should not happen!
        }
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public Message getMessage() {
        return message;
    }

    public boolean performTransaction(Transaction currentTransaction) throws CardRetained {
        String doAnotherMessage = "";
        status = null;
        receipt = null;
        message = null;
        state = GETTING_SPECIFICS_STATE;

        while (true) // Terminates by return in ASKING_DO_ANOTHER_STATE or exception
        {
            switch (state) {
                case GETTING_SPECIFICS_STATE:

                    try {
                        int from = currentTransaction.needFromAccount() ? getFromAccount(currentTransaction) : -1;
                        atm.getEventBus().post(new DisplayMessageEvent(""));
                        int to = currentTransaction.needToAccount() ? getToAccount(currentTransaction) : -1;
                        atm.getEventBus().post(new DisplayMessageEvent(""));
                        Money amount = currentTransaction.needAmount() ? getAmount(currentTransaction) : null;

                        message = currentTransaction.getSpecificsFromCustomer(from, to, amount); // get amount and to account
                        atm.getEventBus().post(new DisplayMessageEvent(""));
                        state = SENDING_TO_BANK_STATE;
                    } catch (Cancelled e) {
                        doAnotherMessage = "Last transaction was cancelled";
                        state = ASKING_DO_ANOTHER_STATE;
                    }

                    break;

                case SENDING_TO_BANK_STATE:

                    atm.getNetworkToBank().sendMessage(message, currentTransaction.getBalances());

                    if (status.isInvalidPIN()) {
                        state = INVALID_PIN_STATE;
                    } else if (status.isSuccess()) {
                        state = COMPLETING_TRANSACTION_STATE;
                    } else {
                        doAnotherMessage = status.getMessage();
                        state = ASKING_DO_ANOTHER_STATE;
                    }

                    break;

                case INVALID_PIN_STATE:

                    try {
                        status = performInvalidPINExtension(message, currentTransaction.getBalances());

                        // If customer repeatedly enters invalid PIN's, a
                        // CardRetained exception is thrown, and this method
                        // terminates
                        if (status.isSuccess()) {
                            state = COMPLETING_TRANSACTION_STATE;
                        } else {
                            doAnotherMessage = status.getMessage();
                            state = ASKING_DO_ANOTHER_STATE;
                        }
                    } catch (Cancelled e) {
                        doAnotherMessage = "Last transaction was cancelled";
                        state = ASKING_DO_ANOTHER_STATE;
                    }

                    break;

                case COMPLETING_TRANSACTION_STATE:

                    try {
                        if (currentTransaction.needDispanceAmount()) {
                            atm.getCashDispenser().dispenseCash(message.getAmount());
                        }

                        if (currentTransaction.needAcceptEnvelope()) {
                            if (evelopeInserted) {
                                atm.getNetworkToBank().sendMessage(currentTransaction.getEnvelopeAcceptMessage(), currentTransaction.getBalances());
                            }
                        }

                        receipt = currentTransaction.completeTransaction();
                        state = PRINTING_RECEIPT_STATE;
                    } catch (Cancelled e) {
                        doAnotherMessage = "Last transaction was cancelled";
                        state = ASKING_DO_ANOTHER_STATE;
                    }

                    break;

                case PRINTING_RECEIPT_STATE:
                    atm.getEventBus().post(new ReceiptEvent(receipt));
                    state = ASKING_DO_ANOTHER_STATE;

                    break;

                case ASKING_DO_ANOTHER_STATE:

                    if (doAnotherMessage.length() > 0) {
                        doAnotherMessage += "\n";
                    }

                    try {
                        String[] yesNoMenu = {"Yes", "No"};

                        boolean doAgain = atm.getCustomerConsole().readMenuChoice(
                                doAnotherMessage
                                + "Would you like to do another transaction?",
                                yesNoMenu) == 0;
                        return doAgain;
                    } catch (Cancelled e) {
                        return false;
                    }
            }
        }
    }

    public void setStatus(Status evt) {
        this.status = evt;
    }

    /**
     * Perform the Invalid PIN Extension - reset session pin to new value if
     * successful
     *
     * @return status code returned by bank from most recent re-submission of
     * transaction
     * @exception CustomerConsole.Cancelled if customer presses the CANCEL key
     * instead of re-entering PIN
     * @exception CardRetained if card was retained due to too many invalid
     * PIN's
     */
    public Status performInvalidPINExtension(Message message, Balances balances) throws Cancelled,
            CardRetained {
        status = null;
        for (int i = 0; i < 3; i++) {
            pin = atm.getCustomerConsole().readPIN(
                    "PIN was incorrect\nPlease re-enter your PIN\n"
                    + "Then press ENTER");
            atm.getEventBus().post(new DisplayMessageEvent(""));

            message.setPIN(pin);
            atm.getNetworkToBank().sendMessage(message, balances);

            if (!status.isInvalidPIN()) {
                setPIN(pin);
                return status;
            }
        }

        atm.getCardReader().retainCard();
        atm.getEventBus().post(new DisplayMessageEvent("Your card has been retained\nPlease contact the bank."));
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
        }
        atm.getEventBus().post(new DisplayMessageEvent(""));

        throw new CardRetained();
    }

    /**
     * Change the pin recorded for the customer (if invalid pin extension was
     * performed by a transaction
     *
     * @param pin the newly entered pin
     */
    public void setPIN(int pin) {
        this.pin = pin;
    }

    private int getFromAccount(Transaction trx) throws Cancelled {
        if (trx instanceof Withdrawal) {
            return atm.getCustomerConsole().readMenuChoice(
                    "Account to withdraw from",
                    AccountInformation.ACCOUNT_NAMES);
        } else if (trx instanceof Transfer) {
            return atm.getCustomerConsole().readMenuChoice(
                    "Account to transfer from",
                    AccountInformation.ACCOUNT_NAMES);
        } else if (trx instanceof Inquiry) {
            return atm.getCustomerConsole().readMenuChoice(
                    "Account to inquire from",
                    AccountInformation.ACCOUNT_NAMES);
        }

        return -1;
    }

    private int getToAccount(Transaction trx) throws Cancelled {
        if (trx instanceof Transfer) {
            return atm.getCustomerConsole().readMenuChoice(
                    "Account to transfer to",
                    AccountInformation.ACCOUNT_NAMES);
        } else if (trx instanceof Deposit) {
            return atm.getCustomerConsole().readMenuChoice(
                    "Account to deposit to",
                    AccountInformation.ACCOUNT_NAMES);
        }

        return -1;
    }

    private Money getAmount(Transaction trx) throws Cancelled {
        if (trx instanceof Withdrawal) {
            Money amount = null;
            String[] amountOptions = {"$20", "$40", "$60", "$100", "$200"};
            Money[] amountValues = {
                new Money(20), new Money(40), new Money(60),
                new Money(100), new Money(200)
            };

            String amountMessage = "";
            boolean validAmount = false;

            while (!validAmount) {
                amount = amountValues[atm.getCustomerConsole().readMenuChoice(
                        amountMessage + "Amount of cash to withdraw", amountOptions)];

                validAmount = atm.getCashDispenser().checkCashOnHand(amount);

                if (!validAmount) {
                    amountMessage = "Insufficient cash available\n";
                }
            }

            return amount;
        } else if (trx instanceof Transfer) {
            return atm.getCustomerConsole().readAmount("Amount to transfer");
        } else if (trx instanceof Deposit) {
            return atm.getCustomerConsole().readAmount("Amount to deposit");
        }

        return null;
    }

    private Status status;

    // Instance variables
    /**
     * The ATM on which the session is performed
     */
    private ATM atm;

    /**
     * The PIN entered (or re-entered) by the customer
     */
    private int pin;

    /**
     * The current state of the session
     */
    private int state;

    // Possible values for state
    /**
     * Reading the customer's card
     */
    private static final int READING_CARD_STATE = 1;

    /**
     * Asking the customer to enter a PIN
     */
    private static final int READING_PIN_STATE = 2;

    /**
     * Asking the customer to choose a transaction type
     */
    private static final int CHOOSING_TRANSACTION_STATE = 3;

    /**
     * Peforming a transaction
     */
    private static final int PERFORMING_TRANSACTION_STATE = 4;

    /**
     * Ejecting the customer's card
     */
    private static final int EJECTING_CARD_STATE = 5;

    /**
     * Session finished
     */
    private static final int FINAL_STATE = 6;

    // Possible values for state
    /**
     * Getting specifics of the transaction from customer
     */
    protected static final int GETTING_SPECIFICS_STATE = 1;

    /**
     * Sending transaction to bank
     */
    protected static final int SENDING_TO_BANK_STATE = 2;

    /**
     * Performing invalid PIN extension
     */
    protected static final int INVALID_PIN_STATE = 3;

    /**
     * Completing transaction
     */
    protected static final int COMPLETING_TRANSACTION_STATE = 4;

    /**
     * Printing receipt
     */
    protected static final int PRINTING_RECEIPT_STATE = 5;

    /**
     * Asking if customer wants to do another transaction
     */
    protected static final int ASKING_DO_ANOTHER_STATE = 6;

    Receipt receipt;
    Message message;
    Boolean evelopeInserted = false;

    public void setEnvelopeInserted(Boolean on) {
        evelopeInserted = true;
    }
}

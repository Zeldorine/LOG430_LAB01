/* * ATM Example system - file ATM.java * * copyright (c) 2001 - Russell C. Bjork * */package edu.gordon.atm;import Event.CardEvent;import Event.DisplayMessageEvent;import Event.StatusEvent;import Event.SwitchEvent;import com.google.common.eventbus.EventBus;import com.google.common.eventbus.Subscribe;import java.net.InetAddress;import edu.gordon.banking.Money;import edu.gordon.banking.Status;import edu.gordon.core.CardReader;import edu.gordon.core.CashDispenser;import edu.gordon.core.CoreFactory;import edu.gordon.core.CustomerConsole;import edu.gordon.core.Display;import edu.gordon.core.EnvelopeAcceptor;import edu.gordon.core.Keyboard;import edu.gordon.core.Log;import edu.gordon.core.OperatorPanel;import edu.gordon.core.ReceiptPrinter;import edu.gordon.core.Network;/** * Representation for the ATM itself. An object of this class "owns" the objects * representing the component parts of the ATM, and the communications network, * and is responsible for creating customer sessions which then use it to gain * access to the component parts. This is an active class - when an instance of * the class is created, a thread is executed that actually runs the system. */public class ATM implements Runnable {    private final EventBus eventBus;    /**     * Constructor     *     * @param id the unique ID for this ATM     * @param place the physical location of this ATM     * @param bankName the name of the bank owning this ATM     * @param bankAddress the Internet address of the bank     */    public ATM(int id, String place, String bankName, InetAddress bankAddress, CoreFactory factory) {        this.id = id;        this.place = place;        this.bankName = bankName;        this.bankAddress = bankAddress;        eventBus = new EventBus();        init(factory);        // Set up initial conditions when ATM first created        this.state = OFF_STATE;        switchOn = false;        cardInserted = false;    }    private void init(CoreFactory coreFactory) {        // Create objects corresponding to component parts        eventBus.register(this);        log = coreFactory.getLog();        cardReader = coreFactory.getCardReader(eventBus);        cashDispenser = coreFactory.getCashDispenser(log);        customerConsole = coreFactory.getCustomerConsole(eventBus);        envelopeAcceptor = coreFactory.getEnvelopeAcceptor(log);        networkToBank = coreFactory.getNetwork(eventBus, log, bankAddress);        operatorPanel = coreFactory.getOperatorPanel(eventBus);        receiptPrinter = coreFactory.getReceiptPrinter();        display = coreFactory.getDisplay();        keyboard = coreFactory.getKeyboard(display, envelopeAcceptor);        coreFactory.init();    }    // Methods corresponding to major responsibilities of the ATM    /**     * The main program/applet will create a Thread that executes this code.     */    public void run() {        //Session currentSession = null;        while (true) {            switch (state) {                case OFF_STATE:                    eventBus.post(new DisplayMessageEvent("Not currently available"));                    synchronized (this) {                        try {                            wait();                        } catch (InterruptedException e) {                        }                    }                    if (switchOn) {                        performStartup();                        state = IDLE_STATE;                    }                    break;                case IDLE_STATE:                    eventBus.post(new DisplayMessageEvent("Please insert your card"));                    cardInserted = false;                    synchronized (this) {                        try {                            wait();                        } catch (InterruptedException e) {                        }                    }                    if (cardInserted) {                        state = SERVING_CUSTOMER_STATE;                    } else if (!switchOn) {                        performShutdown();                        state = OFF_STATE;                    }                    break;                case SERVING_CUSTOMER_STATE:                    // The following will not return until the session has                    // completed                    session = new Session(this);                    session.performSession();                    state = IDLE_STATE;                    break;            }        }    }    /**     * Inform the ATM that the switch on the operator console has been moved to     * the "on" position.     */    public synchronized void switchOn() {        switchOn = true;        notify();    }    /**     * Inform the ATM that the switch on the operator console has been moved to     * the "off" position.     */    public synchronized void switchOff() {        switchOn = false;        notify();    }    /**     * Inform the ATM that a card has been inserted into the card reader.     */    public synchronized void cardInserted() {        cardInserted = true;        notify();    }    @Subscribe    public void handleEvent(StatusEvent evt) {        Status status = (Status) evt.getSource();        if (status != null) {            session.setStatus(status);        }    }    @Subscribe    public void handleEvent(CardEvent evt) {        Boolean cardinserted = (Boolean) evt.getSource();        if (cardinserted) {            cardInserted();        }    }    @Subscribe    public void handleEvent(SwitchEvent evt) {        Boolean on = (Boolean) evt.getSource();        if (on) {            switchOn();        } else {            switchOff();        }    }    // The following methods allow objects of other classes to access component    // parts of the ATM    /**     * Accessor for id     *     * @return unique id of this ATM     */    public int getID() {        return id;    }    /**     * Accessor for place     *     * @return physical location of this ATM     */    public String getPlace() {        return place;    }    /**     * Accessor for bank name     *     * @return name of bank owning this ATM     */    public String getBankName() {        return bankName;    }    /**     * Accessor for card reader     *     * @return card reader component of this ATM     */    public CardReader getCardReader() {        return cardReader;    }    /**     * Accessor for cash dispenser     *     * @return cash dispenser component of this ATM     */    public CashDispenser getCashDispenser() {        return cashDispenser;    }    /**     * Accessor for customer console     *     * @return customer console component of this ATM     */    public CustomerConsole getCustomerConsole() {        return customerConsole;    }    /**     * Accessor for envelope acceptor     *     * @return envelope acceptor component of this ATM     */    public EnvelopeAcceptor getEnvelopeAcceptor() {        return envelopeAcceptor;    }    public Display getDisplay() {        return display;    }    public Keyboard getKeyboard() {        return keyboard;    }    /**     * Accessor for log     *     * @return log component of this ATM     */    public Log getLog() {        return log;    }    /**     * Accessor for network to bank     *     * @return network connection to bank of this ATM     */    public Network getNetworkToBank() {        return networkToBank;    }    /**     * Accessor for operator panel     *     * @return operator panel component of this ATM     */    public OperatorPanel getOperatorPanel() {        return operatorPanel;    }    /**     * Accessor for receipt printer     *     * @return receipt printer component of this ATM     */    public ReceiptPrinter getReceiptPrinter() {        return receiptPrinter;    }    // Private methods    /**     * Perform the System Startup use case when switch is turned on     */    private void performStartup() {        Money initialCash = new Money(operatorPanel.getInitialCash());        cashDispenser.setInitialCash(initialCash);        networkToBank.openConnection();    }    /**     * Perform the System Shutdown use case when switch is turned off     */    private void performShutdown() {        networkToBank.closeConnection();    }    public EventBus getEventBus() {        return eventBus;    }    private Session session;    // Instance variables recording information about the ATM    /**     * Unique ID for this ATM     */    private int id;    /**     * Physical location of this ATM     */    private String place;    /**     * Name of the bank owning this ATM     */    private String bankName;    /**     * Internet address of the bank     */    private InetAddress bankAddress;    // Instance variables referring to the omponent parts of the ATM    /**     * The ATM's card reader     */    private CardReader cardReader;    /**     * The ATM's cash dispenser     */    private CashDispenser cashDispenser;    /**     * The ATM's customer console     */    private CustomerConsole customerConsole;    /**     * The ATM's envelope acceptor     */    private EnvelopeAcceptor envelopeAcceptor;    /**     * The ATM's log     */    private Log log;    /**     * The ATM's network connection to the bank     */    private Network networkToBank;    /**     * The ATM's operator panel     */    private OperatorPanel operatorPanel;    /**     * The ATM's receipt printer     */    private ReceiptPrinter receiptPrinter;    private Display display;    private Keyboard keyboard;    // State information    /**     * The current state of the ATM - one of the possible values listed below     */    private int state;    /**     * Becomes true when the operator panel informs the ATM that the switch has     * been turned on - becomes false when the operator panel informs the ATM     * that the switch has been turned off.     */    private boolean switchOn;    /**     * Becomes true when the card reader informs the ATM that a card has been     * inserted - the ATM will make this false when it has tried to read the     * card     */    private boolean cardInserted;    // Possible values for state    /**     * The ATM is off. The switch must be turned on before it can operate     */    private static final int OFF_STATE = 0;    /**     * The ATM is on, but idle. It can service a customer, or it can be shut     * down     */    private static final int IDLE_STATE = 1;    /**     * The ATM is servicing a customer.     */    private static final int SERVING_CUSTOMER_STATE = 2;}
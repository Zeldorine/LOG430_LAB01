package edu.gordon.network;

import edu.gordon.core.Network;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Status;
import edu.gordon.core.Log;
import java.net.InetAddress;

/**
 *
 * @author Zeldorine
 */
public class SimulatedNetworkToBank  implements Network {

    private SimulatedBank bank = new SimulatedBank();
    
    /**
     * Constructor
     *
     * @param log the log in which to record sending of messages and responses
     * @param bankAddress the network address of the bank
     */
    public SimulatedNetworkToBank(Log log, InetAddress bankAddress) {
        this.log = log;
        this.bankAddress = bankAddress;
    }

    @Override
    public void openConnection() {
        // Since the network is simulated, we don't have to do anything
    }

    @Override
    public void closeConnection() {
        // Since the network is simulated, we don't have to do anything
    }

    @Override
    public Status sendMessage(Message message, Balances balances) {
        // LogPhysical sending of the message

        log.logSend(message);

        // Simulate the sending of the message - here is where the real code
        // to actually send the message over the network would go
        Status result = bank.handleMessage(message, balances);

        // LogPhysical the response gotten back
        log.logResponse(result);

        return result;
    }

    // LogPhysical into which to record messages
    private Log log;

    // Network address of the bank
    private InetAddress bankAddress;
}

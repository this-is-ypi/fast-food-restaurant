package by.training.model;

import by.training.model.entity.Client;
import by.training.model.entity.state.ClientState;
import by.training.model.entity.state.ClientWithoutFoodState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents restaurant line. Line can contain
 * usual client and pre-order clients as well.
 */
public class RestaurantLine implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Lock LOCK = new ReentrantLock(true);
    private Queue<Client> usualLine;
    private Queue<Client> preOrderLine;
    private int totalClientNumber;

    public RestaurantLine(int totalClientNumber) {
        this.totalClientNumber = totalClientNumber;
        usualLine = new LinkedList<>();
        preOrderLine = new LinkedList<>();
    }

    /**
     * Generates clients with random pre-order value
     * and adds them to the line.
     */
    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < totalClientNumber; i++) {
            LOCK.lock();
            try {
                boolean preOrder = random.nextBoolean();
                ClientState state = ClientWithoutFoodState.getInstance();
                Client client = new Client(i + 1, preOrder, state);
                addClientInLine(client);
            } finally {
                LOCK.unlock();
            }
        }
    }

    public boolean hasNextClient() {
        LOCK.lock();
        try {
            int usualClientsInLine = usualLine.size();
            int preOrderClientInLine = preOrderLine.size();
            return (preOrderClientInLine > 0 || usualClientsInLine > 0);
        } finally {
            LOCK.unlock();
        }
    }

    public Client getClient() {
        Client nextClient = null;
        LOCK.lock();
        try {
            if (preOrderLine.size() > 0) {
                nextClient = preOrderLine.poll();
            } else {
                if (usualLine.size() > 0) {
                    nextClient = usualLine.poll();
                }
            }
        } finally {
            LOCK.unlock();
        }
        return nextClient;
    }

    private void addClientInLine(Client client) {
        LOCK.lock();
        int clientId = client.getId();
        try {
            if (client.isPreOrder()) {
                preOrderLine.add(client);
                LOGGER.info("Client " + clientId + " (pre-order) comes to restaurant.");
            } else {
                usualLine.add(client);
                LOGGER.info("Client " + clientId + "  comes to restaurant.");
            }
        } finally {
            LOCK.unlock();
        }
    }
}

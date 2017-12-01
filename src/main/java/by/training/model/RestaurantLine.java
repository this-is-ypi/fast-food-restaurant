package by.training.model;

import by.training.model.entity.Client;
import by.training.model.entity.state.ClientWithoutFoodState;
import by.training.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
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

    public RestaurantLine() {
        usualLine = new LinkedList<>();
        preOrderLine = new LinkedList<>();
    }

    /**
     * Generates clients with random pre-order value
     * and adds them to the line.
     */
    @Override
    public void run() {
        PropertyReader reader = PropertyReader.getInstance();
        int clientNumber = reader.getTotalClientNumber();
        Random random = new Random();

        for (int i = 0; i < clientNumber; i++) {
            LOCK.lock();
            try {
                Client client = new Client(i + 1,
                        random.nextBoolean(),
                        new ClientWithoutFoodState());
                addClientInLine(client);
            } finally {
                LOCK.unlock();
            }
        }
    }

    public boolean hasNextClient() {
        LOCK.lock();
        try {
            return (usualLine.size() > 0) || (preOrderLine.size() > 0);
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

package by.training.model;

import by.training.model.entity.Client;
import by.training.model.exception.RestaurantException;
import by.training.reader.PropertyReader;
import by.training.reader.exception.PropertyReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RestaurantLine implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Lock LOCK = new ReentrantLock(true);

    private PropertyReader reader;

    private Queue<Client> usualLine;

    private Queue<Client> preOrderLine;

    public RestaurantLine() throws RestaurantException {
        initLine();
    }

    public void run() {
        generateClients();
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

    private void initLine() throws RestaurantException {
        try {
            reader = PropertyReader.getInstance();
        } catch (PropertyReaderException e) {
            throw new RestaurantException(e.getMessage(), e.getCause());
        }

        usualLine = new LinkedList<>();
        preOrderLine = new LinkedList<>();
    }

    private void generateClients() {
        final int clientNumber = reader.getTotalClientNumber();
        final int preOrderClientNumber = reader.getPreOrderClientNumber();

        final int eachPreOrder;
        if (preOrderClientNumber > clientNumber) {
            eachPreOrder = 1;
        } else {
            if (preOrderClientNumber <= 0) {
                eachPreOrder = clientNumber;
            } else {
                eachPreOrder = clientNumber / preOrderClientNumber;
            }
        }

        for (int i = 0; i < clientNumber; i++) {
            Client client = new Client(i + 1);

            LOCK.lock();
            try {
                if (i != 0 && (i % eachPreOrder) == 0) {
                    client.setPreOrder(true);
                } else {
                    client.setPreOrder(false);
                }

                addClientInLine(client);
            } finally {
                LOCK.unlock();
            }
        }
    }

    private void addClientInLine(Client client) {
        LOCK.lock();
        try {
            if (client.isPreOrder()) {
                preOrderLine.add(client);
                LOGGER.info("Client " + client.getId() + " (preOrder) comes to restaurant.");
            } else {
                usualLine.add(client);
                LOGGER.info("Client " + client.getId() + "  comes to restaurant.");
            }
        } finally {
            LOCK.unlock();
        }
    }
}

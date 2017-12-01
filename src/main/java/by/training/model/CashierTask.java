package by.training.model;

import by.training.model.entity.Cashier;
import by.training.model.entity.Client;
import by.training.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents cashier-client dialog.
 */
public class CashierTask implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Lock LOCK = new ReentrantLock(true);

    private Queue<Cashier> cashierQueue;

    private Cashier cashier;

    private Client client;

    public CashierTask(Queue<Cashier> cashierQueue, Cashier cashier, Client client) {
        this.cashierQueue = cashierQueue;
        this.cashier = cashier;
        this.client = client;
    }

    /**
     * Simulates process of client servicing.
     * After it's done, returns cashier in cashier queue.
     */
    @Override
    public void run() {
        LOGGER.info("Cashier " + cashier.getId() + " in process.");
        try {
            PropertyReader reader = PropertyReader.getInstance();
            int cashierWorkTime = reader.getCashierWorkTime();
            Random random = new Random();
            int sleepTime = random.nextInt(cashierWorkTime);
            TimeUnit.MILLISECONDS.sleep(sleepTime);
            client.changeState();

            int clientId = client.getId();
            int cashierId = cashier.getId();
            LOGGER.info("Client " + clientId + " leaves Cashier " + cashierId + " with food.");

            LOCK.lock();
            try {
                cashierQueue.add(cashier);
                LOGGER.info("Cashier " + cashier.getId() + " is free.");
            } finally {
                LOCK.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package by.training.model;

import by.training.model.entity.Cashier;
import by.training.model.exception.RestaurantException;
import by.training.reader.PropertyReader;
import by.training.reader.exception.PropertyReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CashierTask implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Lock LOCK = new ReentrantLock(true);

    //[YY] you don't need it since you can get access to singletone from any point in code
    private PropertyReader reader;

    private Queue<Cashier> cashierQueue;

    private Cashier cashier;

    private final int clientId;

    public CashierTask(Queue<Cashier> cashierQueue, Cashier cashier, int clientId) throws RestaurantException {
        //[YY] so it is redundunt too
        try {
            reader = PropertyReader.getInstance();
        } catch (PropertyReaderException e) {
            throw new RestaurantException(e.getMessage(), e.getCause());
        }
        this.cashierQueue = cashierQueue;
        this.cashier = cashier;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        LOGGER.info("Cashier " + cashier.getId() + " in process.");
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(reader.getCashierWorkTime()));
            LOGGER.info("Client " + clientId + " leaves restaurant.");

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

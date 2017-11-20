package by.training.model;

import by.training.model.entity.Cashier;
import by.training.model.entity.Client;
import by.training.model.exception.RestaurantException;
import by.training.reader.PropertyReader;
import by.training.reader.exception.PropertyReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents restaurant. At initializing it creates
 * queue of Cashiers and initializes line of Clients
 * (creates object of RestaurantLine).
*/
public class Restaurant {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Lock LOCK = new ReentrantLock(true);

    /* WORKING_TIME guarantees service of all clients */
    private final int WORKING_TIME;

    private static PropertyReader reader;

    private Queue<Cashier> cashierList;

    private RestaurantLine line;

    public Restaurant() throws RestaurantException {
        try {
            reader = PropertyReader.getInstance();
        } catch (PropertyReaderException e) {
            throw new RestaurantException(e.getMessage(), e.getCause());
        }

        int totalClientNumber = reader.getTotalClientNumber();
        int cashierWorkTime = reader.getCashierWorkTime();

        WORKING_TIME = totalClientNumber * cashierWorkTime;

        initCashier();
        initLine();
    }

    /**
     * Creates Thread of RestaurantLine and executes it.
     * In parallel of line thread this method starts
     * executing of CashierTasks.
     * Method ends it's execution on WORKING_TIME expired.
     * @throws RestaurantException
     */
    public void startRestaurant() throws RestaurantException {
        LOGGER.info("Restaurant opens.");
        long startTime = System.currentTimeMillis();

        ExecutorService lineExecutor = Executors.newSingleThreadExecutor();
        lineExecutor.execute(line);
        lineExecutor.shutdown();

        while (System.currentTimeMillis() - startTime < WORKING_TIME) {

            LOCK.lock();
            try {
                if (line.hasNextClient() && cashierList.size() > 0) {

                    Cashier freeCashier = cashierList.poll();
                    Client nextClient = line.getClient();

                    int freeCashierId = freeCashier.getId();
                    int nextClientId = nextClient.getId();

                    LOGGER.info("Cashier " + freeCashierId + " took Client " + nextClientId);

                    ExecutorService cashierExecutor = Executors.newSingleThreadExecutor();
                    cashierExecutor.execute(new CashierTask(cashierList, freeCashier, nextClientId));
                    cashierExecutor.shutdown();
                }
            } finally {
                LOCK.unlock();
            }
        }
        LOGGER.info("Restaurant closes.");
    }

    private void initCashier() {
        cashierList = new LinkedList<>();
        int cashierNumber = reader.getCashierNumber();
        for (int i = 0; i < cashierNumber; i++) {
            cashierList.add(new Cashier(i + 1));
        }
    }

    private void initLine() throws RestaurantException {
        line = new RestaurantLine();
    }
}

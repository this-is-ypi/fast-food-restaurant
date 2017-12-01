package by.training.model;

import by.training.model.entity.Cashier;
import by.training.model.entity.Client;
import by.training.reader.PropertyReader;
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

    private Queue<Cashier> cashierList;

    private RestaurantLine line = new RestaurantLine();

    public Restaurant() {
        initCashier();
    }

    /**
     * Executes Restaurant line as a thread.
     * In parallel of restaurant line thread this method starts
     * executing of CashierTasks.
     * Method ends it's execution on working time expired.
     * It guarantees all clients are serviced.
     */
    public void startRestaurant() {

        LOGGER.info("Restaurant opens.");

        ExecutorService lineExecutor = Executors.newSingleThreadExecutor();
        lineExecutor.execute(line);
        lineExecutor.shutdown();

        PropertyReader reader = PropertyReader.getInstance();
        int totalClientNumber = reader.getTotalClientNumber();
        int cashierWorkTime = reader.getCashierWorkTime();
        int workingTime = totalClientNumber * cashierWorkTime;

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < workingTime) {

            LOCK.lock();
            try {
                if (line.hasNextClient() && cashierList.size() > 0) {

                    Cashier freeCashier = cashierList.poll();
                    Client nextClient = line.getClient();

                    int freeCashierId = freeCashier.getId();
                    int nextClientId = nextClient.getId();
                    LOGGER.info("Cashier " + freeCashierId + " took Client " + nextClientId);

                    ExecutorService cashierExecutor = Executors.newSingleThreadExecutor();
                    cashierExecutor.execute(new CashierTask(cashierList, freeCashier, nextClient));
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
        PropertyReader reader = PropertyReader.getInstance();
        int cashierNumber = reader.getCashierNumber();
        for (int i = 0; i < cashierNumber; i++) {
            cashierList.add(new Cashier(i + 1));
        }
    }
}

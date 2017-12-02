package by.training.model;

import by.training.model.entity.Cashier;
import by.training.model.entity.Client;
import by.training.reader.PropertyHolder;
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
    private RestaurantLine line;
    private PropertyHolder holder;

    public Restaurant(PropertyHolder holder) {
        this.holder = holder;
        init();
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

        int totalClientNumber = holder.getTotalClientNumber();
        int cashierWorkTime = holder.getCashierWorkTime();
        int timeToWork = totalClientNumber * cashierWorkTime;

        long startTime = System.currentTimeMillis();
        long workingTime = 0;

        while (workingTime < timeToWork) {
            LOCK.lock();
            try {
                int cashierNumber = cashierList.size();
                if (line.hasNextClient() && cashierNumber > 0) {

                    Cashier freeCashier = cashierList.poll();
                    int freeCashierId = freeCashier.getId();

                    Client nextClient = line.getClient();
                    int nextClientId = nextClient.getId();

                    LOGGER.info("Cashier " + freeCashierId + " took Client " + nextClientId);

                    ExecutorService cashierExecutor = Executors.newSingleThreadExecutor();
                    CashierTask task = new CashierTask(cashierList,
                            freeCashier,
                            nextClient,
                            cashierWorkTime);
                    cashierExecutor.execute(task);
                    cashierExecutor.shutdown();
                }
            } finally {
                LOCK.unlock();
            }

            workingTime = System.currentTimeMillis() - startTime;
        }
        LOGGER.info("Restaurant closes.");
    }

    private void init() {
        line = new RestaurantLine(holder.getTotalClientNumber());

        cashierList = new LinkedList<>();
        int cashierNumber = holder.getCashierNumber();
        for (int i = 0; i < cashierNumber; i++) {
            cashierList.add(new Cashier(i + 1));
        }
    }
}

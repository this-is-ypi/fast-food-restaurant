package by.training.model.entity.state;

import by.training.model.entity.Client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientWithFoodState implements ClientState {

    private static final Lock LOCK = new ReentrantLock(true);
    private static ClientWithFoodState instance = null;
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    private ClientWithFoodState(){
    }

    public static ClientWithFoodState getInstance() {
        if (!isInitialized.get()) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new ClientWithFoodState();
                    isInitialized.set(true);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public void changeState(Client client) {
        ClientWithoutFoodState newState = ClientWithoutFoodState.getInstance();
        client.setState(newState);
    }
}

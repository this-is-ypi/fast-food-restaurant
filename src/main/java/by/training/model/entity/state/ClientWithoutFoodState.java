package by.training.model.entity.state;

import by.training.model.entity.Client;

public class ClientWithoutFoodState implements ClientState {

    @Override
    public void changeState(Client client) {
        client.setState(new ClientWithFoodState());
    }
}

package by.training.model.entity.clientState;

import by.training.model.entity.Client;

public class ClientWithFoodState implements ClientState {

    @Override
    public void changeState(Client client) {
        client.setState(new ClientWithoutFoodState());
    }
}

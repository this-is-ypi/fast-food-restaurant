package by.training.model.entity;

import by.training.model.entity.clientState.ClientState;

public class Client {

    private int id;

    private boolean preOrder;

    private ClientState state;

    public Client(int id, boolean preOrder, ClientState state) {
        this.id = id;
        this.preOrder = preOrder;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public boolean isPreOrder() {
        return preOrder;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public void changeState() {
        state.changeState(this);
    }
}

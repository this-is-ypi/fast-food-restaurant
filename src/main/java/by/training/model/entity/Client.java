package by.training.model.entity;

public class Client {

    private int id;

    private boolean preOrder;

    public Client(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isPreOrder() {
        return preOrder;
    }

    public void setPreOrder(boolean preOrder) {
        this.preOrder = preOrder;
    }
}

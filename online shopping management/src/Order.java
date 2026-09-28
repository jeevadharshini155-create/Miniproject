public class Order {

    private int customerId;
    private int productId;
    private int quantity;

    public Order(int customerId, int productId, int quantity) {

        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}

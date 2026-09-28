public class Product {

    private String productName;
    private String category;
    private double price;
    private int quantity;

    public Product(String productName, String category,
                   double price, int quantity) {

        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

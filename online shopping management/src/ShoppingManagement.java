import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ShoppingManagement {

    static Scanner sc = new Scanner(System.in);


    // =========================
    // ADD CUSTOMER
    // =========================

    public static void addCustomer() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.println("Enter Customer Name:");
            String name = sc.nextLine();

            System.out.println("Enter Email:");
            String email = sc.nextLine();

            System.out.println("Enter Phone:");
            String phone = sc.nextLine();

            String sql = "INSERT INTO customer "
                       + "(name, email, phone) "
                       + "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer Added Successfully!");
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // VIEW CUSTOMERS
    // =========================

    public static void viewCustomers() {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM customer";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- CUSTOMER DETAILS -----");

            while (rs.next()) {

                System.out.println("Customer ID: "
                        + rs.getInt("customer_id"));

                System.out.println("Name: "
                        + rs.getString("name"));

                System.out.println("Email: "
                        + rs.getString("email"));

                System.out.println("Phone: "
                        + rs.getString("phone"));

                System.out.println("---------------------------");
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // ADD PRODUCT
    // =========================

    public static void addProduct() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.println("Enter Product Name:");
            String productName = sc.nextLine();

            System.out.println("Enter Category:");
            String category = sc.nextLine();

            System.out.println("Enter Price:");
            double price = sc.nextDouble();

            System.out.println("Enter Quantity:");
            int quantity = sc.nextInt();

            sc.nextLine();

            String sql = "INSERT INTO product "
                       + "(product_name, category, price, quantity) "
                       + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, productName);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Product Added Successfully!");
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // VIEW PRODUCTS
    // =========================

    public static void viewProducts() {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM product";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- PRODUCT DETAILS -----");

            while (rs.next()) {

                System.out.println("Product ID: "
                        + rs.getInt("product_id"));

                System.out.println("Product Name: "
                        + rs.getString("product_name"));

                System.out.println("Category: "
                        + rs.getString("category"));

                System.out.println("Price: ₹"
                        + rs.getDouble("price"));

                System.out.println("Quantity: "
                        + rs.getInt("quantity"));

                System.out.println("---------------------------");
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // SEARCH PRODUCT
    // =========================

    public static void searchProduct() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.println("Enter Product Name:");
            String productName = sc.nextLine();

            String sql =
                "SELECT * FROM product WHERE product_name = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, productName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n----- PRODUCT FOUND -----");

                System.out.println("Product ID: "
                        + rs.getInt("product_id"));

                System.out.println("Product Name: "
                        + rs.getString("product_name"));

                System.out.println("Category: "
                        + rs.getString("category"));

                System.out.println("Price: ₹"
                        + rs.getDouble("price"));

                System.out.println("Quantity: "
                        + rs.getInt("quantity"));

            } else {

                System.out.println("Product Not Found!");
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // PLACE ORDER
    // =========================

    public static void placeOrder() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.println("Enter Customer ID:");
            int customerId = sc.nextInt();

            System.out.println("Enter Product ID:");
            int productId = sc.nextInt();

            System.out.println("Enter Quantity:");
            int orderQuantity = sc.nextInt();

            sc.nextLine();


            // Check customer

            String checkCustomer =
                "SELECT * FROM customer WHERE customer_id = ?";

            PreparedStatement customerPs =
                con.prepareStatement(checkCustomer);

            customerPs.setInt(1, customerId);

            ResultSet customerRs =
                customerPs.executeQuery();

            if (!customerRs.next()) {

                System.out.println("Customer Not Found!");

                con.close();

                return;
            }


            // Check product

            String checkProduct =
                "SELECT price, quantity FROM product "
              + "WHERE product_id = ?";

            PreparedStatement productPs =
                con.prepareStatement(checkProduct);

            productPs.setInt(1, productId);

            ResultSet productRs =
                productPs.executeQuery();

            if (!productRs.next()) {

                System.out.println("Product Not Found!");

                con.close();

                return;
            }


            double price =
                productRs.getDouble("price");

            int availableQuantity =
                productRs.getInt("quantity");


            // Check stock

            if (orderQuantity <= 0) {

                System.out.println("Invalid Quantity!");

                con.close();

                return;
            }

            if (orderQuantity > availableQuantity) {

                System.out.println(
                    "Not Enough Stock Available!"
                );

                con.close();

                return;
            }


            // Calculate total

            double totalAmount =
                price * orderQuantity;


            // Insert order

            String orderSql =
                "INSERT INTO orders "
              + "(customer_id, order_date, total_amount, status) "
              + "VALUES (?, CURDATE(), ?, ?)";

            PreparedStatement orderPs =
                con.prepareStatement(
                    orderSql,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                );

            orderPs.setInt(1, customerId);
            orderPs.setDouble(2, totalAmount);
            orderPs.setString(3, "Placed");

            int rows = orderPs.executeUpdate();


            if (rows > 0) {

                ResultSet generatedKeys =
                    orderPs.getGeneratedKeys();

                int orderId = 0;

                if (generatedKeys.next()) {

                    orderId =
                        generatedKeys.getInt(1);
                }


                // Insert order item

                String itemSql =
                    "INSERT INTO order_items "
                  + "(order_id, product_id, quantity, price) "
                  + "VALUES (?, ?, ?, ?)";

                PreparedStatement itemPs =
                    con.prepareStatement(itemSql);

                itemPs.setInt(1, orderId);
                itemPs.setInt(2, productId);
                itemPs.setInt(3, orderQuantity);
                itemPs.setDouble(4, price);

                itemPs.executeUpdate();


                // Reduce product quantity

                String updateProduct =
                    "UPDATE product "
                  + "SET quantity = quantity - ? "
                  + "WHERE product_id = ?";

                PreparedStatement updatePs =
                    con.prepareStatement(updateProduct);

                updatePs.setInt(1, orderQuantity);
                updatePs.setInt(2, productId);

                updatePs.executeUpdate();


                System.out.println(
                    "Order Placed Successfully!"
                );

                System.out.println(
                    "Order ID: " + orderId
                );

                System.out.println(
                    "Total Amount: ₹" + totalAmount
                );
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // VIEW ORDERS
    // =========================

    public static void viewOrders() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT o.order_id, c.name, "
              + "o.order_date, o.total_amount, o.status "
              + "FROM orders o "
              + "JOIN customer c "
              + "ON o.customer_id = c.customer_id";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            System.out.println("\n----- ORDER DETAILS -----");

            while (rs.next()) {

                System.out.println(
                    "Order ID: "
                    + rs.getInt("order_id")
                );

                System.out.println(
                    "Customer: "
                    + rs.getString("name")
                );

                System.out.println(
                    "Order Date: "
                    + rs.getDate("order_date")
                );

                System.out.println(
                    "Total Amount: ₹"
                    + rs.getDouble("total_amount")
                );

                System.out.println(
                    "Status: "
                    + rs.getString("status")
                );

                System.out.println(
                    "------------------------"
                );
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // CANCEL ORDER
    // =========================

    public static void cancelOrder() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.println("Enter Order ID:");
            int orderId = sc.nextInt();

            sc.nextLine();


            // Check order

            String checkSql =
                "SELECT status FROM orders "
              + "WHERE order_id = ?";

            PreparedStatement checkPs =
                con.prepareStatement(checkSql);

            checkPs.setInt(1, orderId);

            ResultSet rs =
                checkPs.executeQuery();

            if (!rs.next()) {

                System.out.println(
                    "Order Not Found!"
                );

                con.close();

                return;
            }


            String status =
                rs.getString("status");


            if (status.equals("Cancelled")) {

                System.out.println(
                    "Order Already Cancelled!"
                );

                con.close();

                return;
            }


            // Get product and quantity

            String itemSql =
                "SELECT product_id, quantity "
              + "FROM order_items "
              + "WHERE order_id = ?";

            PreparedStatement itemPs =
                con.prepareStatement(itemSql);

            itemPs.setInt(1, orderId);

            ResultSet itemRs =
                itemPs.executeQuery();


            while (itemRs.next()) {

                int productId =
                    itemRs.getInt("product_id");

                int quantity =
                    itemRs.getInt("quantity");


                // Restore stock

                String updateProduct =
                    "UPDATE product "
                  + "SET quantity = quantity + ? "
                  + "WHERE product_id = ?";

                PreparedStatement updatePs =
                    con.prepareStatement(
                        updateProduct
                    );

                updatePs.setInt(1, quantity);
                updatePs.setInt(2, productId);

                updatePs.executeUpdate();
            }


            // Update order status

            String updateOrder =
                "UPDATE orders "
              + "SET status = 'Cancelled' "
              + "WHERE order_id = ?";

            PreparedStatement orderPs =
                con.prepareStatement(updateOrder);

            orderPs.setInt(1, orderId);

            orderPs.executeUpdate();


            System.out.println(
                "Order Cancelled Successfully!"
            );

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        while (true) {

            System.out.println(
                "\n========== ONLINE SHOPPING MANAGEMENT SYSTEM =========="
            );

            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Add Product");
            System.out.println("4. View Products");
            System.out.println("5. Search Product");
            System.out.println("6. Place Order");
            System.out.println("7. View Orders");
            System.out.println("8. Cancel Order");
            System.out.println("9. Exit");

            System.out.println(
                "========================================================"
            );

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            sc.nextLine();


            switch (choice) {

                case 1:
                    addCustomer();
                    break;

                case 2:
                    viewCustomers();
                    break;

                case 3:
                    addProduct();
                    break;

                case 4:
                    viewProducts();
                    break;

                case 5:
                    searchProduct();
                    break;

                case 6:
                    placeOrder();
                    break;

                case 7:
                    viewOrders();
                    break;

                case 8:
                    cancelOrder();
                    break;

                case 9:

                    System.out.println(
                        "Thank you for using Online Shopping Management System!"
                    );

                    System.exit(0);

                default:

                    System.out.println(
                        "Invalid Choice! Please try again."
                    );
            }
        }
    }
}

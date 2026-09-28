import java.sql.*;
import java.util.Scanner;

public class LibraryManagement {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("     LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=================================");

        DBConnection.getConnection();

        while (true) {

            System.out.println("\n1. ADD BOOK");
            System.out.println("2. VIEW BOOKS");
            System.out.println("3. SEARCH BOOK");
            System.out.println("4. REGISTER STUDENT");
            System.out.println("5. BORROW BOOK");
            System.out.println("6. RETURN BOOK");
            System.out.println("7. VIEW BORROWED BOOKS");
            System.out.println("8. EXIT");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    viewBooks();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    registerStudent();
                    break;

                case 5:
                    borrowBook();
                    break;

                case 6:
                    returnBook();
                    break;

                case 7:
                    viewBorrowedBooks();
                    break;

                case 8:
                    System.out.println("Thank you!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // 1. ADD BOOK
    static void addBook() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter book title: ");
            String title = sc.nextLine();

            System.out.print("Enter author name: ");
            String author = sc.nextLine();

            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();

            String sql =
                "INSERT INTO book(title, author, quantity) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            System.out.println("Book added successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. VIEW BOOKS
    static void viewBooks() {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM book";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n---------------------------------------------");
            System.out.println("ID\tTITLE\t\tAUTHOR\t\tQUANTITY");
            System.out.println("---------------------------------------------");

            while (rs.next()) {

                System.out.println(
                    rs.getInt("book_id") + "\t" +
                    rs.getString("title") + "\t\t" +
                    rs.getString("author") + "\t\t" +
                    rs.getInt("quantity")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. SEARCH BOOK
    static void searchBook() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter book title to search: ");
            String title = sc.nextLine();

            String sql =
                "SELECT * FROM book WHERE title LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + title + "%");

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("\nBook ID: " +
                    rs.getInt("book_id"));

                System.out.println("Title: " +
                    rs.getString("title"));

                System.out.println("Author: " +
                    rs.getString("author"));

                System.out.println("Quantity: " +
                    rs.getInt("quantity"));
            }

            if (!found) {
                System.out.println("Book not found!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. REGISTER STUDENT
    static void registerStudent() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            System.out.print("Enter student email: ");
            String email = sc.nextLine();

            String sql =
                "INSERT INTO student(name, email) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);

            ps.executeUpdate();

            System.out.println("Student registered successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. BORROW BOOK
    static void borrowBook() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter student ID: ");
            int studentId = sc.nextInt();

            // Check book quantity
            String checkBook =
                "SELECT quantity FROM book WHERE book_id = ?";

            PreparedStatement ps1 =
                con.prepareStatement(checkBook);

            ps1.setInt(1, bookId);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {

                System.out.println("Book not found!");
                con.close();
                return;
            }

            int quantity = rs.getInt("quantity");

            if (quantity <= 0) {

                System.out.println("Book is not available!");
                con.close();
                return;
            }

            // Check student
            String checkStudent =
                "SELECT * FROM student WHERE student_id = ?";

            PreparedStatement ps2 =
                con.prepareStatement(checkStudent);

            ps2.setInt(1, studentId);

            ResultSet rs2 = ps2.executeQuery();

            if (!rs2.next()) {

                System.out.println("Student not found!");
                con.close();
                return;
            }

            // Insert borrow record
            String insertBorrow =
                "INSERT INTO borrow(book_id, student_id, borrow_date, status) " +
                "VALUES (?, ?, CURDATE(), 'BORROWED')";

            PreparedStatement ps3 =
                con.prepareStatement(insertBorrow);

            ps3.setInt(1, bookId);
            ps3.setInt(2, studentId);

            ps3.executeUpdate();

            // Decrease book quantity
            String updateBook =
                "UPDATE book SET quantity = quantity - 1 WHERE book_id = ?";

            PreparedStatement ps4 =
                con.prepareStatement(updateBook);

            ps4.setInt(1, bookId);

            ps4.executeUpdate();

            System.out.println("Book borrowed successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. RETURN BOOK
    static void returnBook() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter borrow ID: ");
            int borrowId = sc.nextInt();

            // Get book ID
            String sql =
                "SELECT book_id FROM borrow " +
                "WHERE borrow_id = ? AND status = 'BORROWED'";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, borrowId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println("Borrow record not found!");
                con.close();
                return;
            }

            int bookId = rs.getInt("book_id");

            // Update borrow table
            String updateBorrow =
                "UPDATE borrow SET return_date = CURDATE(), " +
                "status = 'RETURNED' WHERE borrow_id = ?";

            PreparedStatement ps2 =
                con.prepareStatement(updateBorrow);

            ps2.setInt(1, borrowId);

            ps2.executeUpdate();

            // Increase book quantity
            String updateBook =
                "UPDATE book SET quantity = quantity + 1 " +
                "WHERE book_id = ?";

            PreparedStatement ps3 =
                con.prepareStatement(updateBook);

            ps3.setInt(1, bookId);

            ps3.executeUpdate();

            System.out.println("Book returned successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 7. VIEW BORROWED BOOKS
    static void viewBorrowedBooks() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT b.borrow_id, " +
                "book.title, " +
                "student.name, " +
                "b.borrow_date, " +
                "b.return_date, " +
                "b.status " +
                "FROM borrow b " +
                "JOIN book ON b.book_id = book.book_id " +
                "JOIN student ON b.student_id = student.student_id";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n------------------------------------------------------------");
            System.out.println(
                "BORROW ID\tBOOK\t\tSTUDENT\t\tDATE\t\tSTATUS"
            );
            System.out.println("------------------------------------------------------------");

            while (rs.next()) {

                System.out.println(
                    rs.getInt("borrow_id") + "\t\t" +
                    rs.getString("title") + "\t\t" +
                    rs.getString("name") + "\t\t" +
                    rs.getDate("borrow_date") + "\t" +
                    rs.getString("status")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection{
    static String url="jdbc:mysql://localhost:3306/studentdb";
    static String username="root";
    static String password="root";

    public static Connection getConnection(){
        Connection con=null;
        try{
            con=DriverManager.getConnection(url,username,password);
            System.out.println("Database Connected Successfully");

        }catch(Exception e){
            e.printStackTrace();
        }
        return con;

    }
}
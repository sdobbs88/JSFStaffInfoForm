/**
 * Class: CSCI5520U Rapid Java Development
 * Instructor: Y. Daniel Liang
 * Description: Java Server Faces form that interacts with a database.
 * Allows the user to insert, update and view staff information using
 * the staff ID number as the primary key
 * Due: 03/06/2017
 *
 * @author Shaun C. Dobbs
 * @version 1.0
 *
 * I pledge by honor that I have completed the programming assignment
 * independently. I have not copied the code from a student or any source. I
 * have not given my code to any student.  *
 * Sign here: Shaun C. Dobbs
 */
package chapter39;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.*;

/**
 *
 * @author Shaun
 */
@Named(value = "staffBean")
@ApplicationScoped
public class StaffJSFBean {

    private PreparedStatement pstmt;

    private String id;
    private String lastName;
    private String firstName;
    private String mi;
    private String address;
    private String city;
    private String state;
    private String telephone;
    private String response = "";

    public void insert() {
        initializeJdbc();
        try {
            storeEmployee(id, lastName, firstName, mi, address, city, state, telephone);
            response = (firstName + " " + lastName + " is now registered in the database");

        } catch (SQLException ex) {
            response = ("Error: " + ex.getMessage());
        }
    }

    public void view() {
        
        initializeJdbc();
        try {
            String query = "select * from staffinfo where id=" + id + ";";
            ResultSet rs = pstmt.executeQuery(query);

            if (!rs.next()) {
                response = ("Error: ID not in database");
            } else {
                rs = pstmt.executeQuery(query);
                while (rs.next()) {
                    int IDcol = rs.getInt("id");
                    id = Integer.toString(IDcol);
                    lastName = rs.getString("lastName");
                    firstName = rs.getString("firstName");
                    mi = rs.getString("mi");
                    address = rs.getString("address");
                    city = rs.getString("city");
                    state = rs.getString("state");
                    telephone = rs.getString("telephone");
                }
                response = ("View Successful");
            }
        } catch (SQLException ex) {
            response = ("Error: " + ex.getMessage());
        }
    }

    public void update() throws SQLException {
        initializeJdbc();
        String query = "select * from staffinfo where id=" + id + ";";
        ResultSet rs = pstmt.executeQuery(query);

        if (!rs.next()) {
            response = ("Error: ID not in database");
        } else {

            try {
                //Loading the driver
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver loaded");
                //Start connection
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabook", "scott", "tiger");
                System.out.println("Database Connected");
                //Statement to delete the tuple before it is updated
                pstmt = conn.prepareStatement("delete from staffinfo where id = " + id + ";");
                pstmt.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                response = ("Error: " + ex);
            }
            try {
                initializeJdbc();
                storeEmployee(id, lastName, firstName, mi, address, city, state, telephone);
                response = (firstName + " " + lastName + " has been updated in the database");
            } catch (SQLException ex) {
                response = ("Error: " + ex.getMessage());
            }
        }
    }

    public void test() {
        initializeJdbc();
        System.out.println("Test");
        System.out.println(id);
        System.out.println(lastName);
        System.out.println(firstName);
        System.out.println(mi);
        System.out.println(address);
        System.out.println(city);
        System.out.println(state);
        System.out.println(telephone);
    }

    public void clear() {
        id = "";
        lastName = "";
        firstName = "";
        mi = "";
        address = "";
        city = "";
        state = "";
        telephone = "";
        response = "";
    }

    public String getid() {
        return id;
    }

    public void setid(String ID) {
        this.id = ID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getmi() {
        return mi;
    }

    public void setmi(String mi) {
        this.mi = mi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public StaffJSFBean() {
    }

    private void initializeJdbc() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            // Establish a connection

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabook", "scott", "tiger");
            System.out.println("Database connected");
            // Create a Statement
            pstmt = conn.prepareStatement("insert into staffinfo "
                    + "(ID, lastName, firstName, mi, address, city, state, telephone) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?)");
        } catch (ClassNotFoundException | SQLException ex) {
        }
    }

    private void storeEmployee(String ID, String lastName,
            String firstName, String mi, String address, String city,
            String state, String telephone) throws SQLException {
        pstmt.setString(1, ID);
        pstmt.setString(2, lastName);
        pstmt.setString(3, firstName);
        pstmt.setString(4, mi);
        pstmt.setString(5, address);
        pstmt.setString(6, city);
        pstmt.setString(7, state);
        pstmt.setString(8, telephone);
        pstmt.executeUpdate();
    }
}

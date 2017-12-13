/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DA;

import Domain.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author BryanLee
 */
public class CustomerDA {

    private String host = "jdbc:derby://localhost:1527/FastestDeliveryMan";
    private String user = "assignment";
    private String password = "assignment";
    private String tableName = "CUSTOMER";
    private Connection conn;
    private PreparedStatement stmt;
    private String sqlQueryStr = "SELECT * from " + tableName;

    public CustomerDA() {
        createConnection();

    }

    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(host, user, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void shutDown() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public ResultSet selectPNumberAndAddress(String custID) {
        String queryStr = "SELECT PNUMBER, ADDRESS FROM " + tableName + " WHERE CUSTID = ?";
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, custID);

            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }

    public ArrayList<Customer> retrieveRecord(String pNumber) {
        createConnection();
        String queryStr = "SELECT * FROM " + tableName + " WHERE PNUMBER = ?";
        ResultSet rs = null;
        ArrayList<Customer> list = new ArrayList<Customer>();

        try {
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, pNumber);
            
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                list.add(customer);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return list;
    }
}

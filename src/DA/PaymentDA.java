/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DA;

import Domain.Payment;
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
public class PaymentDA {

    private String host = "jdbc:derby://localhost:1527/FastestDeliveryMan";
    private String user = "assignment";
    private String password = "assignment";
    private String tableName = "PAYMENT";
    private Connection conn;
    private PreparedStatement stmt;
    private String sqlQueryStr = "SELECT * from " + tableName;

    public PaymentDA() {
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

    public int generateReceipt() {
        ResultSet rs = null;
        int rcp = 2000;

        try {
            stmt = conn.prepareStatement(sqlQueryStr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                rcp = rs.getInt(1);
                rcp = rcp + 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return rcp;
    }

    public ResultSet selectNotDeliveredRecord() {
        String queryStr = "SELECT CUSTID FROM " + tableName + " WHERE STATUS = 'NOT DELIVERED'";
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(queryStr);

            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }

    public ResultSet selectRecord(int orderID) {
        String queryString = "SELECT * FROM " + tableName + " WHERE ORDERID = ?";

        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(queryString);
            stmt.setInt(1, orderID);

            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return rs;
    }

    public ArrayList<Payment> retrieveRecord(String custID) {
        createConnection();
        String queryStr = "SELECT * FROM " + tableName + " WHERE CUSTID = ?";
        ResultSet rs = null;
        ArrayList<Payment> paymentList = new ArrayList<Payment>();
        try {
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, custID);

            rs = stmt.executeQuery();

            Payment payment;

            while (rs.next()) {
                payment = new Payment(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6));
                paymentList.add(payment);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return paymentList;
    }

    public void updateDeliveredStatus(int orderID) {
        String updateStr = "UPDATE " + tableName + " SET STATUS = ? WHERE ORDERID = ?";

        try {
            ResultSet rs = selectRecord(orderID);

            if (rs.next()) {
                stmt = conn.prepareStatement(updateStr);
                stmt.setString(1, "DELIVERED");
                stmt.setInt(2, orderID);
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
}

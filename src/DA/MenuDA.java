/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DA;

import Domain.FoodOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Domain.Menu;

/**
 *
 * @author BryanLee
 */
public class MenuDA {

    private String host = "jdbc:derby://localhost:1527/FastestDeliveryMan";
    private String user = "assignment";
    private String password = "assignment";
    private String tableName = "MENU";
    private Connection conn;
    private PreparedStatement stmt;
    //Menu menu = new Menu();

    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(host, user, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ResultSet selectRecord(String selectedFoodName) {
        createConnection();        
        String queryStr = "SELECT * FROM " + tableName + " WHERE FOODNAME = ?";
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, selectedFoodName);

            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }

    public ArrayList<Menu> retrieveRecord() {
        createConnection();
        String queryStr = "SELECT * FROM " + tableName;
        ResultSet rs = null;
        ArrayList<Menu> menuList = new ArrayList<Menu>();
        try {
            stmt = conn.prepareStatement(queryStr);

            rs = stmt.executeQuery();

            Menu menu;

            while (rs.next()) {
                menu = new Menu(rs.getString(1), rs.getString(2), rs.getString(3), Double.parseDouble(rs.getString(4)));
                menuList.add(menu);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return menuList;
    }
}

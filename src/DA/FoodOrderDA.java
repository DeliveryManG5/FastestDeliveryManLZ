/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Domain.FoodOrder;

/**
 *
 * @author BryanLee
 */
public class FoodOrderDA {
    private String host = "jdbc:derby://localhost:1527/FastestDeliveryMan";
    private String user = "assignment";
    private String password = "assignment";
    private String tableName = "FOODORDER";
    private Connection conn;
    private PreparedStatement stmt;
    
    
    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(host, user, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteRecord(String foodName){
        String insertStr = "DELETE FROM " + tableName + " WHERE FOODNAME = ?";
        
        try{
            stmt = conn.prepareStatement(insertStr);
            stmt.setString(1, foodName);
            stmt.executeUpdate();
        }catch(SQLException ex){
            ex.getMessage();
        }
    }
    
    public ArrayList<FoodOrder> retrieveRecord(){
        createConnection();
        String queryStr = "SELECT * FROM " +  tableName;
        ResultSet rs = null;
        ArrayList<FoodOrder> orderList = new ArrayList<FoodOrder>();
        try{
            stmt = conn.prepareStatement(queryStr);
            
            rs = stmt.executeQuery();
            
            FoodOrder foodOrder;
            
            while(rs.next()){
                foodOrder = new FoodOrder(rs.getString(1),rs.getString(2), Double.parseDouble(rs.getString(3)), Integer.parseInt(rs.getString(4)), Double.parseDouble(rs.getString(5)));
                orderList.add(foodOrder);
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return orderList;
    }   
    
    public ResultSet selectFoodAllRecord(){
        String queryStr = "SELECT * FROM " + tableName;
        ResultSet rs = null;
        try{
            stmt = conn.prepareStatement(queryStr);
                        
            rs = stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }
    
    public int generateOrderID(){
        String queryStr = "SELECT MAX(ORDERID) FROM " + tableName;
        ResultSet rs = null;
        int orderID = 1000;
        try{
            ResultSet allRecord = selectFoodAllRecord();
            if(allRecord.next()){                
                stmt = conn.prepareStatement(queryStr);
                rs = stmt.executeQuery();
                if(rs.next()){
                    orderID = rs.getInt(1);
                    orderID++;    
                }
            }   
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return orderID;
    }
    
    public boolean verifyRecord(String id){
        createConnection();
        String queryStr = "SELECT * FROM " + tableName + " WHERE FOODNAME = ?";
        ResultSet rs = null;
        boolean yesno = false;
        try{
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, id);
            
            rs = stmt.executeQuery();
            
            if(rs.next()){
                yesno = true;
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return yesno;
    }
    
    public ResultSet selectRecord(String foodName){
        String queryStr = "SELECT * FROM " + tableName + " WHERE FOODNAME = ?";
        ResultSet rs = null;
        try{
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1,foodName);
            
            rs = stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }
    
    public void updateRecord(String foodName, int qty) {
        String updateStr = "UPDATE "+ tableName + " SET QUANTITY = ?, TOTALAMOUNT = ? WHERE FOODNAME = ?";
       
        try {
            ResultSet rs = selectRecord(foodName);
            if(rs.next()){
                int editQty = rs.getInt(4) + qty;
                double totalPrice = rs.getDouble(3) * editQty;
                stmt = conn.prepareStatement(updateStr);
                stmt.setInt(1, editQty);
                stmt.setDouble(2, totalPrice);
                stmt.setString(3, foodName);
                stmt.executeUpdate();
            }
            } catch (SQLException ex) {
            ex.getMessage();
        }
    }
     
    public void addRecord(String foodName, double price, int orderQuantity, double totalPrice){
        String insertStr = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?)";
        
        //int orderID = generateOrderID();
        
        try {
            if(verifyRecord(foodName)){
                updateRecord(foodName, orderQuantity);
            }
            else{
                stmt = conn.prepareStatement(insertStr);
                stmt.setInt(1, 1001);
                stmt.setString(2, foodName);
                stmt.setDouble(3,price);
                stmt.setInt(4,orderQuantity);
                stmt.setDouble(5,totalPrice);

                stmt.executeUpdate(); 
            }
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}

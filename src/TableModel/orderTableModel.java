package TableModel;


import ADT.List;
import Interface.ListInterface;
import Domain.FoodOrder;
import javax.swing.table.AbstractTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author BryanLee
 */
public class orderTableModel extends AbstractTableModel {

    private ListInterface<FoodOrder> contents = new List<>();
    int row;
    String[] columns = {"Food Name", "Price", "Quantity", "Total Price"};

    public orderTableModel(ListInterface<FoodOrder> contents){
        this.contents = contents;
    
    }

    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public int getRowCount() {
        return this.contents.getNumberOfEntries();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    public void setRowCount(int row){
        this.row = row;
    }

    @Override
    public Object getValueAt(int row, int column){
        String foodname = null;
        double price = 0, totalPrice = 0;
        int qty = 0;
        row++;
        //while(row <= numOfEntries ){
            FoodOrder foodorder = (FoodOrder) contents.get(row);
             foodname = foodorder.getFoodName();
             price = foodorder.getFoodPrice();
             qty = foodorder.getQty();
             totalPrice = foodorder.getTotalPrice();
             if(column == 0){
                 return foodname;
             }
             else if(column == 1){
                 return price;
             }
             else if(column == 2){
                 return qty;
             }
             else{
                 return totalPrice;
             }
          //  row++;
//            return (column == 0)? foodname:price:qty:totalPrice;
        //}
        
        //map column index to property
        //return (column == 0)? deliveryman.getDeliveryManID() : deliveryman.getDeliveryManName();
        
    }

}

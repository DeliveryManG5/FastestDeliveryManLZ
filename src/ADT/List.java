package ADT;

import Assignment.Cart;
import Assignment.Restaurant;
import Interface.ListInterface;
import Domain.FoodOrder;
import Assignment.Restaurant2Menu;
import Assignment.Sprint2;
import Domain.Customer;
import Domain.Payment;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author BryanLee
 */
public class List<T> implements ListInterface<T> {

    private Node firstNode, lastNode; // reference to first node
    private T[] list;
    private int numberOfEntries;  	// number of entries in list

    @Override
    public String generateOrderID() {
        String orderID = null;
        if (isEmpty()) {
            orderID = "O1001";
        } else {
            Payment payment = (Payment) this.getLast();
            String lastOrderID = payment.getOrderID();

            orderID = "O" + (Integer.parseInt(lastOrderID.substring(1, lastOrderID.length())) + 1);
        }
        return orderID;
    }

    @Override
    public String generateReceiptNo() {
        String receiptNo = null;

        if (isEmpty()) {
            return receiptNo = "R1001";
        } else {
            Payment payment = (Payment) this.getLast();
            String lastReceiptNo = payment.getReceiptNo();

            receiptNo = "R" + (Integer.parseInt(lastReceiptNo.substring(1, lastReceiptNo.length())) + 1);

            return receiptNo;
        }
    }

    @Override
    public T getPaymentLast() {
        T result = null;

        if (Cart.paymentList.isEmpty()) // if empty list
        {
            result = null;
        } else {
            Node currentNode = firstNode;	// traverse linked list with p pointing to the current node

            Payment payment = (Payment) currentNode.data;
            while (currentNode.next != null) {	// while have not reached the last node
                currentNode = currentNode.next;
            }
            result = currentNode.data;// currentNode is pointing to the last node
        }

        return result;
    }

    @Override
    public T getLast() {
        T result = null;

        if (isEmpty()) // if empty list
        {
            result = null;
        } else {
            Node currentNode = firstNode;	// traverse linked list with p pointing to the current node
            while (currentNode.next != null) {	// while have not reached the last node
                currentNode = currentNode.next;
            }
            result = currentNode.data;// currentNode is pointing to the last node
        }

        return result;
    }

    @Override
    public ListInterface getOrder() {
        ListInterface<FoodOrder> orderList = new List<>();

        if (!Restaurant2Menu.orderFoodList.isEmpty()) {
            Node currentNode = firstNode;
            for (int i = 0; i < numberOfEntries; i++) {
                FoodOrder foodorder = (FoodOrder) currentNode.data;
                orderList.add(foodorder);
                currentNode = currentNode.next;
            }
        }
        return orderList;
    }

    @Override
    public double getSubTotal() {
        double subtotal = 0.0;
        if (!Restaurant2Menu.orderFoodList.isEmpty()) {
            Node currentNode = firstNode;
            for (int i = 0; i < numberOfEntries; i++) {
                FoodOrder foodorder = (FoodOrder) currentNode.data;
                subtotal += foodorder.getTotalPrice();
                currentNode = currentNode.next;
            }
        }

        return subtotal;
    }

    @Override
    public boolean remove(T anEntry) {
        if (!isEmpty()) {
            if (firstNode.data.equals(anEntry)) {
                firstNode = firstNode.next;
                if (firstNode == null) {
                    lastNode = null;
                } else {
                    firstNode.previous = null;
                }
                numberOfEntries--;
                return true;
            }

            Node temp = firstNode;
            while (temp != null && !temp.data.equals(anEntry)) {
                temp = temp.next;
            }

            if (temp != null && temp.data.equals(anEntry)) {
                if (temp == lastNode) {
                    lastNode = temp.previous;
                    lastNode.next = null;
                } else {
                    temp.previous.next = temp.next;
                    temp.next.previous = temp.previous;
                }
                numberOfEntries--;
                return true;
            }

        }
        return false;
    }

    public T get(int givenPosition) // returns the element at the specified position in the list.
    {
        T result = null;

        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            Node currentNode = firstNode;
            for (int i = 0; i < givenPosition - 1; ++i) {
                currentNode = currentNode.next;		// advance currentNode to next node
            }
            result = currentNode.data;	// currentNode is pointing to the node at givenPosition
        }

        return result;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        boolean isSuccessful = true;

        if ((newPosition >= 1) && (newPosition <= numberOfEntries + 1)) {
            Node newNode = new Node(newEntry);

            if (isEmpty() || (newPosition == 1)) {     // case 1: add to beginning of list
                newNode.next = firstNode;
                firstNode = newNode;
            } else {								                      // case 2: list is not empty and newPosition > 1
                Node nodeBefore = firstNode;
                for (int i = 1; i < newPosition - 1; ++i) {
                    nodeBefore = nodeBefore.next;		// advance nodeBefore to its next node
                }

                newNode.next = nodeBefore.next;	// make new node point to current node at newPosition
                nodeBefore.next = newNode;		// make the node before point to the new node
            }

            numberOfEntries++;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    @Override
    public boolean add(T newEntry) {
        Node newNode = new Node(newEntry);	// create the new node

        if (isEmpty()) // if empty list
        {
            firstNode = newNode;
        } else {                        // add to end of nonempty list
            Node currentNode = firstNode;					// traverse linked list with p pointing to the current node
            while (currentNode.next != null) {		// while have not reached the last node
                currentNode = currentNode.next;
            }
            currentNode.next = newNode; // make last node reference new node
        }

        numberOfEntries++;
        return true;
    }

//    @Override
//    public T remove(int givenPosition) {
//        T result = null;
//
//        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
//            result = list[givenPosition - 1];
//
//            if (givenPosition < numberOfEntries) {
//                removeGap(givenPosition);
//            }
//
//            numberOfEntries--;
//        }
//
//        return result;
//    }
    
    private void removeGap(int givenPosition) {
        // move each entry to next lower position starting at entry after the
        // one removed and continuing until end of list
        int removedIndex = givenPosition - 1;
        int lastIndex = numberOfEntries - 1;

        for (int index = removedIndex; index < lastIndex; index++) {
            list[index] = list[index + 1];
        }
    }

    @Override
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        boolean isSuccessful = true;

        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            Node currentNode = firstNode;
            for (int i = 0; i < givenPosition - 1; ++i) {
                // System.out.println("Trace| currentNode.data = " + currentNode.data + "\t, i = " + i);
                currentNode = currentNode.next;		// advance currentNode to next node
            }
            currentNode.data = newEntry;	// currentNode is pointing to the node at givenPosition
        } else {
            isSuccessful = false;
        }

        return isSuccessful;
    }

    @Override
    public T getEntry(int givenPosition) {
        T result = null;

        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            Node currentNode = firstNode;
            for (int i = 0; i < givenPosition; ++i) {
                currentNode = currentNode.next;		// advance currentNode to next node
            }
            result = currentNode.data;	// currentNode is pointing to the node at givenPosition
        }

        return result;
    }

    @Override
    public boolean contains(T anEntry) {
        boolean found = false;
        Node currentNode = firstNode;

        while (!found && (currentNode != null)) {
            if (anEntry.equals(currentNode.data)) {
                found = true;
            } else {
                currentNode = currentNode.next;
            }
        }

        return found;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return firstNode == null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public String toString() {
        String outputStr = "";
        Node currentNode = firstNode;
        while (currentNode != null) {
            outputStr += currentNode.data + "\n";
            currentNode = currentNode.next;
        }
        return outputStr;
    }

    @Override
    public boolean searchPhoneNumber(String phoneNumber) {
        //T result = null;
        boolean found = false;
        Node currentNode = firstNode;
        for (int i = 0; i < numberOfEntries; i++) {
            Customer customer = (Customer) currentNode.data;
            String pNumber = customer.getpNumber();
            if (pNumber.equals(phoneNumber)) {
                found = true;
                //result = currentNode.data;
            } else {
                currentNode = currentNode.next;
            }
        }

        return found;
    }

    @Override
    public int getPosition(String pNumber) {
        int position = 0;
        Node currentNode = firstNode;
        for (int i = 1; i <= numberOfEntries; i++) {

            Customer customer = (Customer) currentNode.data;
            String phoneNumber = customer.getpNumber();
            if (phoneNumber.equals(pNumber)) {
                position = i;
                break;
            } else {
                currentNode = currentNode.next;
            }
        }

        return position;
    }

    @Override
    public T getRecord(String pNumber) {
        T result = null;

        if (Restaurant.customerList.isEmpty()) {
            result = null;
        } else {
            Node currentNode = firstNode;
            for (int i = 0; i < numberOfEntries; i++) {

                Customer customer = (Customer) currentNode.data;

                String phoneNumber = customer.getpNumber();

                if (phoneNumber.equals(pNumber)) {
                    result = currentNode.data;
                } else {
                    currentNode = currentNode.next;
                }
            }
        }

        return result;
    }

    private class Node {

        private T data;
        private Node next, previous;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }

        private Node(T data, Node next, Node previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }
    }

}

/*Ploypailin Nolastname   6413106
Tanawat Kanchan         6413215
Natedee Muenkrut        6413220*/
package Project1_220;

import java.util.*;
import java.io.*;

class Menu{
    private String name;
    private int price;
    private int totalDishes =0;
    
    public Menu(String name,int price){
        this.name = name;
        this.price = price;
    }
    private void UpdateDish(int dish_quantity){//'qty' stands for 'quantity'
        totalDishes += dish_quantity;
    }
    public void Summarize(){
        //print "Menu summary" sorted by price, total delivery, alphabet
        //ploy will do this method
        //System.out.printf("%s  %d  %d\n", name, price, totalDishes);
    }
    public int CalculateOrderBill(int dish_quantity){
        UpdateDish(dish_quantity);
        return price*dish_quantity;
    }
};

class Customer{
    private String name;
    private int points=0;
    
    public Customer(String name, int orderbill){
        this.name = name;
        updatePoints(orderbill);
    }
    public void updatePoints(int orderbill){
       points += (int)(orderbill*0.1);
    }
    public void Summarize(){
        //Show all customers sorted by left point
        //ploy will do this method
        //System.out.printf("%s  %d\n", name, points);
    }
    public boolean Equal(Customer other){
        if(this.name.equals(other.name)) return true;
        else return false;
    }
};

class Order{
    private Customer Ref;
    private int ID;
    private int[] dish_quantity = new int[5];
    private int orderbill = 0, finalbill = 0;
    
    public Order(int id, int dish_qty[], Customer temp, int orderbill){
        ID = id;
        Ref = temp;
        for(int i=0; i<5; i++){
            dish_quantity[i] = dish_qty[i];
        }
        //calculate orderbill and finalbill, ploy will do it
    }
    public void Summarize(){
        //Show all customers sorted by left point
        //ploy will do this method
        /*System.out.printf("%d  %d  %d", ID, orderbill, finalbill);
        for(int i=0; i<5; i++){
            System.out.print("  "+dish_quantity[i]);
        }
        Ref.Summarize();*/
    }
    
    private void CalculateBills(String n, int odr_qty[]){
        
    }
};

class MyInputReader{
    private String path, menufile, orderfile;
    boolean opensuccess = false;
    Scanner sc;//remove or not? cuz we use it just in openfile method.
    
    //Constructor
    public MyInputReader(String p, String menu, String order){   
        path = p;
        menufile = menu;
        orderfile = order;
        sc = new Scanner(System.in);
    }
     
    public void openfile(Menu menu[], ArrayList<Customer> customer_list, ArrayList<Order> order_list){
        String filename = menufile;
        for(int k=0; k<2; k++){
            opensuccess = false;
            while(!opensuccess){
                try(
                    Scanner readfile = new Scanner(new File(path + filename));
                ){
                    opensuccess = true;
                    for(int i=0; i<5&&k==0; i++){
                        String line = readfile.nextLine();
                        String[] buf = line.split(",");
                        menu[i] = new Menu(buf[0].trim(),Integer.parseInt(buf[1].trim()));
                        //menu[i].Summarize();
                    }
                    while(readfile.hasNext()){
                        processLine(readfile.nextLine(),menu,customer_list,order_list);
                    }
                }
                catch(FileNotFoundException e){
                    System.out.println(e);
                    System.out.print("New file name : ");
                    filename = sc.nextLine();
                    switch(k){
                        case 0: menufile  = filename; break;
                        case 1: orderfile = filename;
                    }
                }
            }
            filename = orderfile;
        }
    }
    
    //get data from openfile method, have line and arraylist in param
    private void processLine(String line, Menu menu[], ArrayList<Customer> customer_list, ArrayList<Order> order_list){
        String[] buf = line.split(",");
        int orderbill=0;
        int[] dish_qty={0,0,0,0,0};
        boolean hasError=false;
        /*for(int i = 0 ; i < buf.length; i++){
            buf[i] = buf[i].trim();
        }*/
        
        for(int i=2; i<buf.length && i<7; i++){//calculate summary point and keep dish quantity in num[]
            try{
                dish_qty[i-2] = Integer.parseInt(buf[i].trim());
                if(dish_qty[i-2] < 0) throw new Exception("for negative value \""+ dish_qty[i-2] + "\"");//ArithmeticException
            }/*catch(ArithmeticException e){
                hasError=true;
                num[i-2] = 0;
            }*/catch(Exception e){
                System.err.println(e);
                hasError=true;
                dish_qty[i-2] = 0;
            }
            finally{
                orderbill += menu[i-2].CalculateOrderBill(dish_qty[i-2]);
                //menu[i-2].UpdateDish(dish_qty[i-2]);
            }
        }
        if(hasError){
            System.out.print("Original [" + line + "]  ->  Correction  [" + buf[0] +","+ buf[1]);
            for(int i=0; i<5; i++) System.out.print(",  "+ dish_qty[i]);
            System.out.print("]\n\n");
        }
        
        //create an Customer obj or update points to the exist obj
        Customer C_temp = new Customer(buf[1].trim(),orderbill);
        int index=-1;
        index = check(customer_list,C_temp);
        if(index!=-1){
            customer_list.get(index).updatePoints(orderbill);
            C_temp = customer_list.get(index);
        }
        else customer_list.add(C_temp);
        
        Order O_temp = new Order(Integer.parseInt(buf[0].trim()), dish_qty, C_temp, orderbill);
        order_list.add(O_temp);
        
        order_list.get(order_list.size()-1).Summarize();
    }
    
    private int check(ArrayList<Customer> customer, Customer temp){
        for(int i=0; i<customer.size(); i++){
            if(customer.get(i).Equal(temp)) return i;
        }
        return -1;
    }
}

/*class MyException extends Exception{
    public MyException(String message){ super(message); }
};*/

public class FoodDelivery {

    public static void main(String[] args) {
        String path = "src/main/java/Project1_220/";
	String [] filename = {"menus.txt","orders.txt","orders_errors.txt"};
        Menu[] menu = new Menu[5];
        ArrayList<Customer> customer_list = new ArrayList<>();
        ArrayList<Order> order_list = new ArrayList<>();
        
        MyInputReader food_delivery = new MyInputReader(path,filename[0],filename[1]);
        food_delivery.openfile(menu,customer_list,order_list);
        
        
    }
}

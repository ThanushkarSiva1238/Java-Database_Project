package LO1_Project;

import java.sql.*;
import java.util.Scanner;

public class Project {
    public static void main(String[] args) throws SQLException {

//         Database Connectivity....
        String url = "jdbc:mysql://localhost:3306/sampath";
        String us = "root";
        String password = "";

        Connection con = DriverManager.getConnection(url, us, password);
        Statement st = con.createStatement();


//        User Input Scanner
        Scanner input = new Scanner(System.in);

        char exit = 'n';
        while (exit =='n'){
            System.out.println("\nSampath Food City (PVT) Ltd - Online System\n*******************************************");
            System.out.println("1. Admin \n2. Customer");
            System.out.print("Choose your User-type : ");
            String user = input.next().toLowerCase();

//            User-Type Validation......
            if (user.equals("admin") || user.equals("1")){
//                Admin_ID : Admin
//                Password : Ad_Pass
                admin(con, st, input);

            } else if (user.equals("customer") || user.equals("2")) {
                customer(st, input);
            } else {
                System.out.println("Invalid User Type");
            }

            System.out.print("Do you want exit (Y/N) : ");
            exit = input.next().toLowerCase().charAt(0);
        }

        input.close();
        st.close();
        con.close();
        System.out.println("\n>>>>> Thank you <<<<<");
    }

    public static void admin(Connection con, Statement st, Scanner input) throws SQLException {

//        Admin Login Interface
        int count = 1;

        while (count <= 3){
            String Admin_ID, Password;
            System.out.print("\nEnter Admin_ID : ");
            Admin_ID = input.next();
            System.out.print("Enter Password : ");
            Password = input.next();

            String Ad = "SELECT * FROM login WHERE User_ID ='"+Admin_ID+"' AND Password ='"+Password+"'";
            ResultSet rs = st.executeQuery(Ad);
            rs.next();

            if (rs.getRow() == 1){

                char menu = 'y';

                while (menu == 'y'){

                    System.out.println("\n>>>>> Product List <<<<<");
                    //                        Select SQL Contidion
                    String SelectQuery = "SELECT * FROM product";
                    ResultSet rsSelect = st.executeQuery(SelectQuery);

                    while (rsSelect.next()){
                        System.out.printf("%s::%s (%d)\n\tPrice : Rs.%.2f\n",rsSelect.getString("Product_ID"), rsSelect.getString("Product_Name"), rsSelect.getInt("Quantity"), rsSelect.getFloat("Price"));
                    }

                    System.out.println("\n1. Add Product \n2. Modify Product \n3. Remove Product \n4. View Customer Order");
                    System.out.print("Select the step (No): ");
                    int Selection = input.nextInt();

                    if (Selection == 1){

                        char add = 'y';

                        while (add == 'y'){
                            System.out.println("\n>>>>> Add Product <<<<<");

                            System.out.print("Enter Product_ID : ");
                            String Product_ID = input.next();

                            String ProID = "SELECT * FROM product WHERE Product_ID = '"+Product_ID+"'";
                            ResultSet proId= st.executeQuery(ProID);
                            proId.next();

                            if (proId.getRow() == 0){
                                System.out.print("Enter Product Name : ");
                                String Product_Name = input.next();
                                System.out.print("Enter Quantity : ");
                                int Quantity = input.nextInt();
                                System.out.print("Enter Price : ");
                                float Price = input.nextFloat();

//                            SQL Query Condition
                                String InsertQuery = "INSERT INTO product VALUES ('"+Product_ID+"', '"+Product_Name+"', "+Quantity+", "+Price+")";
                                st.executeUpdate(InsertQuery);

                                System.out.println("\nProduct Added Successfully...");
                            }else {
                                System.out.println("Product ID already existed...");
                            }

                            System.out.print("\nAdd more product (Y/N) : ");
                            add = input.next().toLowerCase().charAt(0);
                        }

                    }
                    else if (Selection == 2) {
                        System.out.println("\n>>>>> Modify Product <<<<<");

                        System.out.print("Enter Product_ID : ");
                        String Product_ID = input.next();

//                        Validation SQL Query
                        String ID_Check = "SELECT * FROM product WHERE Product_ID = '"+Product_ID+"'";
                        ResultSet rsModify = st.executeQuery(ID_Check);
                        rsModify.next();

                        if (rsModify.getRow() > 0){

                            System.out.println("\n1. Modify Product Name \n2. Modify Quantity \n3. Modify Price");
                            System.out.print("Choose one (No): ");
                            int option = input.nextInt();

                            if (option == 1){
                                System.out.print("\nProduct Name : ");
                                String name = input.next();

//                                Update SQL Condition (Product Name)
                                String updateName = "UPDATE product SET Product_Name = '"+name+"' where Product_Id = '"+Product_ID+"' ";
                                st.executeUpdate(updateName);
                                System.out.println("Product Name Updated Successfully...\n");

                            } else if (option == 2) {
                                System.out.print("\nQuantity : ");
                                int quantity = input.nextInt();

//                                Update SQL Condition (Product Quantity)
                                String updateQuantity = "UPDATE product SET Quantity = "+quantity+" where Product_Id = '"+Product_ID+"' ";
                                st.executeUpdate(updateQuantity);
                                System.out.println("Product Quantity Updated Successfully...\n");

                            } else if (option == 3) {
                                System.out.print("\nPrice : ");
                                float price = input.nextFloat();

//                                Update SQL Condition (Product Name)
                                String updatePrice = "UPDATE product SET Price = "+price+" where Product_Id = '"+Product_ID+"' ";
                                st.executeUpdate(updatePrice);
                                System.out.println("Product Price Updated Successfully...\n");

                            }else {
                                System.out.println("Invalid Input\n");
                            }

                        }else {
                            System.out.println("\nProduct Id Mismatch\n");
                        }
                    }
                    else if (Selection == 3) {
                        System.out.println("\n>>>>> Remove Product <<<<<");

                        System.out.print("Enter Product_ID : ");
                        String Product_ID = input.next();

//                        Validation SQL Query
                        String ID_Check = "SELECT * FROM product WHERE Product_ID = '" + Product_ID + "'";
                        ResultSet rsRemove = st.executeQuery(ID_Check);
                        rsRemove.next();

                        if (rsRemove.getRow() > 0) {
                            String RemoveQuery = "DELETE FROM product WHERE Product_ID = '" + Product_ID + "'";
                            st.executeUpdate(RemoveQuery);
                            System.out.println("Product Details Removed Successfully\n");
                        } else {
                            System.out.println("\nInvalid Product Id");
                        }
                        rsRemove.close();
                    }
                    else if (Selection == 4) {
                        System.out.println("\n>>>>> View Customer Order <<<<<\n");

                        String OrderQuery = "SELECT SUM(Payment) as 'Payment', Customer_Name FROM order_product GROUP BY Customer_Name";
                        ResultSet order = st.executeQuery(OrderQuery);

                        while (order.next()){
                            String CusName = order.getString("Customer_Name");
                            System.out.printf("> %s : Rs.%.2f\n", CusName, order.getFloat("Payment"));

                            Statement stmt = con.createStatement();
                            String Order = "SELECT * FROM order_product WHERE Customer_Name = '"+CusName+"'";
                            ResultSet or = stmt.executeQuery(Order);

                            while(or.next()){
                                System.out.printf("\t# %s (%d) = Rs.%.2f\n", or.getString("Order_ProductName"), or.getInt("Quantity"), or.getFloat("Payment"));
                            }
                            stmt.close();
                            System.out.println(" ");
                        }

                    }

                    System.out.print("Do you want to go Main-menu (Y/N): ");
                    menu = input.next().toLowerCase().charAt(0);
                }
                break;
            }else {
                System.out.println("Admin_ID or Password Invalid\n");
            }
            count ++;
        }
    }

    public static void customer(Statement st, Scanner input) throws SQLException {
        System.out.println("\n>>>>> Customer Login / Register Interface <<<<< \n***********************************************");
        System.out.println("1. Register \n2. Login");
        System.out.print("Select Option : ");
        int Interface = input.nextInt();

        if (Interface == 1){
            System.out.println("\n----- Register -----");

            String Customer_ID, Password;
            System.out.print("Enter Customer_ID : ");
            Customer_ID = input.next();

            String cus = "SELECT * FROM login WHERE User_ID = '"+Customer_ID+"'";
            ResultSet rs = st.executeQuery(cus);
            rs.next();

            if (rs.getRow() == 0) {
                System.out.print("Enter Password : ");
                Password = input.next();

//            Register SQL Query
                String Register = "INSERT INTO login VALUES ('"+Customer_ID+"', '"+Password+"', '2')";
                st.executeUpdate(Register);
                System.out.println("Customer_ID and Password Registered Successfully...\n");
            }else {
                System.out.println("Customer ID already existed...\n");
            }

        } else if (Interface == 2) {
            System.out.println("\n----- Login -----");
            CusLogin(st, input);
        }
    }

    public static void CusLogin(Statement st, Scanner input) throws SQLException {
        //        Customer Login Interface
        int count = 1;

        while (count <= 3){
            String Customer_ID, Password;
            System.out.print("Enter Customer_ID : ");
            Customer_ID = input.next();
            System.out.print("Enter Password : ");
            Password = input.next();

            String Cs = "SELECT * FROM login WHERE User_ID ='"+Customer_ID+"' AND Password ='"+Password+"'";
            ResultSet rs = st.executeQuery(Cs);
            rs.next();

            if (rs.getRow() == 1){
                String UserID = rs.getString("User_ID");

                char menu = 'y';

                while (menu == 'y') {
                    System.out.println("\n1. View Product List \n2. Order Product \n3. View the Total Amount");
                    System.out.print("Select the step (No): ");
                    int Selection = input.nextInt();

                    if (Selection == 1) {
                        System.out.println("\n>>>>> View Product List <<<<<\n");

//                        Select SQL Contidion
                        String SelectQuery = "SELECT * FROM product";
                        ResultSet rsSelect = st.executeQuery(SelectQuery);

                        while (rsSelect.next()){
                            System.out.printf("%s::%s (%d)\n\tPrice : %.2f\n",rsSelect.getString("Product_ID"), rsSelect.getString("Product_Name"), rsSelect.getInt("Quantity"), rsSelect.getFloat("Price"));
                        }
                    }
                    else if (Selection == 2) {
                        System.out.println("\n>>>>> Order the Product <<<<<\n");

                        char add = 'y';

                        while (add == 'y'){
                            System.out.print("Product ID : ");
                            String Product_ID = input.next();
                            System.out.print("Quantity : ");
                            int Quantity = input.nextInt();

                            String SelectQuery = "SELECT * FROM product WHERE Product_ID = '"+Product_ID+"'";
                            ResultSet rsSelect = st.executeQuery(SelectQuery);
                            rsSelect.next();

                            String Pro_ID = rsSelect.getString("Product_ID");
                            String Pro_Name = rsSelect.getString("Product_Name");
                            int quantity = rsSelect.getInt("Quantity");
                            float amount = rsSelect.getFloat("Price");

                            if (quantity > 5){
                                if (quantity > Quantity) {

//                                    Insert SQL Condition
                                    float Payment = Quantity * amount;
                                    String InsertQuery = "INSERT INTO order_product SET Order_ProductName = '"+Pro_Name+"', Quantity ="+Quantity+" , Payment = "+Payment+",Product_ID ='"+Pro_ID+"', Customer_Name = '"+UserID+"'";
                                    st.executeUpdate(InsertQuery);

//                                    Update SQL Condition
                                    int no = quantity - Quantity;
                                    String UpdateQuery = "UPDATE product SET Quantity = "+no+"  WHERE Product_ID = '"+Product_ID+"'";
                                    st.executeUpdate(UpdateQuery);

                                    System.out.println("Product added in Card");

                                }

                            } else {
                                if (quantity == 0) {
                                    System.out.println("Out of Stock");
                                }
                                else {
                                    System.out.println("Low Stock");
                                }
                            }

                            System.out.print("\nAdd more Order (Y/N) : ");
                            add = input.next().toLowerCase().charAt(0);
                        }

                    } else if (Selection == 3) {
                        System.out.println("\n>>>>> View the Total Amount <<<<<");

//                        Sum SQL Condition
                        String sum = "SELECT SUM(Payment) as 'Total_Payment' From order_product WHERE Customer_Name = '"+UserID+"'";
                        ResultSet Total = st.executeQuery(sum);
                        Total.next();

                        System.out.println("Total Payment : Rs." + Total.getFloat("Total_Payment"));

                    }

                    System.out.print("\nDo you want to go Main-menu (Y/N): ");
                    menu = input.next().toLowerCase().charAt(0);
                }
                break;
            }else {
                System.out.println("User_ID or Password Invalid");
            }
            count ++;
        }
    }

}